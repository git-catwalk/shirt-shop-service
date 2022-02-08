package com.bluntsoftware.shirtshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileItem {
    String id;
    String folderId;
    String name;
    String createdBy;
    String createdAt;
    String modifiedAt;
    String size;
    String type;
    String contents;
    String description;
}
