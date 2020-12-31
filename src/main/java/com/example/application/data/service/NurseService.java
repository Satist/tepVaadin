package com.example.application.data.service;
import com.example.application.data.entity.Nurse;

import com.example.application.data.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

@Service
public class NurseService extends CrudService<Nurse, Integer>{
    private NurseRepository repository;

    public NurseService(@Autowired NurseRepository repository) {
        this.repository = repository;
    }
    @Override
    protected NurseRepository getRepository() {
        return repository;
    }

    public Collection<Nurse> getAll(){
        return repository.findAll();
    }
}
