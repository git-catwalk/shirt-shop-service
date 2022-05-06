package com.bluntsoftware.shirtshop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Invoice {

	@Id
	private String id;
	private Long invoiceNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date created;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date modified;
	private String estimateId;
	private Customer customer;
	private String description;
	private String owner;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date dateDue;
	private String status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date orderDate;
	private String depositUrl;
	private String depositPercentage;
	private BigDecimal depositAmount;
	private String paymentUrl;
	private PriceProfile pricingProfile;
	private List<LineItem> items;
}
