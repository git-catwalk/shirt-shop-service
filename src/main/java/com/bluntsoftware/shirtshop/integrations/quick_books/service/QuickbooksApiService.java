package com.bluntsoftware.shirtshop.integrations.quick_books.service;

import com.bluntsoftware.shirtshop.integrations.Integration;
import com.bluntsoftware.shirtshop.integrations.quick_books.config.QuickbooksApiConfig;
import com.bluntsoftware.shirtshop.integrations.quick_books.model.QBEstimate;
import com.bluntsoftware.shirtshop.integrations.quick_books.model.QBInvoice;
import org.springframework.stereotype.Service;

@Service
public class QuickbooksApiService {
    private final QuickbooksApiConfig qbConfig;
    private final QuickbooksAuthService quickbooksAuthService;

    public QuickbooksApiService(QuickbooksApiConfig qbConfig, QuickbooksAuthService quickbooksAuthService) {
        this.qbConfig = qbConfig;
        this.quickbooksAuthService = quickbooksAuthService;
    }

    public void createInvoice(QBInvoice invoice) {
        //System.out.println(this.getCredentials());
    }

    public void createOrGetEstimate(QBEstimate estimate) {
        Integration integration = quickbooksAuthService.getCredentials();
        //System.out.println(this.getCredentials());
    }

    public void createOrGetCustomer(QBEstimate estimate) {
        //System.out.println(this.getCredentials());
    }

}
