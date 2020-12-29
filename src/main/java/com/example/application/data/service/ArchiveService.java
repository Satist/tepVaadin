package com.example.application.data.service;

import com.example.application.data.entity.Archive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class ArchiveService extends CrudService<Archive, Integer> {

    private ArchiveRepository repository;

    public ArchiveService(@Autowired ArchiveRepository repository) {
        this.repository = repository;
    }

    @Override
    protected ArchiveRepository getRepository() {
        return repository;
    }

}
