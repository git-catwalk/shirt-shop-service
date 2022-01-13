package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class CustomerTest {

  @Test
  void shouldCreateCustomer(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Customer.class));
  }

  @Test
  void shouldBuildCustomer(){
    Assertions.assertNotNull(Customer.builder().build());
  }
}
