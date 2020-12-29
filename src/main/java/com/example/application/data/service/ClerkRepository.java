package com.example.application.data.service;

import com.example.application.data.entity.Clerk;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClerkRepository extends JpaRepository<Clerk, Integer> {

}