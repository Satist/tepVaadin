package com.example.application.data.service;

import com.example.application.data.entity.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class PatientService extends CrudService<Patient, Integer> {

    private PatientRepository repository;

    public PatientService(@Autowired PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    protected PatientRepository getRepository() {
        return repository;
    }

}
