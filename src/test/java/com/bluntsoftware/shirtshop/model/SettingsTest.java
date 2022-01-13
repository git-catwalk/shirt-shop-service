package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class SettingsTest {

  @Test
  void shouldCreateSettings(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Settings.class));
  }

  @Test
  void shouldBuildSettings(){
    Assertions.assertNotNull(Settings.builder().build());
  }
}
