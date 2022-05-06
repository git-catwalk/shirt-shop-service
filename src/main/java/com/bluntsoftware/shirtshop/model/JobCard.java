package com.bluntsoftware.shirtshop.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@SuperBuilder
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
