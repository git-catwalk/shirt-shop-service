package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class ManufacturerTest {

  @Test
  void shouldCreateManufacturer(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Manufacturer.class));
  }

  @Test
  void shouldBuildManufacturer(){
    Assertions.assertNotNull(Manufacturer.builder().build());
  }
}
