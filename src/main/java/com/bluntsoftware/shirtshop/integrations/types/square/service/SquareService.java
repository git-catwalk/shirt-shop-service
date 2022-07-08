package com.bluntsoftware.shirtshop.integrations.types.square.service;

import com.bluntsoftware.shirtshop.integrations.service.IntegrationService;
import com.bluntsoftware.shirtshop.integrations.types.square.config.SquareConfig;
import com.bluntsoftware.shirtshop.model.LineItem;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.LocationsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class SquareService {
    private final IntegrationService integrationService;
    private final SquareConfig squareConfig;
    private String location = "LCMDQTP7CSEHV";
    public SquareService(IntegrationService integrationService, SquareConfig squareConfig) {
        this.integrationService = integrationService;
        this.squareConfig = squareConfig;
    }

    String getDefaultLocation(){
        try {
            List<Location> activeLocations = listLocations()
                    .stream()
                    .filter(l-> l.getStatus().equalsIgnoreCase("Active"))
                    .collect(Collectors.toList());

            if(activeLocations.size() > 0){
                return this.location = activeLocations.get(0).getId();
            }
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    SquareClient getSandboxClient(){
        return new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(squareConfig.getSandboxToken())
                .build();
    }

    SquareClient getProductionClient(){
        Map<String,Object>  credentials = this.integrationService.getCredentials("square");
        if(credentials.containsKey("access_token")){
            return new SquareClient.Builder()
                    .environment(Environment.PRODUCTION)
                    .accessToken( credentials.get("access_token").toString())
                    .build();
        }
        return null;
    }

    SquareClient getClient(){
        if(squareConfig.getSandboxToken() != null && !squareConfig.getSandboxToken().isEmpty()){
            return getSandboxClient();
        }else{
            return getProductionClient();
        }
    }

    public List<Location> listLocations() throws IOException, ApiException {
        SquareClient squareClient = getClient();
        LocationsApi locationsApi = squareClient.getLocationsApi();
        ListLocationsResponse resp = locationsApi.listLocations();
        return resp.getLocations();
    }

    public String createAnInvoiceLink(com.bluntsoftware.shirtshop.model.Invoice invoice){
        try {
            String location = getDefaultLocation();
            if(location == null){
                throw new ApiException("A Location is needed");
            }else{
                this.location = location;
            }
            Order order = createAnOrder(invoice);
            Invoice sqInvoice = createInvoice(order,invoice);
            return publishInvoice(sqInvoice).getPublicUrl();
        } catch (ApiException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Invoice publishInvoice(Invoice invoice) throws IOException, ApiException {
        PublishInvoiceRequest body = new PublishInvoiceRequest.Builder(invoice.getVersion())
                .build();
        PublishInvoiceResponse resp = getClient().getInvoicesApi().publishInvoice(invoice.getId(), body);
        return resp.getInvoice();
    }

    Invoice createInvoice(Order order,com.bluntsoftware.shirtshop.model.Invoice invoice) throws IOException, ApiException {
        boolean canTakeAch = true;

        InvoiceRecipient primaryRecipient = new InvoiceRecipient.Builder()
                .customerId(order.getCustomerId())
                .build();

        LinkedList<InvoicePaymentRequest> paymentRequests = new LinkedList<>();
        InvoicePaymentRequest invoicePaymentRequest = new InvoicePaymentRequest.Builder()
                .requestType("BALANCE")
                .dueDate(invoice.getDateDue() != null ? getSimpleDate(invoice.getDateDue()) : getFutureDate(15))
                .automaticPaymentSource("NONE")
                .build();
        paymentRequests.add(invoicePaymentRequest);

        if(invoice.getDepositPercentage() != null && !invoice.getDepositPercentage().isEmpty() && !invoice.getDepositPercentage().equalsIgnoreCase("0")){
            InvoicePaymentRequest invoiceDepositRequest = new InvoicePaymentRequest.Builder()
                    .requestType("DEPOSIT")
                    .dueDate(getSimpleDate(new Date()))
                    .percentageRequested(invoice.getDepositPercentage())
                    .automaticPaymentSource("NONE")
                    .build();
            paymentRequests.add(invoiceDepositRequest);
            canTakeAch = false;
        }

        InvoiceAcceptedPaymentMethods acceptedPaymentMethods = new InvoiceAcceptedPaymentMethods.Builder()
                .card(true)
                .squareGiftCard(false)
                .bankAccount(canTakeAch)
                .build();

        CreateInvoiceRequest body = new CreateInvoiceRequest.Builder(new Invoice.Builder()
                .locationId(location)
                .orderId(order.getId())
                .primaryRecipient(primaryRecipient)
                .paymentRequests(paymentRequests)
                .deliveryMethod("SHARE_MANUALLY")
                .invoiceNumber("INV-" + StringUtils.leftPad(invoice.getInvoiceNumber().toString(), 5, "0") )
                //.description("We appreciate your business!")
                .acceptedPaymentMethods(acceptedPaymentMethods)
                .saleOrServiceDate(getSimpleDate(new Date()))
                .build())
          .build();

        return getClient().getInvoicesApi().createInvoice(body).getInvoice();
    }

    String getSimpleDate(Date date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    String getFutureDate(int numDays){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, numDays);
        return getSimpleDate(c.getTime());
    }

    public Order findOrderByRefId(String referenceId) throws IOException, ApiException {
        LinkedList<String> locationIds = new LinkedList<>();
        locationIds.add(location);
        SearchOrdersRequest body = new SearchOrdersRequest.Builder().locationIds(locationIds).build();
        List<Order> orders = getClient().getOrdersApi().searchOrders(body)
                .getOrders().stream().filter((o)-> o.getReferenceId().equalsIgnoreCase(referenceId))
                .collect(Collectors.toList());
         if(orders.size() > 0) return orders.get(0);
         throw new ApiException("Order Not found");
    }

    Order findOrCreateOrder(com.bluntsoftware.shirtshop.model.Invoice invoice) throws IOException, ApiException {
        try {
            return findOrderByRefId(invoice.getId());
        } catch (IOException | ApiException e) {
            return createAnOrder(invoice);
        }
    }

    Order createAnOrder(com.bluntsoftware.shirtshop.model.Invoice invoice) throws IOException, ApiException {
        Order order = mapInvoiceToOrder(invoice);
        CreateOrderRequest orderRequest = new CreateOrderRequest.Builder()
                .order(order).build();
        CreateOrderResponse resp = getClient().getOrdersApi().createOrder(orderRequest);
        return  resp.getOrder();
    }

    String getSizesDescription(LineItem li){
        StringBuilder sizes = new StringBuilder();
        for(String sizeName:li.getSizes().keySet()){
            Integer qty = li.getSizes().get(sizeName).getQty();
            if(qty != null && qty > 0){
                sizes.append(qty).append(":").append(sizeName).append(" ");
            }
        }
        return sizes.toString();
    }

    Order mapInvoiceToOrder(com.bluntsoftware.shirtshop.model.Invoice invoice) throws IOException, ApiException {
        Order.Builder builder = new Order.Builder(location)
                .referenceId(invoice.getId())
                .customerId(findOrCreateCustomer(invoice.getCustomer()).getId());

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for(LineItem li:invoice.getItems()){

            AtomicReference<Integer> qty = new AtomicReference<>(0);
            li.getSizes().values().forEach((qs)->   {
                qty.updateAndGet(v -> v + (qs != null && qs.getQty() != null ? qs.getQty() : 0));
            });
            li.getNamesNumbers().forEach((nn)->{
                qty.updateAndGet(v -> v + (nn != null && nn.getQty() != null ? nn.getQty() : 0));
            });
            Number itemAmount = li.getCostEa().doubleValue() *100;
            OrderLineItem lineItem = new OrderLineItem.Builder(qty.get().toString())
                    .name(li.getDescription() + " (" + li.getGarmentColor().getName() + ")")
                    .note(getSizesDescription(li) + "\n\r" + htmlToText(li.getGarmentStyle().getDescription()))
                    .basePriceMoney(new Money.Builder()
                            .currency("USD")
                            .amount(itemAmount.longValue()).build())
                    .build();

            orderLineItems.add(lineItem);
        }
        builder.lineItems(orderLineItems);

        Double taxRate = invoice.getCustomer().getTaxRate();
        if(taxRate != null && taxRate > 0.0){
            List<OrderLineItemTax> taxes = new ArrayList<>();
            OrderLineItemTax tax = new OrderLineItemTax.Builder()
                    .uid("state-sales-tax")
                    .name("State Sales Tax")
                    .percentage(taxRate.toString())
                    .scope("ORDER")
                    .build();
            taxes.add( tax);
            builder.taxes(taxes);
        }

        if(invoice.getDiscountTotal() != null && invoice.getDiscountTotal().doubleValue() > 0){
            Number discountAmount = invoice.getDiscountTotal().doubleValue() *100;
            OrderLineItemDiscount discount = new OrderLineItemDiscount.Builder()
                    .amountMoney(new Money.Builder()
                            .currency("USD")
                            .amount(discountAmount.longValue()).build())
                    .name("Discount")
                    .build();
            List<OrderLineItemDiscount> discounts = new ArrayList<>();
            discounts.add(discount);
            builder.discounts(discounts);
        }
        return builder.build();
    }

    private String htmlToText(String description) {
        return Jsoup.parse(description).wholeText();
    }

    Customer findOrCreateCustomer(com.bluntsoftware.shirtshop.model.Customer customer) throws IOException, ApiException {
        try {
            return findCustomerByRefId(customer.getId());
        } catch (IOException | ApiException e) {
           return createCustomer(customer);
        }
    }

    Customer createCustomer(com.bluntsoftware.shirtshop.model.Customer customer) throws IOException, ApiException {
        CreateCustomerRequest customerRequest = new CreateCustomerRequest.Builder()
                .emailAddress(customer.getEmail())
                .nickname(customer.getName())
                .givenName(customer.getFirstName())
                .familyName(customer.getLastName())
                .phoneNumber(customer.getPhone())
                .referenceId(customer.getId()).build();
        CreateCustomerResponse resp = getClient().getCustomersApi().createCustomer(customerRequest);
        return resp.getCustomer();
    }

    Customer findCustomerByRefId(String referenceId) throws IOException, ApiException {
       List<Customer> customers = getClient().getCustomersApi().searchCustomers(new SearchCustomersRequest.Builder()
               .limit(1L)
               .query(new CustomerQuery.Builder()
                       .filter(new CustomerFilter.Builder()
                               .referenceId(new CustomerTextFilter.Builder()
                                       .exact(referenceId).build())
                               .build()).build())
               .build()).getCustomers();
       if(customers != null && customers.size() > 0){ return customers.get(0);}
       throw new ApiException("Customer Not Found");
    }
}
