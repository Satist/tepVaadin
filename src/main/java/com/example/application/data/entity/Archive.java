package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.*;

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
    @ManyToOne
    @JoinColumn(name = "exams_id")
    private Exams exams;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "patient_to_drugs",joinColumns = @JoinColumn(name = "archive_id"),inverseJoinColumns = @JoinColumn(name = "drug_id"))
    private Set<Drug> drugs= new HashSet<>();
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

    public void setExams(Exams exams) {
        this.exams = exams;
    }

    public Exams getExams() {
        return exams;
    }

    public void setDrugs(Set<Drug> drugs) {
        this.drugs = drugs;
    }

    public Set<Drug> getDrugs() {
        return drugs;
    }

    public String getDrugsNames(){
        StringBuilder names = new StringBuilder();
        for (Drug drug : drugs) {
            names.append(drug.getName());
            names.append(" ");
        }
        return names.toString();
    }
}
