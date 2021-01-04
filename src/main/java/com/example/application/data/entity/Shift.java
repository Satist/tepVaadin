package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Shift extends AbstractEntity {
    private String date;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "shift_to_personnel",joinColumns = @JoinColumn(name = "shift_id"),inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    Set<Doctor> doctors=new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "shift_to_personnel",joinColumns = @JoinColumn(name = "shift_id"),inverseJoinColumns = @JoinColumn(name = "nurse_id"))
    Set<Nurse> nurses=new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "shift_to_personnel",joinColumns = @JoinColumn(name = "shift_id"),inverseJoinColumns = @JoinColumn(name = "clerk_id"))
    Set<Clerk> clerks=new HashSet<>();

    public void setClerks(Set<Clerk> clerks) {
        this.clerks = clerks;
    }

    public Set<Clerk> getClerks() {
        return clerks;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Set<Nurse> getNurses() {
        return nurses;
    }

    public void setNurses(Set<Nurse> nurses) {
        this.nurses = nurses;
    }

    public String getDate() {
        return date;
    }

    public String getDoctorsID(){
        StringBuilder names = new StringBuilder();
        for (Doctor doctor : doctors) {
            names.append(doctor.getId());
            names.append(" ");
        }
        return names.toString();
    }

    public String getNursesID(){
        StringBuilder names = new StringBuilder();
        for (Nurse nurse : nurses) {
            names.append(nurse.getId());
            names.append(" ");
        }
        return names.toString();
    }

    public String getClerksID(){
        StringBuilder names = new StringBuilder();
        for (Clerk clerk : clerks) {
            names.append(clerk.getId());
            names.append(" ");
        }
        return names.toString();
    }
}
