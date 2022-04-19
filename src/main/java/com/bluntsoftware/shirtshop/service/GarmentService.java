package com.bluntsoftware.shirtshop.service;


import com.bluntsoftware.shirtshop.integrations.types.ss_active.service.ProductService;
import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.model.GarmentColor;
import com.bluntsoftware.shirtshop.model.GarmentStyle;
import com.bluntsoftware.shirtshop.repository.GarmentRepo;
import com.bluntsoftware.shirtshop.repository.GarmentStyleRepo;
import org.springframework.data.domain.Example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GarmentService {
    private final ProductService productService;
    private final GarmentRepo garmentRepo;
    private final GarmentStyleRepo garmentStyleRepo;
    public GarmentService(ProductService productService, GarmentRepo garmentRepo, GarmentStyleRepo garmentStyleRepo) {
        this.productService = productService;
        this.garmentRepo = garmentRepo;
        this.garmentStyleRepo = garmentStyleRepo;
    }

    public Garment save(Garment garment) {
            return garmentRepo.save(garment);
    }

    public Optional<Garment> findById(String id) {
        return garmentRepo.findById(id);
    }

    public Iterable<Garment> findAll() {
            return garmentRepo.findAll();
    }

    public void deleteById(String id) {
        garmentRepo.deleteById(id);
    }

    public Page<Garment> search(String searchTerm, PageRequest of) {
        Example<Garment> exampleGarment =  Example.of(
                Garment.builder()
                        .styleId(searchTerm)
                        .build());

        return garmentRepo.findAll(exampleGarment,of);
    }

    public List<GarmentColor> findColors(String styleId) {
        return findGarmentsByStyleId(styleId).stream().map(g-> GarmentColor.builder()
                .colorId(g.getColorCode())
                .webColor(g.getWebColor())
                .name(g.getColorName())
                .colorFrontImage(g.getColorFrontImage())
                .build())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Garment> findGarmentsByStyleId(String styleId) {
        GarmentStyle gs = this.garmentStyleRepo.findByStyleId(styleId);
        String reseller = gs.getReseller();
        if(reseller != null && reseller.equalsIgnoreCase("S&SActiveWear")){
            //Call S&S Api
            return this.productService.findGarments(styleId);
        }
        return garmentRepo.findAllByStyleId(styleId);
    }

    public List<Garment> findGarmentsByStyleIdAndColorId(String styleId, String colorId) {
        return findGarmentsByStyleId(styleId).stream().filter((p)->p.getColorCode().equalsIgnoreCase(colorId)).collect(Collectors.toList());
    }
}
