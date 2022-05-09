package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCard {
    long orderNumber;
    String invoiceId;
    String name;
    String description;
    Customer customer;
    LineItem lineItem;
    PrintLocation printLocation;
    List<String> labels;
    Date dueDate;
}
