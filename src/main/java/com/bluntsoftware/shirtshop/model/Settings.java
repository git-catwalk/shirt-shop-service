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
public class Settings {

	@Id
	private String id;
	private BigDecimal materialMarkup;
	private BigDecimal laborMarkup;
	private BigDecimal overallMarkup;
	private BigDecimal inkMsi;
}