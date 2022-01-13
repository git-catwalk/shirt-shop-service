package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class EstimateTest {

  @Test
  void shouldCreateEstimate(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Estimate.class));
  }

  @Test
  void shouldBuildEstimate(){
    Assertions.assertNotNull(Estimate.builder().build());
  }
}
