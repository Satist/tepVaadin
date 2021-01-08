package com.example.application.data.service;

import com.example.application.data.entity.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

@Service
public class DoctorService extends CrudService<Doctor, Integer> {

    private DoctorRepository repository;

    public DoctorService(@Autowired DoctorRepository repository) {
        this.repository = repository;
    }

    @Override
    protected DoctorRepository getRepository() {
        return repository;
    }

    public Collection<Doctor> getAll(){
        return repository.findAll();
    }
}
