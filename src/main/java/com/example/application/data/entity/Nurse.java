package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Nurse extends AbstractEntity {
    private String name;
    @ManyToMany(mappedBy = "nurses",fetch = FetchType.EAGER)
    private Set<Shift> shifts= new HashSet<>();
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }

    public Set<Shift> getShifts() {
        return shifts;
    }
}
