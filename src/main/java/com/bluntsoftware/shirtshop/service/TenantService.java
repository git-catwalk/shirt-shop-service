package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Tenant;
import com.bluntsoftware.shirtshop.repository.TenantRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class TenantService{

  private final TenantRepo repo;

  public TenantService(TenantRepo repo) {
    this.repo = repo;
  }

  public  Tenant save(Tenant item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Tenant> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Tenant> findAll() {
    return repo.findAll();
  }

  public Page<Tenant> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }
}
