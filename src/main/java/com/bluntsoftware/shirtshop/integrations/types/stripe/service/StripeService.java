package com.bluntsoftware.shirtshop.integrations.types.stripe.service;

import com.bluntsoftware.shirtshop.mapper.StripeMapper;
import com.bluntsoftware.shirtshop.model.LineItem;
import com.bluntsoftware.shirtshop.service.StripeApiService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StripeService {
    private final StripeApiService stripeApiService;

    public StripeService(StripeApiService stripeApiService) {
        this.stripeApiService = stripeApiService;
    }

    public String createAnInvoiceLink(com.bluntsoftware.shirtshop.model.Invoice invoice) throws StripeException {
        Stripe.apiKey = stripeApiService.get().orElseThrow(()-> new RuntimeException("Secret Key Not Found")).getSecretKey();
        Stripe.clientId = stripeApiService.get().orElseThrow(()-> new RuntimeException("Publishable Key Not Found")).getPublishableKey();
        Customer customer;
        List<Customer> customers = Customer.search(CustomerSearchParams.builder().setQuery("metadata['customerId']:'" + invoice.getCustomer().getId() + "'").build()).getData();
        if(customers != null && customers.size() > 0){
            customer = customers.get(0);
        }else{
            customer = Customer.create(StripeMapper.shirtShopCustomerToStripeCustomer(invoice.getCustomer()));
        }

        for(LineItem li:invoice.getItems()){
            Product product = Product.create(StripeMapper.lineItemToProduct(li));
            Price price = Price.create(StripeMapper.lineItemToPrice(li,product.getId()));
            InvoiceItem.create(StripeMapper.lineItemToInvoiceItem(li,customer.getId(), price.getId()));
        }

        Map<String,String> invoiceMetaData = new HashMap<>();
        invoiceMetaData.put("orderId",invoice.getId());

        Invoice inv;
        List<Invoice> invoices = Invoice.search(InvoiceSearchParams.builder().setQuery("metadata['orderId']:'" + invoice.getId() + "'").build()).getData();
        if(invoices.size() > 0){
            inv = invoices.get(0);
        }else{
            InvoiceCreateParams.Builder builder = InvoiceCreateParams.builder()
                    .setMetadata(invoiceMetaData)
                    .setCustomer(customer.getId())
                    .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
                    .setDaysUntilDue(30L);
            if(invoice.getCustomer().getTaxRate() != null){
                builder.addDefaultTaxRate(getTaxRate(invoice));
            }
            if(invoice.getDiscountTotal() != null && invoice.getDiscountTotal().doubleValue() > 0){
                builder.addDiscount(getDiscount(invoice));
            }
            InvoiceCreateParams params = builder.build();
            inv = Invoice.create(params);
            Number total = invoice.getAmountDue().doubleValue() * 100;
            Number stripeTotal = inv.getTotal();

            if(Math.abs(stripeTotal.longValue() - total.longValue()) > 2) {
                throw new RuntimeException("Invoice Totals Dont Match " + stripeTotal.longValue() + " - " + total.longValue());
            }
            inv = inv.finalizeInvoice(InvoiceFinalizeInvoiceParams.builder().build());
        }
        return inv.getHostedInvoiceUrl();
    }

    private String getTaxRate(com.bluntsoftware.shirtshop.model.Invoice invoice) throws StripeException {
        Double taxRate = invoice.getCustomer().getTaxRate();
        if(taxRate != null){
            TaxRateCreateParams params =
                    TaxRateCreateParams
                            .builder()
                            .setDisplayName("Sales Tax")
                            .setInclusive(false)
                            .setPercentage(new BigDecimal(taxRate))
                            .setCountry("US")
                            .setState("PA")
                            .setJurisdiction("US - PA")
                            .setDescription("PA Sales Tax")
                            .build();

            TaxRate txRate =  TaxRate.create(params);
            if(txRate != null){
                return txRate.getId();
            }
        }
        return null;
    }

    InvoiceCreateParams.Discount getDiscount(com.bluntsoftware.shirtshop.model.Invoice invoice) throws StripeException {
        InvoiceCreateParams.Discount discount = null;
        if(invoice.getDiscountTotal() != null){
            Number discountAmount = invoice.getDiscountTotal().doubleValue() *100;
            Coupon coupon = Coupon.create(CouponCreateParams
                    .builder()
                    .setAmountOff(discountAmount.longValue())
                    .setCurrency("usd")
                    .setName("Discount")
                    .setDuration(CouponCreateParams.Duration.ONCE)
                    .build());
            discount = InvoiceCreateParams.Discount.builder()
                    .setCoupon(coupon != null ? coupon.getId() : null)
                    .build();
        }
        return discount;
    }
}
