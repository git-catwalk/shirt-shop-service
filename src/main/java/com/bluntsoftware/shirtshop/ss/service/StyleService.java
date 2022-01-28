package com.bluntsoftware.shirtshop.ss.service;

import com.bluntsoftware.shirtshop.ss.model.Style;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StyleService {

    public Optional<Style> findById(String id) {
        return null;
    }

    public Iterable<Style> findAll() {
        return null;
    }

    public Page<Style> search(String term, Pageable pageable) {
        return null;
    }

    public List<Style> importSSCsv(InputStream is){
       List<Style> styles = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.Builder.create()
                             .setHeader().setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(false)
                             .setTrim(true)
                             .build())) {
            csvParser.getRecords().forEach(r-> {
                Style style = Style.builder()
                            .reseller("S&SActiveWear")
                            .styleId(r.get("styleID"))
                            .partNumber(r.get("partNumber"))
                            .brandName(r.get("brandName"))
                            .styleName(r.get("styleName"))
                            .title(r.get("title"))
                            .description(r.get("description"))
                            .baseCategory(r.get("baseCategory"))
                            .categories(r.get("categories"))
                            .catalogPageNumber(r.get("catalogPageNumber"))
                            .newStyle(Boolean.parseBoolean(r.get("newStyle")))
                            .comparableGroup(Integer.parseInt(r.get("comparableGroup")))
                            .companionGroup(Integer.parseInt(r.get("companionGroup")))
                            .brandImage(r.get("brandImage"))
                            .styleImage(r.get("styleImage"))
                            .build();
                styles.add(style);
           });
        } catch (Exception e) {
            throw new RuntimeException("Could not parse the csv file: " + e.getMessage());
        }
        return styles;
    }

    public List<Style> getStyles(){
        RestTemplate restTemplate = new RestTemplate();
        String customerAPIUrl =  "https://api.ssactivewear.com/v2/styles/";
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(customerAPIUrl)
                .queryParam("mediaType","json")
                .build();
        HttpEntity<List<Style>> request = new HttpEntity<>(null,createHeaders("457322","d4477011-4e8c-42ba-b4e9-022ee074afe1"));
        ResponseEntity<Style[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Style[].class );
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
