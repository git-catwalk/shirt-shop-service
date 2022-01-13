package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.InvoiceItems;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemsRepo extends MongoRepository<InvoiceItems, String> {
}