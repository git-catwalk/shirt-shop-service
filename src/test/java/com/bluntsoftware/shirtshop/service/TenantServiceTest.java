package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.repository.TenantRepo;
import com.bluntsoftware.shirtshop.model.Tenant;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ExtendWith(SpringExtension.class)
@Scope("test")
class TenantServiceTest {

  @MockBean
  private TenantRepo repo;

  Tenant item1;
  Tenant item2;
  TenantService service;

  @BeforeEach
  void before(){
    EasyRandom generator = new EasyRandom();
    item1 = generator.nextObject(Tenant.class);
    item2 = generator.nextObject(Tenant.class);
    service = new TenantService(this.repo);
  }

  @Test
  void shouldSave(){
    Mockito.when(repo.save(Mockito.any(Tenant.class))).thenReturn(item1);
    Tenant data = service.save(Tenant.builder().build());
    Assertions.assertEquals(data,item1);
  }

  @Test
  void findById(){
    Mockito.when(repo.findById(Mockito.any(String.class))).thenReturn(Optional.of(item1));
    Assertions.assertNotNull(service.findById(String.valueOf("1")));
  }

  @Test
  void deleteById(){
    Assertions.assertAll(() -> service.deleteById(String.valueOf("1")));
  }

  @Test
  void findAll(){
    Mockito.when(repo.findAll()).thenReturn(Arrays.asList(item1,item2));
    List<Tenant> all = StreamSupport.stream(service.findAll().spliterator(), false).collect(Collectors.toList());
    Assertions.assertEquals(2, all.size());
  }

  @Test
  void search(){
    PageRequest pageRequest = PageRequest.of(1,2);
    Page<Tenant> page = new PageImpl<>(Arrays.asList(item1,item2));
    Mockito.when(repo.findAll(pageRequest)).thenReturn(page);
    List<Tenant> all = new ArrayList<>(service.search(null, pageRequest).getContent());
    Assertions.assertEquals(2, all.size());
  }

}
