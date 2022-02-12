package com.bluntsoftware.shirtshop.mapper;


import com.bluntsoftware.shirtshop.model.Customer;
import com.intuit.ipp.data.EmailAddress;


public class QBMapper {

    public static com.intuit.ipp.data.Customer mapCustomer(Customer customer){
        com.intuit.ipp.data.Customer qbCustomer = new com.intuit.ipp.data.Customer();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setAddress(customer.getEmail());
        qbCustomer.setId(customer.getId());
        qbCustomer.setGivenName(customer.getFirstName());
        qbCustomer.setFamilyName(customer.getLastName());
        qbCustomer.setCompanyName(customer.getName());
        qbCustomer.setPrimaryEmailAddr(emailAddress);

        return  qbCustomer;
    }



}
