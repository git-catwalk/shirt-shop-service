package com.bluntsoftware.shirtshop.mapper;


import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.model.Estimate;
import com.intuit.ipp.data.EmailAddress;
import com.intuit.ipp.data.ReferenceType;


public class QBMapper {

    public static com.intuit.ipp.data.Customer mapCustomer(Customer customer){
        com.intuit.ipp.data.Customer qbCustomer = new com.intuit.ipp.data.Customer();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setAddress(customer.getEmail());
       // qbCustomer.setId(customer.getId());
        qbCustomer.setGivenName(customer.getFirstName());
        qbCustomer.setFamilyName(customer.getLastName());
        qbCustomer.setCompanyName(customer.getName());
        qbCustomer.setPrimaryEmailAddr(emailAddress);
        qbCustomer.setClientEntityId(customer.getId());
        return  qbCustomer;
    }

    public static Customer mapCustomer(com.intuit.ipp.data.Customer qbc){
        return Customer.builder()
                .id(qbc.getId())
                .firstName(qbc.getGivenName())
                .lastName(qbc.getFamilyName())
                .name(qbc.getCompanyName())
                .phone(qbc.getPrimaryPhone() != null ? qbc.getPrimaryPhone().getFreeFormNumber() : null)
                .email(qbc.getPrimaryEmailAddr() != null ? qbc.getPrimaryEmailAddr().getAddress() : null)
                .build();
    }

    public static com.intuit.ipp.data.Estimate mapEstimate( Estimate estimate) {
        com.intuit.ipp.data.Estimate qbEstimate = new com.intuit.ipp.data.Estimate();
        com.intuit.ipp.data.Customer customer = mapCustomer(estimate.getCustomer());

        //qbEstimate.set

        return null;
    }
}
