package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.PriceProfile;
import com.bluntsoftware.shirtshop.repository.PriceProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PriceProfileService {
    private final PriceProfileRepo repo;

    public PriceProfileService(PriceProfileRepo repo) {
        this.repo = repo;
    }

    PriceProfile buildDefaultProfile(){

        Map<String, Map<String, BigDecimal>> matrix = new HashMap<>();
        matrix.put("Screen Printing",new HashMap<>());
        matrix.get("Screen Printing").put("1 Color",BigDecimal.valueOf(2.00));
        matrix.get("Screen Printing").put("2 Colors",BigDecimal.valueOf(3.00));
        matrix.get("Screen Printing").put("3 Colors",BigDecimal.valueOf(4.00));
        matrix.get("Screen Printing").put("4 Colors",BigDecimal.valueOf(5.00));
        matrix.get("Screen Printing").put("5 Colors",BigDecimal.valueOf(6.00));
        matrix.get("Screen Printing").put("6 Colors",BigDecimal.valueOf(7.00));
        matrix.get("Screen Printing").put("7 Colors",BigDecimal.valueOf(8.00));

        matrix.put("Embroidery",new HashMap<>());
        matrix.get("Embroidery").put("3000 Stitches",BigDecimal.valueOf(2.00));
        matrix.get("Embroidery").put("6000 Stitches",BigDecimal.valueOf(3.00));
        matrix.get("Embroidery").put("12,000 Stitches",BigDecimal.valueOf(4.00));

        matrix.put("Toner",new HashMap<>());
        matrix.get("Toner").put("1 Color",BigDecimal.valueOf(2.00));
        matrix.get("Toner").put("2 Colors",BigDecimal.valueOf(3.00));
        matrix.get("Toner").put("3 Colors",BigDecimal.valueOf(4.00));
        matrix.get("Toner").put("4 Colors",BigDecimal.valueOf(5.00));

        return PriceProfile.builder()
                .id("3324-Se5fwG-4234-fdfs")
                .garmentMarkup(100.0)
                .name("default")
                .description("Default profile used for all customers that do not have a pricing profile selected")
                .matrix(matrix)
                .build();
    }

    public PriceProfile save(PriceProfile item) {
        return repo.save(item);
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }

    public Optional<PriceProfile> findById(String id) {
        return repo.findById(id);
    }

    public Iterable<PriceProfile> findAll() {
        return repo.findAll();
    }

    public Page<PriceProfile> search(String term, Pageable pageable) {
        if(repo.count() < 1){
            this.repo.save(buildDefaultProfile());
        }
        log.info("create a filter in repo for search term {}",term);
        return repo.findAll(pageable);
    }
}
