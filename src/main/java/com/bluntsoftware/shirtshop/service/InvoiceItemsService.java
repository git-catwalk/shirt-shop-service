package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.InvoiceItems;
import com.bluntsoftware.shirtshop.repository.InvoiceItemsRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class InvoiceItemsService{

  private final InvoiceItemsRepo repo;

  public InvoiceItemsService(InvoiceItemsRepo repo) {
    this.repo = repo;
  }

  public  InvoiceItems save(InvoiceItems item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<InvoiceItems> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<InvoiceItems> findAll() {
    return repo.findAll();
  }

  public Page<InvoiceItems> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }
}
