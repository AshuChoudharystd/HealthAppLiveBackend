package org.example.healthappbackendjava.repository;

import org.example.healthappbackendjava.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByUserId(int userId);
    List<Prescription> findByDoctorId(int doctorId);
    Optional<Prescription> findByAppointmentId(int appointmentId);
}
