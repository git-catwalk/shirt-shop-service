package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(value = CustomerController.class)
@Import(CustomerService.class)
@ExtendWith(SpringExtension.class)
@Scope("test")
class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  CustomerService service;

  Customer mono;
  List<Customer> flux;

  @BeforeEach
  void before() {
      EasyRandomParameters parameters = new EasyRandomParameters();
      parameters.setCollectionSizeRange(new EasyRandomParameters.Range<>(2,10));
      EasyRandom generator = new EasyRandom(parameters);

      mono = generator.nextObject(Customer.class);
      flux = Arrays.asList(generator.nextObject(Customer.class), generator.nextObject(Customer.class));
      Mockito.when(this.service.findAll()).thenReturn(flux);
      Mockito.when(this.service.save(any())).thenReturn(mono);
      Mockito.when(this.service.findById(any())).thenReturn(Optional.of(mono));
  }

  @Test
  void shouldFindById() throws Exception {
    String expected = objectMapper.writeValueAsString(mono);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
          .get("/rest/customer/1")
          .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    JSONAssert.assertEquals(expected, response.getContentAsString(), false);
  }

  @Test
  void shouldFindAll() throws Exception {
    String expected = objectMapper.writeValueAsString(flux);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
          .get("/rest/customer")
          .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    JSONAssert.assertEquals(expected, response.getContentAsString(), false);
  }

  @Test
  void shouldDeleteById() throws Exception {
      RequestBuilder requestBuilder = MockMvcRequestBuilders
          .delete("/rest/customer/1")
          .accept(MediaType.APPLICATION_JSON);
      MvcResult result = mockMvc.perform(requestBuilder).andReturn();
      MockHttpServletResponse response = result.getResponse();
      Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void shouldSave() throws Exception {
    String jsonBlob = objectMapper.writeValueAsString(mono);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
          .post("/rest/customer")
          .accept(MediaType.APPLICATION_JSON)
          .content(jsonBlob)
          .contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    JSONAssert.assertEquals(jsonBlob, result.getResponse().getContentAsString(), false);
  }

  @Test
  void shouldSearch() throws Exception {
      String expected = objectMapper.writeValueAsString(flux);
      RequestBuilder requestBuilder = MockMvcRequestBuilders
      .get("/rest/customer/search")
      .accept(MediaType.APPLICATION_JSON)
      .content(expected)
      .contentType(MediaType.APPLICATION_JSON);
      MvcResult result = mockMvc.perform(requestBuilder).andReturn();
      MockHttpServletResponse response = result.getResponse();
      Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
