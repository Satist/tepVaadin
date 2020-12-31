package com.example.application.data.service;

import com.example.application.data.entity.Drug;

import com.example.application.data.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

@Service
public class DrugService extends CrudService<Drug, Integer> {

    private DrugRepository repository;

    public DrugService(@Autowired DrugRepository repository) {
        this.repository = repository;
    }

    @Override
    protected DrugRepository getRepository() {
        return repository;
    }

    public Collection<Drug> getAll(){
        return repository.findAll();
    }
}
