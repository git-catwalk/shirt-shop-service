package com.bluntsoftware.shirtshop.integrations.types.quick_books.service;

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


    private final QuickBooksManager quickBooksManager;
    public QuickbooksApiService(QuickBooksManager quickBooksManager) {
        this.quickBooksManager = quickBooksManager;
    }

    public void createInvoice( Invoice invoice) {
        //System.out.println(this.getCredentials());
    }

    public void createOrGetEstimate( Estimate estimate) {

        //System.out.println(this.getCredentials());
    }

    public Customer saveCustomer(Customer customer) throws FMSException {
        DataService ds = quickBooksManager.getDataService();
        return ds.add(customer);
    }

    public Customer findCustomerById(String id) throws FMSException {
        DataService ds = quickBooksManager.getDataService();
        Customer customer = new Customer();
        customer.setId(id);
        return ds.findById(customer);
    }

    public List<Customer> findAll() throws FMSException {
        DataService ds = quickBooksManager.getDataService();
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
