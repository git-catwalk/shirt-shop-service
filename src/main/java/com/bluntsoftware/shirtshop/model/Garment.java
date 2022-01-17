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
import java.util.HashMap;
import java.util.Map;

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
	private String catalogPageNumber;
	private String styleNumber;
	private String size;
	private String casePack;
	private Map<String, Map<String,BigDecimal>> matrix;
	public void addCost(String size, String colorFamily, BigDecimal cost){
		if(matrix == null){
			matrix = new HashMap<>();
		}
		Map<String,BigDecimal> color = new HashMap<>();
		if(matrix.containsKey(size)){
			color = matrix.get(size);
		}
		color.put(colorFamily, cost);
		matrix.put(size,color);
	}
}
