package com.bluntsoftware.shirtshop.model;

import com.bluntsoftware.shirtshop.integrations.ss_active.model.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garment {
    String sku;
    String gtin;
    String styleId;
    String brandName;
    String colorName;
    String colorCode;
    String webColor;
    String sizeName;
    String sizeCode;
    BigDecimal piecePrice;
    BigDecimal dozenPrice;
    BigDecimal casePrice;
    BigDecimal salePrice;
    BigDecimal customerPrice;
    Integer qty;
    List<Warehouse> warehouses;
}
