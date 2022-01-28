package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
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
	private Map<String,QtySize> sizes;
	private List<Garment> garments;
	private String styleNumber;
	private Integer discount;
	private String brand;
	private String thumbnail;
	private BigDecimal garmentPriceTotal;
	private BigDecimal costEa;
	private Integer totalColors;
	private String notes;
	private List<PrintLocation> printLocations;
	private List<Screen> screens;
}
