package com.bluntsoftware.shirtshop.service;


import com.bluntsoftware.shirtshop.model.*;
import com.bluntsoftware.shirtshop.repository.GarmentStyleRepo;
import com.bluntsoftware.shirtshop.repository.InvoiceRepo;
import com.bluntsoftware.shirtshop.repository.SequenceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class InvoiceService {
    private final InvoiceRepo repo;
    private final SequenceRepo sequenceRepo;
    private final GarmentStyleRepo garmentStyleRepo;
    private static final String INVOICE_SEQUENCE_KEY = "invoice-seq-key";

    public InvoiceService(InvoiceRepo repo, SequenceRepo sequenceRepo, GarmentStyleRepo garmentStyleRepo) {
        this.repo = repo;
        this.sequenceRepo = sequenceRepo;
        this.garmentStyleRepo = garmentStyleRepo;
    }
    public Invoice save(Invoice item) {
        if(item.getCreated() == null){
            item.setCreated(new Date());
        }
        item.setModified(new Date());
        if(item.getInvoiceNumber() == null){
            item.setInvoiceNumber(sequenceRepo.getNextSequenceId(INVOICE_SEQUENCE_KEY));
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
}
