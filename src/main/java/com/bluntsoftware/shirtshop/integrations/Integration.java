package com.bluntsoftware.shirtshop.integrations;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Integration {
    @Id
    private String id;
    private String tenant;
    private String realmId;
    private Date issued;
    private Date expires;
    private Map<String,Object> credentials;
    private byte[] storedCredential;
}
