package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.integrations.quick_books.service.QuickbooksApiService;
import com.bluntsoftware.shirtshop.mapper.QBMapper;
import com.bluntsoftware.shirtshop.model.Estimate;
import com.bluntsoftware.shirtshop.repository.EstimateRepo;
import com.bluntsoftware.shirtshop.repository.SequenceRepo;
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
  private final SequenceRepo sequenceRepo;
  private static final String ESTIMATE_SEQUENCE_KEY = "estimate-seq-key";
  public EstimateService(QuickbooksApiService quickbooksService, EstimateRepo repo, SequenceRepo sequenceRepo) {
    this.quickbooksService = quickbooksService;
    this.repo = repo;
    this.sequenceRepo = sequenceRepo;
  }

  public  Estimate save(Estimate item) {
    if(item.getEstimateNumber() == null){
      item.setEstimateNumber(sequenceRepo.getNextSequenceId(ESTIMATE_SEQUENCE_KEY));
    }
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

  void pushToQuickbooks(String id){
    Estimate estimate = repo.findById(id).orElse(null);
    quickbooksService.createOrGetEstimate(QBMapper.mapEstimate(estimate));
  }

  public void createInvoice(String id) {
    Estimate estimate = findById(id).orElse(null);
    if(estimate != null){
      // this.quickbooksService.createInvoice();
    }
  }
}
