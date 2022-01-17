package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItem {
	private String id;
	private String description;
	private Garment garment;
	private String styleNumber;
	private Integer discount;
	private String brand;
	private String thumbnail;
	private String garmentColor;
	private BigDecimal garmentPriceTotal;
	private BigDecimal costEa;
	private Integer totalColors;
	private Integer xs;
	private Integer sm;
	private Integer med;
	private Integer lg;
	private Integer xl;
	private Integer xxl;
	private Integer xxxl;
	private Integer xxxxl;
	private String notes;
	private List<PrintLocation> printLocations;
	private List<Screen> screens;
}
