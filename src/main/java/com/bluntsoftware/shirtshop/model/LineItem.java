package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItem {
	private String id;
	private String description;
	private GarmentStyle garmentStyle;
	private GarmentColor garmentColor;
	private Double garmentMarkupPercentage;
	private Map<String,QtySize> sizes;
	private List<Garment> garments;
	private String styleNumber;
	private Integer discount;
	private String brand;
	private String thumbnail;
	private BigDecimal garmentPriceTotal;
	@NotNull
	private BigDecimal costEa;
	private Integer totalColors;
	private String notes;
	//Garment ordering
	private Date orderedDate;
	private Date estimatedDeliveryDate;
	private Date receivedDate;
	//line item Delivery
	private Date deliveryDate;
	private Date deliveryReceivedDate;

	@Size(min=1,message="at least one print location should exist")
	private List<PrintLocation> printLocations;
	private List<Screen> screens;
	private List<NamesNumbers> namesNumbers;
}
