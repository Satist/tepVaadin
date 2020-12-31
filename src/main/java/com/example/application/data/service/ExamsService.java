package com.example.application.data.service;


import com.example.application.data.entity.Exams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

@Service

public class ExamsService extends CrudService<Exams, Integer> {
    private ExamRepository repository;

    public ExamsService(@Autowired ExamRepository repository) {
        this.repository = repository;
    }

    @Override
    protected ExamRepository getRepository() {
        return repository;
    }

    public Collection<Exams> getAll(){
        return repository.findAll();
    }
}
