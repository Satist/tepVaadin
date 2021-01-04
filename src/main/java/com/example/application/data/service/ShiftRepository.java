package com.example.application.data.service;

import com.example.application.data.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift,Integer> {
}
