package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.integrations.quick_books.model.QBInvoice;
import com.bluntsoftware.shirtshop.integrations.quick_books.service.QuickbooksApiService;
import com.bluntsoftware.shirtshop.model.Estimate;
import com.bluntsoftware.shirtshop.repository.EstimateRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class EstimateService{
  private final QuickbooksApiService quickbooksService;
  private final EstimateRepo repo;

  public EstimateService(QuickbooksApiService quickbooksService, EstimateRepo repo) {
    this.quickbooksService = quickbooksService;
    this.repo = repo;
  }

  public  Estimate save(Estimate item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Estimate> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Estimate> findAll() {
    return repo.findAll();
  }

  public Page<Estimate> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }

  public void createInvoice(String id) {
    Estimate estimate = findById(id).orElse(null);
    if(estimate != null){
       this.quickbooksService.createInvoice(QBInvoice.builder().id(estimate.getId()).build());
    }
  }
}
