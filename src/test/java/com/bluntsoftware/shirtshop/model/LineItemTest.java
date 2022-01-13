package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class LineItemTest {

  @Test
  void shouldCreateLineItem(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(LineItem.class));
  }

  @Test
  void shouldBuildLineItem(){
    Assertions.assertNotNull(LineItem.builder().build());
  }
}
