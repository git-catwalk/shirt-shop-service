package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class GarmentStyle {
	@Id
	private String id;
	String styleId;
	String reseller;
	String partNumber;
	@TextIndexed(weight=3)
	String brandName;
	@TextIndexed(weight=5)
	String styleName;
	@TextIndexed
	String title;
	String description;
	String brandImage;
	String styleImage;
	BigDecimal estPrice;
	@TextScore
	Float score;
}
