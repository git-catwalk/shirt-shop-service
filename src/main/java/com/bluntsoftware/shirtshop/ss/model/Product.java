package com.bluntsoftware.shirtshop.ss.model;

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
public class Product {
    String sku;
    String gtin;
    String styleId;
    String brandName;
    String styleName;
    String colorName;
    String colorCode;
    String colorPriceCodeName;
    String colorGroup;
    String colorGroupName;
    String colorFamilyID;
    String colorFamily;
    String colorSwatchImage;
    String colorSwatchTextColor;
    String colorFrontImage;
    String colorSideImage;
    String colorBackImage;
    String color1;
    String color2;
    String sizeName;
    String sizeCode;
    String sizeOrder;
    String sizePriceCodeName;
    Integer caseQty;
    Double unitWeight;
    BigDecimal mapPrice;
    BigDecimal piecePrice;
    BigDecimal dozenPrice;
    BigDecimal casePrice;
    BigDecimal salePrice;
    BigDecimal customerPrice;
    Double caseWeight;
    Double caseWidth;
    Double caseLength;
    Double caseHeight;
    Integer qty;
    String countryOfOrigin;
    List<Warehouse> warehouses;
}
