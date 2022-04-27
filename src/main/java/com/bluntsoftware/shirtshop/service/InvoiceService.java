package com.bluntsoftware.shirtshop.service;


import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.repository.InvoiceRepo;
import com.bluntsoftware.shirtshop.repository.SequenceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class InvoiceService {
    private final InvoiceRepo repo;
    private final SequenceRepo sequenceRepo;
    private static final String INVOICE_SEQUENCE_KEY = "invoice-seq-key";

    public InvoiceService(InvoiceRepo repo, SequenceRepo sequenceRepo) {
        this.repo = repo;
        this.sequenceRepo = sequenceRepo;
    }
    public Invoice save(Invoice item) {
        if(item.getInvoiceNumber() == null){
            item.setInvoiceNumber(sequenceRepo.getNextSequenceId(INVOICE_SEQUENCE_KEY));
        }
        return repo.save(item);
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
