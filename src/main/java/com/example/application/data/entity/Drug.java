package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class Drug extends AbstractEntity {
    private String name;
    private String type;
    private int mg;
    private String disease;

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setDisease(String disease) { this.disease = disease; }
    public String getDisease() { return disease; }
    public void setMg(int mg) { this.mg = mg; }
    public void setType(String type) { this.type = type; }
    public int getMg() { return mg; }
    public String getType() { return type; }
}
