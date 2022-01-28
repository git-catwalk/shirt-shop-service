package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

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
	String brandName;
	String styleName;
	String title;
	String description;
	String brandImage;
	String styleImage;
}
