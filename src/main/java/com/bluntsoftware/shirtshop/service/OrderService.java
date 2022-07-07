package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.exception.ValidationException;
import com.bluntsoftware.shirtshop.integrations.types.square.service.SquareService;
import com.bluntsoftware.shirtshop.integrations.types.stripe.service.StripeService;
import com.bluntsoftware.shirtshop.model.*;
import com.bluntsoftware.shirtshop.repository.GarmentStyleRepo;
import com.bluntsoftware.shirtshop.repository.OrderRepo;
import com.bluntsoftware.shirtshop.repository.SequenceRepo;
import com.intuit.ipp.exception.BadRequestException;
import com.stripe.exception.StripeException;
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
    private final StripeService stripeService;
    private final SquareService squareService;
    private static final String INVOICE_SEQUENCE_KEY = "invoice-seq-key";
    private static final String ORDER_SEQUENCE_KEY = "order-seq-key";

    public OrderService(OrderRepo repo, SequenceRepo sequenceRepo, GarmentStyleRepo garmentStyleRepo, StripeService stripeService, SquareService squareService) {
        this.repo = repo;
        this.sequenceRepo = sequenceRepo;
        this.garmentStyleRepo = garmentStyleRepo;
        this.stripeService = stripeService;
        this.squareService = squareService;
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

        return repo.save(ord);
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
        invoice.setPaymentUrl(squareService.createAnInvoiceLink(invoice));
        //invoice.setPaymentUrl(stripeService.createAnInvoiceLink(invoice));
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
