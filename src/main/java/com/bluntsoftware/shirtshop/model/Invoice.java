package com.bluntsoftware.shirtshop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	private Long orderNumber;
	private Long invoiceNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date created;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date modified;
	private String estimateId;

	@NotNull(message="customer should not be null")
	private Customer customer;

	private String description;
	private String owner;

	@FutureOrPresent(message="order due date must be in the present or in the future")
	private Date dateDue;

	private BigDecimal amountDue;
	private BigDecimal amountPaid;
	private BigDecimal amountRemaining;
	private BigDecimal discountTotal;
	private String status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date orderDate;
	private String depositUrl;
	private String depositPercentage;
	private BigDecimal depositAmount;
	private String paymentUrl;
	private PriceProfile pricingProfile;
	private List<Label> tags;
	@Size(min=1,message="at least one line item for the order should exist")
	private List<LineItem> items;
	private List<CustomerTransaction> transactions;
}
