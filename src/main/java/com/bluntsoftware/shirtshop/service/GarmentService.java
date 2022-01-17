package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.repository.GarmentRepo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class GarmentService{

  private final GarmentRepo repo;

  public GarmentService(GarmentRepo repo) {
    this.repo = repo;
  }

  public  Garment save(Garment item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Garment> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Garment> findAll() {
    return repo.findAll();
  }

  public Page<Garment> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAllByStyleNumberIgnoreCaseContainingOrBrandIgnoreCaseContainingOrDescriptionIgnoreCaseContaining
            (term,term,term,pageable);
  }
  /*
  New,Catalog Page #,Style #,Brand,Style Description,Colors,Size,Case Pack, Case Price
   */
  public void importSSCsv(InputStream is){
    this.repo.deleteAll();
    try (var fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
         var csvParser = new CSVParser(fileReader,
                 CSVFormat.Builder.create()
                         .setHeader().setSkipHeaderRecord(true)
                         .setIgnoreHeaderCase(false)
                         .setTrim(true)
                         .build())) {

      csvParser.getRecords().forEach(r-> {

        Garment garment = Garment.builder()
                .id(r.get("Style #"))
                .costDate(new Date())
                .catalogPageNumber(r.get("Catalog Page #"))
                .styleNumber(r.get("Style #"))
                .brand(r.get("Brand"))
                .description(r.get("Style Description"))
                .color(r.get("Colors"))
                .size(r.get("Size"))
                .casePack(r.get("Case Pack"))
                .cost(BigDecimal.valueOf(Double.parseDouble(r.get("Case Price").replace("$",""))))
                .build();
                updateGarment(garment);
        }
      );
    } catch (Exception e) {
      throw new RuntimeException("Could not parse the csv file: " + e.getMessage());
    }
  }

  public void updateGarment(Garment garment){
    Garment current = repo.findById(garment.getId()).orElse(garment);
    current.addCost(garment.getSize(),garment.getColor(),garment.getCost());
    save(current);
  }

}
