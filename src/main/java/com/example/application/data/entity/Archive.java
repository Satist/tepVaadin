package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Archive extends AbstractEntity {
    private String out_date;
    private String in_date;
    private String symptoms;
    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Diseases diseases;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    public String getIn_date() { return in_date; }
    public String getOut_date() { return out_date; }
    public void setIn_date(String in_date) { this.in_date = in_date; }
    public void setOut_date(String out_date) { this.out_date = out_date; }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getSymptoms() {
        return symptoms;
    }


    public void setDiseases(Diseases diseases) {
        this.diseases = diseases;
    }

    public Diseases getDiseases() {
        return diseases;
    }
}
