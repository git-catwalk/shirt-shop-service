package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.AuditTrail;
import com.bluntsoftware.shirtshop.repository.AuditTrailRepo;
import com.bluntsoftware.shirtshop.tenant.TenantUserService;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class AuditTrailService {
    private final AuditTrailRepo repo;

    public AuditTrailService(AuditTrailRepo repo) {
        this.repo = repo;
    }

    public AuditTrail audit(String what,String whereId){
        AuditTrail auditTrail = AuditTrail.builder()
                .what(what)
                .where(whereId)
                .when(new Date())
                .who(TenantUserService.getUser().get().getName())
                .build();
        return repo.save(auditTrail);
    }

    public AuditTrail audit(String what,String orderId,String lineItemId){
        return audit(what,orderId + "-" + lineItemId);
    }

    public List<AuditTrail> findAllByWhere(List<String> where){
        return repo.findAllByWhereInOrderByWhenAsc(where);
    }
}
