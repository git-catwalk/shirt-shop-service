package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class ScreenTest {

  @Test
  void shouldCreateScreen(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Screen.class));
  }

  @Test
  void shouldBuildScreen(){
    Assertions.assertNotNull(Screen.builder().build());
  }
}
