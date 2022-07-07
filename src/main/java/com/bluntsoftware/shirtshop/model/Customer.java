package com.bluntsoftware.shirtshop.model;

import com.bluntsoftware.shirtshop.util.CheckAtLeastOneNotEmpty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@CheckAtLeastOneNotEmpty(fieldNames={"email","phone"},message="customer email or phone should not be empty")
public class Customer {

	@Id
	private String id;

	@NotNull
	private String name;
	private String firstName;
	private String lastName;
	private String accountNumber;
	@Email(message = "customer email should be valid")
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date created;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date modified;
}
