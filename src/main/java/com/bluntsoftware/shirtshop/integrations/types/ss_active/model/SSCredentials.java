package com.bluntsoftware.shirtshop.integrations.types.ss_active.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSCredentials {
    String apiKey;
    String customerNumber;
}
