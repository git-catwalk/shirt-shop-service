package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Company {

    @Id
    private String id;
    private String logo;
    private String name;
    private String email;
    private String phone;
    private String fax;
    private String mobile;
    private String tollFree;
    private String website;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
