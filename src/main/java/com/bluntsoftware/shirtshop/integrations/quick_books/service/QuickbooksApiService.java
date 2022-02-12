package com.bluntsoftware.shirtshop.integrations.quick_books.service;

import com.bluntsoftware.shirtshop.integrations.Integration;
import com.bluntsoftware.shirtshop.integrations.quick_books.config.QuickbooksApiConfig;

import com.intuit.ipp.data.Customer;
import com.intuit.ipp.data.Estimate;
import com.intuit.ipp.data.Invoice;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.services.DataService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuickbooksApiService {
    private final QuickbooksApiConfig qbConfig;
    private final QuickbooksAuthService quickbooksAuthService;

    public QuickbooksApiService(QuickbooksApiConfig qbConfig, QuickbooksAuthService quickbooksAuthService) {
        this.qbConfig = qbConfig;
        this.quickbooksAuthService = quickbooksAuthService;
    }

    public void createInvoice( Invoice invoice) {
        //System.out.println(this.getCredentials());
    }

    public void createOrGetEstimate( Estimate estimate) {
        Integration integration = quickbooksAuthService.getCredentials();
        //System.out.println(this.getCredentials());
    }

    public void saveCustomer(Customer customer) throws FMSException {
        DataService ds = quickbooksAuthService.getDataService();
        Customer s = ds.add(customer);
        //System.out.println(this.getCredentials());
    }

    public Customer findCustomerById(String id) throws FMSException {
        DataService ds = quickbooksAuthService.getDataService();
        Customer customer = new Customer();
        customer.setId(id);
        return ds.findById(customer);
    }

    public List<Customer> findAll() throws FMSException {
        DataService ds = quickbooksAuthService.getDataService();
        return ds.findAll(new Customer());
    }

    public ResponseEntity<?> test() {
        try {
            return ResponseEntity.ok(findCustomerById("1"));
        } catch (FMSException e) {
            e.printStackTrace();
            throw new RuntimeException("Test Error");
        }
    }
}
