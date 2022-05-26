package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.integrations.types.quick_books.service.QuickbooksApiService;
import com.bluntsoftware.shirtshop.mapper.QBMapper;
import com.bluntsoftware.shirtshop.model.*;
import com.bluntsoftware.shirtshop.repository.EstimateRepo;
import com.bluntsoftware.shirtshop.repository.GarmentStyleRepo;
import com.bluntsoftware.shirtshop.repository.SequenceRepo;
import com.bluntsoftware.shirtshop.tenant.TenantUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
  private final AuditTrailService auditTrailService;
  private final GarmentStyleRepo garmentStyleRepo;

  private static final String ESTIMATE_SEQUENCE_KEY = "estimate-seq-key";

  public EstimateService(QuickbooksApiService quickbooksService, EstimateRepo repo, SequenceRepo sequenceRepo, InvoiceService invoiceService, AuditTrailService auditTrailService, GarmentStyleRepo garmentStyleRepo) {
    this.quickbooksService = quickbooksService;
    this.repo = repo;
    this.sequenceRepo = sequenceRepo;
    this.invoiceService = invoiceService;
    this.auditTrailService = auditTrailService;
    this.garmentStyleRepo = garmentStyleRepo;
  }

  public  Estimate save(Estimate item) {
    if(item.getCreated() == null){
      item.setCreated(new Date());
    }
    item.setModified(new Date());

    if(item.getEstimateNumber() == null){
      if(TenantUserService.getUser().isPresent()){
        item.setOwner(TenantUserService.getUser().get().getEmail());
      }
      Long estimateNumber  = sequenceRepo.getNextSequenceId(ESTIMATE_SEQUENCE_KEY);
      item.setEstimateNumber(estimateNumber);

      Estimate estimate =  repo.save(item);
      auditTrailService.audit(" created a new Estimate " + "EST-" + estimateNumber +  "for " + item.getCustomer().getName(),estimate.getId());
      return estimate;
    }
    updatePricing(item);
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
      auditTrailService.audit(" created a new Invoice from estimate " + "EST-" + estimate.getEstimateNumber() +  "for " + estimate.getCustomer().getName(),invoice.getId());
      //lock the estimate
      return invoice;
    }
    return null;
  }

  public void updatePricing(){
    this.repo.findAll().forEach(this::updatePricing);
  }

  void updatePricing(Estimate e){
    e.getItems().forEach(this::updatePricing);
  }

  void updatePricing(LineItem i){
    GarmentStyle gs = i.getGarmentStyle();
    if(gs.getEstPrice() == null){
      Optional<QtySize> qtySize =i.getSizes().values().stream().findFirst();
      if(qtySize.isPresent() && qtySize.get().getCustomerPrice() != null){
        BigDecimal avgCost = qtySize.get().getCustomerPrice();
        GarmentStyle current = garmentStyleRepo.findByStyleIdAndReseller(gs.getStyleId(),gs.getReseller());
        if(current != null){
          gs = current;
        }
        gs.setEstPrice(avgCost);
        garmentStyleRepo.save(gs);
      }
    }
  }

  public Invoice convertToInvoice(Estimate estimate){

    return Invoice.builder()
            .estimateId(estimate.getId())
            .customer(estimate.getCustomer())
            .pricingProfile(estimate.getPricingProfile())
            .dateDue(estimate.getDateDue())
            .items(estimate.getItems())
            .build();
  }
}
