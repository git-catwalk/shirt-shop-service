package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class NamesNumbersTest {

  @Test
  void shouldCreateNamesNumbers(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(NamesNumbers.class));
  }

  @Test
  void shouldBuildNamesNumbers(){
    Assertions.assertNotNull(NamesNumbers.builder().build());
  }
}
