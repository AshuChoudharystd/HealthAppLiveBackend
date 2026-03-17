package org.example.healthappbackendjava.dto;

import org.example.healthappbackendjava.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public Doctor dtoToDoctor(DoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setEmail(dto.getEmail());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setPassword(dto.getPassword());
        doctor.setGender(dto.getGender());
        doctor.setProfilePicture(dto.getProfilePicture());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setAppointmentSlot(dto.getAppointmentSlot());
        doctor.setRole(dto.getRole());
        return doctor;
    }

    public DoctorDto doctorToDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setEmail(doctor.getEmail());
        dto.setPhoneNumber(doctor.getPhoneNumber());
//        dto.setPassword(doctor.getPassword());
        dto.setRole(doctor.getRole());
        dto.setGender(doctor.getGender());
        dto.setCreatedAt(doctor.getCreatedAt());
        dto.setProfilePicture(doctor.getProfilePicture());
        dto.setRating(doctor.getRating());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setAppointments(doctor.getAppointments());
        dto.setAppointmentSlot(doctor.getAppointmentSlot());
        return dto;
    }
}
