package com.example.application.data.service;

import com.example.application.data.entity.Diseases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class DiseaseService extends CrudService<Diseases, Integer> {

    private DiseaseRepository repository;

    public DiseaseService(@Autowired DiseaseRepository repository) {
        this.repository = repository;
    }

    @Override
    protected DiseaseRepository getRepository() {
        return repository;
    }

}