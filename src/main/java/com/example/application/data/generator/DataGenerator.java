package com.example.application.data.generator;

import com.example.application.data.entity.Clerk;
import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Patient;
import com.example.application.data.service.ClerkRepository;
import com.example.application.data.service.DoctorRepository;
import com.example.application.data.service.PatientRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(DoctorRepository doctorRepository, ClerkRepository clerkRepository,
            PatientRepository patientRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (doctorRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Doctor entities...");
            ExampleDataGenerator<Doctor> doctorRepositoryGenerator = new ExampleDataGenerator<>(Doctor.class);
            doctorRepositoryGenerator.setData(Doctor::setId, DataType.ID);
            doctorRepositoryGenerator.setData(Doctor::setId, DataType.NUMBER_UP_TO_100);
            doctorRepositoryGenerator.setData(Doctor::setName, DataType.FULL_NAME);
            doctorRepositoryGenerator.setData(Doctor::setSpecialty, DataType.WORD);
            doctorRepository.saveAll(doctorRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Clerk entities...");
            ExampleDataGenerator<Clerk> clerkRepositoryGenerator = new ExampleDataGenerator<>(Clerk.class);
            clerkRepositoryGenerator.setData(Clerk::setId, DataType.ID);
            clerkRepositoryGenerator.setData(Clerk::setId, DataType.NUMBER_UP_TO_100);
            clerkRepositoryGenerator.setData(Clerk::setName, DataType.FULL_NAME);
            clerkRepository.saveAll(clerkRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Patient entities...");
            ExampleDataGenerator<Patient> patientRepositoryGenerator = new ExampleDataGenerator<>(Patient.class);
            patientRepositoryGenerator.setData(Patient::setId, DataType.ID);
            patientRepositoryGenerator.setData(Patient::setId, DataType.NUMBER_UP_TO_100);
            patientRepositoryGenerator.setData(Patient::setName, DataType.FULL_NAME);
            patientRepositoryGenerator.setData(Patient::setAddress, DataType.ADDRESS);
            patientRepositoryGenerator.setData(Patient::setInsurance, DataType.WORD);
            patientRepository.saveAll(patientRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}