package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobBoard {
    List<JobCard> garmentOrders;
    List<JobCard> prepress;
    List<JobCard> production;
    List<JobCard> delivery;
}
