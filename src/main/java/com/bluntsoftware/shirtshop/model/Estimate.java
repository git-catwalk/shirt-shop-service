package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date dateDue;
	private String status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date orderDate;
	private BigDecimal inkCost;
	private BigDecimal shirtCost;
	private String depositUrl;
	private String depositPercentage;
	private BigDecimal screenCost;
	private String paymentUrl;
	private BigDecimal shippingCost;
	private BigDecimal materialMarkup;
	private BigDecimal totalAmount;
	private BigDecimal laborCost;
	private BigDecimal depositAmount;
	private BigDecimal laborMarkup;
	private BigDecimal totalCost;
	private Integer numberOfShipments;
	private BigDecimal overrideTotal;
	private PriceProfile pricingProfile;
	private List<LineItem> items;

}
