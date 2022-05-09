package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Label;
import com.bluntsoftware.shirtshop.repository.LabelRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LabelService {
    private final LabelRepo repo;

    public LabelService(LabelRepo repo) {
        this.repo = repo;
    }

    public Label save(Label label) {
        return this.repo.save(label);
    }

    public Optional<Label> findById(String id) {
        return this.repo.findById(id);
    }

    public Iterable<Label> findAll() {
        return this.repo.findAll();
    }

    public void deleteById(String id) {
        this.repo.deleteById(id);
    }

    public Page<Label> search(String searchTerm, Pageable pageable) {
        return this.repo.findAllByNameIgnoreCaseContaining(searchTerm,pageable);
    }
}
