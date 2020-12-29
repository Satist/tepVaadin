package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.example.application.data.AbstractEntity;

import java.util.List;

@Entity
public class Patient extends AbstractEntity {
    private Integer amka;
    private String name;
    private String address;
    private String insurance;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private List<Archive> archives;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getInsurance() {
        return insurance;
    }
    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }
    public Integer getAmka() {
        return amka;
    }
    public void setAmka(Integer amka) {
        this.amka = amka;
    }

}
