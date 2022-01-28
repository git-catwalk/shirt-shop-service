package com.bluntsoftware.shirtshop.ss.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Style {
    @Id
    String styleId;
    String reseller;
    String partNumber;
    String brandName;
    String styleName;
    String title;
    String description;
    String baseCategory;
    String categories;
    String catalogPageNumber;
    Boolean newStyle;
    Integer comparableGroup;
    Integer companionGroup;
    String brandImage;
    String styleImage;
}
