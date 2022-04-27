package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.integrations.types.quick_books.service.QuickbooksApiService;
import com.bluntsoftware.shirtshop.mapper.QBMapper;
import com.bluntsoftware.shirtshop.model.AuditTrail;
import com.bluntsoftware.shirtshop.model.Estimate;
import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.repository.AuditTrailRepo;
import com.bluntsoftware.shirtshop.repository.EstimateRepo;
import com.bluntsoftware.shirtshop.repository.SequenceRepo;
import com.bluntsoftware.shirtshop.tenant.TenantUserService;
import org.springframework.stereotype.Service;

import java.util.Date;
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
  private final InvoiceService invoiceService;
  private final AuditTrailRepo auditTrailRepo;

  private static final String ESTIMATE_SEQUENCE_KEY = "estimate-seq-key";

  public EstimateService(QuickbooksApiService quickbooksService, EstimateRepo repo, SequenceRepo sequenceRepo, InvoiceService invoiceService, AuditTrailRepo auditTrailRepo ) {
    this.quickbooksService = quickbooksService;
    this.repo = repo;
    this.sequenceRepo = sequenceRepo;
    this.invoiceService = invoiceService;
    this.auditTrailRepo = auditTrailRepo;

  }

  public  Estimate save(Estimate item) {
    if(item.getEstimateNumber() == null){

      if(TenantUserService.getUser().isPresent()){
        item.setOwner(TenantUserService.getUser().get().getEmail());
      }
      Long estimateNumber  = sequenceRepo.getNextSequenceId(ESTIMATE_SEQUENCE_KEY);
      item.setEstimateNumber(estimateNumber);

      Estimate estimate =  repo.save(item);
      AuditTrail auditTrail = AuditTrail.builder()
              .what(" created a new Estimate " + "EST-" + estimateNumber +  "for " + item.getCustomer().getName())
              .estimateId(estimate.getId())
              .when(new Date())
              .who(TenantUserService.getUser().get().getName())
              .build();
      auditTrailRepo.save(auditTrail);
      return estimate;
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
    return repo.findAllByCustomer_NameIgnoreCaseContaining(term,pageable);
  }

  void pushToQuickbooks(String id){
    Estimate estimate = repo.findById(id).orElse(null);
    quickbooksService.createOrGetEstimate(QBMapper.mapEstimate(estimate));
  }

  public Invoice createInvoice(String id) {
    Estimate estimate = findById(id).orElse(null);
    if(estimate != null){
      // todo: has an invoice already been created ?
      Invoice invoice = convertToInvoice(estimate);
      invoice = invoiceService.save(invoice);
      AuditTrail auditTrail = AuditTrail.builder()
              .what(" created a new Invoice from estimate " + "EST-" + estimate.getEstimateNumber() +  "for " + estimate.getCustomer().getName())
              .invoiceId(invoice.getId())
              .estimateId(estimate.getId())
              .when(new Date())
              .who(TenantUserService.getUser().get().getName())
              .build();
      auditTrailRepo.save(auditTrail);

      //lock the estimate
      return invoice;
    }
    return null;
  }

  public Invoice convertToInvoice(Estimate estimate){

    return Invoice.builder()
            .estimateId(estimate.getId())
            .customer(estimate.getCustomer())
            .items(estimate.getItems())
            .build();
  }
}
