package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Diseases extends AbstractEntity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diseases",cascade = CascadeType.ALL)
    private List<Archive> archives;
    private String name;
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
