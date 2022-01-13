package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class TenantTest {

  @Test
  void shouldCreateTenant(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Tenant.class));
  }

  @Test
  void shouldBuildTenant(){
    Assertions.assertNotNull(Tenant.builder().build());
  }
}
