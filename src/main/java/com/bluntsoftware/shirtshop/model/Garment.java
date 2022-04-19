package com.bluntsoftware.shirtshop.model;

import com.bluntsoftware.shirtshop.integrations.types.ss_active.model.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garment {
    @Id
    private String id;
    String sku;
    String gtin;
    String styleId;
    String brandName;
    String colorName;
    String colorCode;
    String webColor;
    String sizeName;
    String sizeCode;
    String colorFrontImage;
    BigDecimal piecePrice;
    BigDecimal dozenPrice;
    BigDecimal casePrice;
    BigDecimal salePrice;
    BigDecimal customerPrice;
    Integer qty;
    List<Warehouse> warehouses;
}

