package com.bluntsoftware.shirtshop.service;


import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.repository.GarmentRepo;
import org.springframework.data.domain.Example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GarmentService {

    private final GarmentRepo garmentRepo;

    public GarmentService(GarmentRepo garmentRepo) {
        this.garmentRepo = garmentRepo;
    }

    public List<Garment> findByStyleId(String styleId){
        return garmentRepo.findAllByStyleId(styleId);
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
}
