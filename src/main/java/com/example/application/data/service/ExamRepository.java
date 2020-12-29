package com.example.application.data.service;

import com.example.application.data.entity.Exams;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exams, Integer> {

}