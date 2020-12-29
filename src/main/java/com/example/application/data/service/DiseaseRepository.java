package com.example.application.data.service;

import com.example.application.data.entity.Diseases;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Diseases, Integer> {

}