package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen {

	private String description;
	private Integer mesh;
}