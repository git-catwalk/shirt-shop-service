package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Garment {

	@Id
	private String id;
	private String brand;
	private BigDecimal cost;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date costDate;
	private String color;
	private Manufacturer manufacturer;
	private String description;
	private BigDecimal twoXCost;
	private BigDecimal threeXCost;
}