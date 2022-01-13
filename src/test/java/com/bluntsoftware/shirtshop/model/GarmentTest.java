package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class GarmentTest {

  @Test
  void shouldCreateGarment(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Garment.class));
  }

  @Test
  void shouldBuildGarment(){
    Assertions.assertNotNull(Garment.builder().build());
  }
}
