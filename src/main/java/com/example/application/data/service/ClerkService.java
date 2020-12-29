package com.example.application.data.service;

import com.example.application.data.entity.Clerk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class ClerkService extends CrudService<Clerk, Integer> {

    private ClerkRepository repository;

    public ClerkService(@Autowired ClerkRepository repository) {
        this.repository = repository;
    }

    @Override
    protected ClerkRepository getRepository() {
        return repository;
    }

}
