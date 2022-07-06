package com.bluntsoftware.shirtshop.repository;

import com.bluntsoftware.shirtshop.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends MongoRepository<Invoice, String> {
    Page<Invoice> findAllByCustomer_NameIgnoreCaseContaining(String term, Pageable pageable);
    List<Invoice>  findAllByCustomer_NameIgnoreCaseContainingOrStatusIgnoreCaseContainingOrInvoiceNumber(String customer,String status,Integer invNum);
}
