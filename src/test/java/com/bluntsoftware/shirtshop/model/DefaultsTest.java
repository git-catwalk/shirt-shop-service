package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class DefaultsTest {

  @Test
  void shouldCreateDefaults(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Defaults.class));
  }

  @Test
  void shouldBuildDefaults(){
    Assertions.assertNotNull(Defaults.builder().build());
  }
}
