package com.example.application.data.service;

import com.example.application.data.entity.Diseases;

import com.example.application.data.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

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

    public Collection<Diseases> getAll(){
        return repository.findAll();
    }
}