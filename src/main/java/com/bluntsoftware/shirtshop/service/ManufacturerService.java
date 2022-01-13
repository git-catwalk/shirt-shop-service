package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Manufacturer;
import com.bluntsoftware.shirtshop.repository.ManufacturerRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class ManufacturerService{

  private final ManufacturerRepo repo;

  public ManufacturerService(ManufacturerRepo repo) {
    this.repo = repo;
  }

  public  Manufacturer save(Manufacturer item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Manufacturer> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Manufacturer> findAll() {
    return repo.findAll();
  }

  public Page<Manufacturer> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }
}
