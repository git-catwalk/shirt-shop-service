package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.exception.ValidationException;
import com.bluntsoftware.shirtshop.integrations.types.square.service.SquareService;
import com.bluntsoftware.shirtshop.integrations.types.stripe.service.StripeService;
import com.bluntsoftware.shirtshop.model.*;
import com.bluntsoftware.shirtshop.repository.GarmentStyleRepo;
import com.bluntsoftware.shirtshop.repository.OrderRepo;
import com.bluntsoftware.shirtshop.repository.SequenceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class OrderService {
    private final OrderRepo repo;
    private final SequenceRepo sequenceRepo;
    private final GarmentStyleRepo garmentStyleRepo;
   // private final StripeService stripeService;
    private final SquareService squareService;
    private final ItemCostingService itemCostingService;
    private static final String INVOICE_SEQUENCE_KEY = "invoice-seq-key";
    private static final String ORDER_SEQUENCE_KEY = "order-seq-key";

    public OrderService(OrderRepo repo, SequenceRepo sequenceRepo, GarmentStyleRepo garmentStyleRepo,  SquareService squareService, ItemCostingService itemCostingService) {
        this.repo = repo;
        this.sequenceRepo = sequenceRepo;
        this.garmentStyleRepo = garmentStyleRepo;
      //  this.stripeService = stripeService;
        this.squareService = squareService;
        this.itemCostingService = itemCostingService;
    }

    public Invoice save(Invoice item) {
        if(item.getOrderDate() == null){
            item.setOrderDate(new Date());
        }
        if(item.getCreated() == null){
            item.setCreated(new Date());
        }
        item.setModified(new Date());

        if(item.getOrderNumber() == null){
            item.setOrderNumber(sequenceRepo.getNextSequenceId(ORDER_SEQUENCE_KEY));
        }

        Invoice ord = repo.save(item);
        String orderStatus = LineItemService.orderStatus(ord);
        ord.setStatus(orderStatus);
        updatePricing(ord);
        //reconcile(ord);
        return repo.save(ord);
    }

    void reconcile(Invoice order){
      double total =  itemCostingService.getTotal(order.getItems(),order.getCustomer(),order.getPricingProfile());
      if(total != order.getAmountDue().doubleValue()){
          System.out.println("does not reconcile calculated total : " + total + "   set total : " + order.getAmountDue());
      }
    }

    public Map<String,Object> report(){
        Map<String,Object> report = new HashMap<>();
        report.put("byPrintType",getAllBreakdownByPrintType());
        report.put("totalSales",totalSales());
        return report;
    }

    double totalSales(){
        return totalSales(repo.findAll());
    }

    double totalSales(List<Invoice> list){
       return list.stream()
               .mapToDouble(o -> o.getAmountDue() != null ? o.getAmountDue().doubleValue() : 0.0)
               .sum();
    }

    Map<String,Double> getAllBreakdownByPrintType(){
        return getBreakdownByPrintType(repo.findAll());
    }

    Map<String,Double> getBreakdownByPrintType(List<Invoice> list){
        Map<String,Double> breakdown = new HashMap<>();
        list.forEach(ord-> ord.getItems()
                .forEach(li-> itemCostingService.breakDownByPrintType(li,ord.getPricingProfile(),breakdown)));
        return breakdown;
    }

    public void updatePricing(){
        this.repo.findAll().forEach(this::updatePricing);
    }

    void updatePricing(Invoice e){
        e.getItems().forEach(this::updatePricing);
    }

    void updatePricing(LineItem i){
        GarmentStyle gs = i.getGarmentStyle();
        if(gs.getEstPrice() == null){
            Optional<QtySize> qtySize =i.getSizes().values().stream().findFirst();
            if(qtySize.isPresent() && qtySize.get().getCustomerPrice() != null){
                BigDecimal avgCost = qtySize.get().getCustomerPrice();
                GarmentStyle current = garmentStyleRepo.findByStyleIdAndReseller(gs.getStyleId(),"S&SActiveWear");
                if(current != null){
                    gs = current;
                }
                gs.setEstPrice(avgCost);
                garmentStyleRepo.save(gs);
            }
        }
    }

    @Transactional
    public Invoice finalizeInvoice(Invoice invoice) throws ValidationException {
        List<String> errors = validate(invoice);
        if(errors.size() > 0){
            throw new ValidationException(String.format("Order has errors %s",Arrays.toString(errors.toArray())));
        }
        invoice.setInvoiceNumber(sequenceRepo.getNextSequenceId(INVOICE_SEQUENCE_KEY));
        String invoiceLink = squareService.createAnInvoiceLink(invoice);
        invoice.setPaymentUrl(invoiceLink);
        return repo.save(invoice);
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }

    public Optional<Invoice> findById(String id) {
        return repo.findById(id);
    }

    public Iterable<Invoice> findAll() {
        return repo.findAll();
    }

    public Page<Invoice> search(String term, Pageable pageable) {
        log.info("create a filter in repo for search term {}",term);
        return repo.findAllByCustomer_NameIgnoreCaseContaining(term,pageable);
    }


    List<String> validate(Invoice invoice){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Invoice>> invoiceErrors = validator.validate(invoice);
        Set<ConstraintViolation<Customer>> customerErrors = validator.validate(invoice.getCustomer());
        Set<ConstraintViolation<LineItem>> lineItemErrors = new HashSet<>();
        for(LineItem li : invoice.getItems()){
            lineItemErrors.addAll(validator.validate(li));
        }
        List<String> errors = new ArrayList<>();
        invoiceErrors.forEach(c-> errors.add(c.getMessage()));
        customerErrors.forEach(c-> errors.add(c.getMessage()));
        lineItemErrors.forEach(c-> errors.add(c.getMessage()));
        return errors;
    }

    public void fixOrderNumber() {
        findAll().forEach(i->{
            Long invNumber = i.getInvoiceNumber();
            Long ordNumber = i.getOrderNumber();
            if(invNumber != null && ordNumber == null){
                i.setOrderNumber(invNumber);
                repo.save(i);
            }
        });
    }
}
