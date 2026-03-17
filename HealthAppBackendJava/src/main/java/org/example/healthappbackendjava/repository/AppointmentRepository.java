package org.example.healthappbackendjava.repository;

import org.example.healthappbackendjava.entity.Appointments;
import org.example.healthappbackendjava.entity.Doctor;
import org.example.healthappbackendjava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointments,Integer> {
    List<Appointments> findByUserId(int user_id);
    List<Appointments> findByDoctorId(int doctor_id);
}
