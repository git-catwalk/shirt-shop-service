package com.bluntsoftware.shirtshop.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class ExpensesTest {

  @Test
  void shouldCreateExpenses(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Expenses.class));
  }

  @Test
  void shouldBuildExpenses(){
    Assertions.assertNotNull(Expenses.builder().build());
  }
}
