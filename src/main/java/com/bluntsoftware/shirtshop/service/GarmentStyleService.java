package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.model.GarmentStyle;
import com.bluntsoftware.shirtshop.model.GarmentColor;

import com.bluntsoftware.shirtshop.repository.GarmentStyleRepo;
import com.bluntsoftware.shirtshop.integrations.types.ss_active.service.ProductService;
import com.bluntsoftware.shirtshop.integrations.types.ss_active.service.StyleService;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GarmentStyleService {

  private final ProductService productService;
  private final GarmentStyleRepo repo;
  private final StyleService styleService;

  public GarmentStyleService(ProductService productService, GarmentStyleRepo repo, StyleService styleService) {
    this.productService = productService;
    this.repo = repo;
    this.styleService = styleService;
  }

  public GarmentStyle save(GarmentStyle item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<GarmentStyle> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<GarmentStyle> findAll() {
    return repo.findAll();
  }

  public Page<GarmentStyle> fullTextSearch(String searchPhrase, Pageable pageable) {
    Sort sort = Sort.by("score");
    TextCriteria criteria = TextCriteria.forDefaultLanguage()
            .caseSensitive(false)
            .matchingAny(searchPhrase.split(" "));
    return repo.findAllBy(criteria, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
  }

  //poor man's full text search
  public Page<GarmentStyle> search(String term, Pageable pageable) {
    String[] terms = term.split(" ");

    if(terms.length > 1){
      List<GarmentStyle> list = repo.findAllByBrandNameIgnoreCaseContainingAndTitleIgnoreCaseContaining(terms[0],terms[1]);
      for(int x = 2;x < terms.length;x++){
        String te = terms[x];
        list = list.stream()
                .filter((t)-> t.getTitle().toLowerCase(Locale.ROOT).contains(te.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
      }
      return new PageImpl<>(list);
    }

    Page<GarmentStyle> ret = repo.findAllByStyleNameIgnoreCaseContainingOrTitleIgnoreCaseContainingOrBrandNameIgnoreCaseContaining
            (term.trim(),term.trim(),term.trim(),pageable);

    return ret;
  }

  public void deleteAll(){
    this.repo.deleteAll();
  }

  public List<GarmentColor> findColors(String styleId) {
    return this.productService.findColors(styleId);
  }

  public List<Garment> findGarments(String styleId, String colorId) {
    return this.productService.findGarments(styleId,colorId);
  }

  public void importSSCsv(InputStream inputStream) {
    this.styleService.importSSCsv(inputStream).forEach((s)->{
      GarmentStyle current = repo.findByStyleIdAndReseller(s.getStyleId(),"S&SActiveWear");
      GarmentStyle gs = GarmentStyle.builder()
              .reseller("S&SActiveWear")
              .brandImage(s.getBrandImage())
              .styleName(s.getStyleName())
              .styleId(s.getStyleId())
              .description(s.getDescription())
              .title(s.getTitle())
              .partNumber(s.getPartNumber())
              .brandName(s.getBrandName())
              .styleImage(s.getStyleImage())
              .brandImage(s.getBrandImage())
              .build();
      if(current != null){
        gs.setId(current.getId());
      }
      repo.save(gs);
    });
  }
}
