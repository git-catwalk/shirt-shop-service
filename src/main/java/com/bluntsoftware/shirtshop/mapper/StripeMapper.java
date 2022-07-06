package com.bluntsoftware.shirtshop.mapper;

import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.model.LineItem;
import com.stripe.model.Address;
import com.stripe.model.Discount;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class StripeMapper {

   public static Customer stripeCustomerToShirtShopCustomer(com.stripe.model.Customer stripeCustomer){

       Address address = stripeCustomer.getAddress() != null ? stripeCustomer.getAddress() : new Address() ;
       Address shipAddress = stripeCustomer.getShipping() != null && stripeCustomer.getShipping().getAddress() != null ? stripeCustomer.getShipping().getAddress() : new Address() ;

       return Customer.builder()
                .email(stripeCustomer.getEmail())
                .phone(stripeCustomer.getPhone())
                .name(stripeCustomer.getName())
                .street1(address.getLine1())
                .street2(address.getLine2())
                .city(address.getCity())
                .country(address.getCountry())
                .state(address.getPostalCode())
                .zipcode(address.getPostalCode())
                .shipStreet1(shipAddress.getLine1())
                .shipStreet2(shipAddress.getLine2())
                .shipCity(shipAddress.getCity())
                .shipState(shipAddress.getPostalCode())
                .shipZipcode(shipAddress.getPostalCode())
                .shipCountry(shipAddress.getCountry())
                .build();
    }

    public static CustomerCreateParams shirtShopCustomerToStripeCustomer(Customer customer){
        Map<String,String>  meta = new HashMap<>();
        meta.put("customerId",customer.getId());
       return CustomerCreateParams.builder()
                .setMetadata(meta)
                .setEmail(customer.getEmail())
                .setName(customer.getName())
                .setPhone(customer.getPhone())
                .setAddress(CustomerCreateParams.Address.builder()
                        .setCity(customer.getCity())
                        .setLine1(customer.getStreet1())
                        .setLine2(customer.getStreet2())
                        .setState(customer.getState())
                        .setPostalCode(customer.getZipcode())
                        .setCountry(customer.getCountry())
                        .build())
                .setShipping(CustomerCreateParams.Shipping.builder()
                        .setName(customer.getName())
                        .setPhone(customer.getPhone())
                        .setAddress( CustomerCreateParams.Shipping.Address.builder()
                                .setCity(customer.getShipCity())
                                .setLine1(customer.getShipStreet1())
                                .setLine2(customer.getShipStreet2())
                                .setState(customer.getShipState())
                                .setPostalCode(customer.getShipZipcode())
                                .setCountry(customer.getShipCountry())
                                .build())
                        .build())
                .build();
    }

    public static ProductCreateParams lineItemToProduct(LineItem li){
       return ProductCreateParams.builder()
                .setName(li.getDescription())
                .setDescription(li.getGarmentStyle().getDescription())
                .build();
    }

    public static PriceCreateParams lineItemToPrice(LineItem li, String productId){
       Number cost = li.getCostEa().doubleValue()* 100;
       return PriceCreateParams.builder()
               .setProduct(productId)
               .setCurrency("usd")
               .setTaxBehavior(PriceCreateParams.TaxBehavior.EXCLUSIVE)
               .setUnitAmount(cost.longValue())
               .build();
    }

    public static InvoiceItemCreateParams lineItemToInvoiceItem(LineItem li,String customerId,String priceId) {
        AtomicReference<Integer> qty = new AtomicReference<>(0);
                li.getSizes().values().forEach((qs)->   {
                    qty.updateAndGet(v -> v + qs.getQty());
                });
                li.getNamesNumbers().forEach((nn)->{
                    qty.updateAndGet(v -> v + nn.getQty());
                });

        Number cost = li.getCostEa().doubleValue() * 100;

        return InvoiceItemCreateParams.builder()
                .setCustomer(customerId)
                .setDescription(li.getDescription())
                .setQuantity(Long.parseLong(qty.get().toString()))
                .setUnitAmount(cost.longValue())
                .setCurrency("usd")
                .build();
    }
}
