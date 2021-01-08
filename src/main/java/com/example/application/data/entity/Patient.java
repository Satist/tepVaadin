package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Patient extends AbstractEntity {
    private Integer amka;
    private String name;
    private String address;
    private String insurance;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
    private Set<Archive> archives;

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

    public Set<Archive> getArchives() {
        return archives;
    }

    public void setArchives(Set<Archive> archives) {
        this.archives = archives;
    }
}
