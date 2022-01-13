package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Defaults {

	@Id
	private String id;
	private BigDecimal screenCostPerColor;
	private BigDecimal filmPerColor;
	private BigDecimal artCostPerSide;
	private BigDecimal shippingCostPerShirt;
	private BigDecimal printLaborHoursPerColor;
}