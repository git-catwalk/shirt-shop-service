package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class PrintLocationTest {

  @Test
  void shouldCreatePrintLocation(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(PrintLocation.class));
  }

  @Test
  void shouldBuildPrintLocation(){
    Assertions.assertNotNull(PrintLocation.builder().build());
  }
}
