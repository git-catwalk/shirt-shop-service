package com.bluntsoftware.shirtshop.repository;

import com.bluntsoftware.shirtshop.model.AuditTrail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditTrailRepo  extends MongoRepository<AuditTrail, String> {
    List<AuditTrail> findAllByWhereInOrderByWhenAsc(List<String> where);
}
