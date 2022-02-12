package com.bluntsoftware.shirtshop.integrations.wave.service;

import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import graphql.kickstart.spring.webclient.boot.GraphQLResponse;
import graphql.kickstart.spring.webclient.boot.GraphQLWebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WaveCustomerService {
    private final GraphQLWebClient graphQLWebClient;

    public WaveCustomerService(GraphQLWebClient graphQLWebClient) {
        this.graphQLWebClient = graphQLWebClient;
    }

    public Map<String,String> getBusinesses() {
        GraphQLRequest request = GraphQLRequest.builder().query("query {\n" +
                "  businesses(page: 1, pageSize: 10) {\n" +
                "    pageInfo {\n" +
                "      currentPage\n" +
                "      totalPages\n" +
                "      totalCount\n" +
                "    }\n" +
                "    edges {\n" +
                "      node {\n" +
                "        id\n" +
                "        name\n" +
                "        isClassicAccounting\n" +
                "        isClassicInvoicing\n" +
                "        isPersonal\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}").build();
        GraphQLResponse response = graphQLWebClient.post(request).block();
        System.out.println(response);
        return new HashMap<>();
    }
    private static HttpHeaders buildHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
