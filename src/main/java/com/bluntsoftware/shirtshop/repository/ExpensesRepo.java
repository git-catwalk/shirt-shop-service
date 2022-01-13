package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Expenses;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepo extends MongoRepository<Expenses, String> {
}