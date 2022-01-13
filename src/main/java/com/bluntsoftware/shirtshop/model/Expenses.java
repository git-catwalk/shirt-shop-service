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
public class Expenses {

	@Id
	private String id;
	private String merchant;
	private String category;
	private BigDecimal amount;
	private Integer quantity;
	private String description;
}