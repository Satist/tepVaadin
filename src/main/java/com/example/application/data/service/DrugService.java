package com.example.application.data.service;

import com.example.application.data.entity.Drug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

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

}
