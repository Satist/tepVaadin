package com.example.application.data.service;

import com.example.application.data.entity.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;


@Service
public class ShiftService extends CrudService<Shift, Integer> {

    private ShiftRepository repository;
    public ShiftService(@Autowired ShiftRepository repository) {
        this.repository = repository;
    }

    @Override
    protected ShiftRepository getRepository() {
        return repository;
    }

    public Collection<Shift> getAll(){
        return repository.findAll();
    }
}
