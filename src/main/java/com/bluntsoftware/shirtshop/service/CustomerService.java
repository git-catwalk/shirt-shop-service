package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.repository.CustomerRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class CustomerService{

  private final CustomerRepo repo;

  public CustomerService(CustomerRepo repo) {
    this.repo = repo;
  }

  public  Customer save(Customer item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Customer> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Customer> findAll() {
    return repo.findAll();
  }

  public Page<Customer> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAll(pageable);
  }
}
