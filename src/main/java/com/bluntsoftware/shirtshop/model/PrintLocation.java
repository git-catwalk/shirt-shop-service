package com.bluntsoftware.shirtshop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintLocation {
	private String description;
	private String printType;
	private String printTypeProperty;
	private Date prePressStartDate;
	private Date prePressCompleteDate;
	private Date productionStartDate;
	private Date productionCompleteDate;
	private List<Label> tags;
}
