package com.example.application.data.entity;

import javax.persistence.Entity;

import com.example.application.data.AbstractEntity;

@Entity
public class Clerk extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
