package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Exams extends AbstractEntity {
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exams",cascade = CascadeType.ALL)
    private List<Archive> archives;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
