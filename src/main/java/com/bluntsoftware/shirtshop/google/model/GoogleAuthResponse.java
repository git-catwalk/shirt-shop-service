package com.bluntsoftware.shirtshop.google.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleAuthResponse {
    private String code;
    private String redirectUrl;
}
