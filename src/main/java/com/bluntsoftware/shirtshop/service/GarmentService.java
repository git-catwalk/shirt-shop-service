package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.repository.GarmentRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class GarmentService{

  private final GarmentRepo repo;

  public GarmentService(GarmentRepo repo) {
    this.repo = repo;
  }

  public  Garment save(Garment item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Garment> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Garment> findAll() {
    return repo.findAll();
  }

  public Page<Garment> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }
}
