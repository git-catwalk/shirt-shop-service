package com.bluntsoftware.shirtshop.integrations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntegrationDto {
    private String id;
    private String tenant;
    private String realmId;
    private Date issued;
    private Date expires;
}
