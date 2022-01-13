package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Defaults;
import com.bluntsoftware.shirtshop.repository.DefaultsRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class DefaultsService{

  private final DefaultsRepo repo;

  public DefaultsService(DefaultsRepo repo) {
    this.repo = repo;
  }

  public  Defaults save(Defaults item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Defaults> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Defaults> findAll() {
    return repo.findAll();
  }

  public Page<Defaults> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }
}
