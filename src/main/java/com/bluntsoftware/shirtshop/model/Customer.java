package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Customer {

	@Id
	private String id;
	private String name;
	private String firstName;
	private String lastName;
	private String accountNumber;
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
	private String shipContact;
	private String shipPhone;
	private String shipStreet1;
	private String shipStreet2;
	private String shipCity;
	private String shipState;
	private String shipCountry;
	private String shipZipcode;
	private String deliveryInstructions;
	private PriceProfile pricingProfile;
	private Double taxRate;
}
