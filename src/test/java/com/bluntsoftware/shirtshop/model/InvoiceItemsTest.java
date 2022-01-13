package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class InvoiceItemsTest {

  @Test
  void shouldCreateInvoiceItems(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(InvoiceItems.class));
  }

  @Test
  void shouldBuildInvoiceItems(){
    Assertions.assertNotNull(InvoiceItems.builder().build());
  }
}
