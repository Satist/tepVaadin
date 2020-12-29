package com.example.application.data.service;

import com.example.application.data.entity.Archive;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Integer> {

}