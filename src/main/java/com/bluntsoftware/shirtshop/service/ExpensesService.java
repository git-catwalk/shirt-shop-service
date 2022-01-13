package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Expenses;
import com.bluntsoftware.shirtshop.repository.ExpensesRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class ExpensesService{

  private final ExpensesRepo repo;

  public ExpensesService(ExpensesRepo repo) {
    this.repo = repo;
  }

  public  Expenses save(Expenses item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Expenses> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Expenses> findAll() {
    return repo.findAll();
  }

  public Page<Expenses> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }
}
