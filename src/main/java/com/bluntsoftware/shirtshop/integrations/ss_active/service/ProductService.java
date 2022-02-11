package com.bluntsoftware.shirtshop.integrations.ss_active.service;

import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.model.GarmentColor;
import com.bluntsoftware.shirtshop.model.GarmentVendorApi;
import com.bluntsoftware.shirtshop.service.GarmentVendorApiService;
import com.bluntsoftware.shirtshop.integrations.ss_active.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {
    private final GarmentVendorApiService garmentVendorApiService;

    public ProductService(GarmentVendorApiService garmentVendorApiService) {
        this.garmentVendorApiService = garmentVendorApiService;
    }

    public List<GarmentColor> findColors(String styleId) {
        return  getProductsByStyle(styleId)
                .stream()
                .map((p)-> GarmentColor
                        .builder()
                        .colorId(p.getColorCode())
                        .webColor(p.getColor1())
                        .name(p.getColorName())
                        .colorFrontImage(p.getColorFrontImage())
                        .build())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Garment> findGarments(String styleId, final String colorId) {
        return getProductsByStyle(styleId)
                .stream()
                .filter((p)->p.getColorCode().equalsIgnoreCase(colorId))
                .map((p)-> Garment
                        .builder()
                        .qty(p.getQty())
                        .colorName(p.getColorName())
                        .colorCode(p.getColorCode())
                        .brandName(p.getBrandName())
                        .styleId(p.getStyleId())
                        .salePrice(p.getSalePrice())
                        .customerPrice(p.getCustomerPrice())
                        .casePrice(p.getCasePrice())
                        .dozenPrice(p.getDozenPrice())
                        .sku(p.getSku())
                        .gtin(p.getGtin())
                        .warehouses(p.getWarehouses())
                        .sizeName(p.getSizeName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Product> getProductsByStyle(String styleId){
        GarmentVendorApi garmentVendorApi = garmentVendorApiService.get().get();

        RestTemplate restTemplate = new RestTemplate();
        String customerAPIUrl =  "https://api.ssactivewear.com/v2/products/";
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(customerAPIUrl)
                .queryParam("styleId",styleId)
                .build();

        HttpEntity<List<Product>> request = new HttpEntity<>(null,createHeaders(garmentVendorApi.getSandsCustomerKey(),garmentVendorApi.getSandsApiKey()));
        ResponseEntity<Product[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Product[].class );
        return Arrays.asList(response.getBody());
    }

    HttpHeaders createHeaders(String username, String password){

        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }

}
