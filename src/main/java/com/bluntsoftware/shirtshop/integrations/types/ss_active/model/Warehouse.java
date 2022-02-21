package com.bluntsoftware.shirtshop.integrations.types.ss_active.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    String warehouseAbbr;
    Integer skuID;
    Integer qty;
    Boolean closeout;
    Boolean dropship;
    Boolean excludeFreeFreight;
    Boolean fullCaseOnly;
    Boolean returnable;
}
