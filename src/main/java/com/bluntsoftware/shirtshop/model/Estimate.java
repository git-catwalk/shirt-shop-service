package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Estimate {

	@Id
	private String id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date created;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date modified;
	private Long estimateNumber;
	private Customer customer;
	private String description;
	private String owner;
	@FutureOrPresent(message="order due date must be in the present or in the future")
	private Date expirationDate;
	private String status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date orderDate;
	private String depositUrl;
	private String depositPercentage;
	private BigDecimal depositAmount;
	private String paymentUrl;
	private List<Label> tags;
	private PriceProfile pricingProfile;
	private List<LineItem> items;

}
