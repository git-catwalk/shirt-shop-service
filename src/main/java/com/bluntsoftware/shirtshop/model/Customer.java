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
	private String email;
	private String phone;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zipcode;
}
