package org.example.healthappbackendjava.service;

import org.example.healthappbackendjava.config.CacheNames;
import org.example.healthappbackendjava.dto.PrescriptionDto;
import org.example.healthappbackendjava.entity.*;
import org.example.healthappbackendjava.repository.AppointmentRepository;
import org.example.healthappbackendjava.repository.PrescriptionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepo;
    private final AppointmentRepository appointmentRepo;

    public PrescriptionService(PrescriptionRepository prescriptionRepo, AppointmentRepository appointmentRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.appointmentRepo = appointmentRepo;
    }
    @Caching(evict = {@CacheEvict(value = CacheNames.PRESCRIPTIONs, allEntries = true),
        @CacheEvict(value = CacheNames.PRESCRIPTIONs, key = "#appointmentId")})
    public PrescriptionDto createPrescription(int appointmentId, PrescriptionDto dto) {
        Appointments appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDoctor(appointment.getDoctor());
        prescription.setUser(appointment.getUser());
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setNotes(dto.getNotes());

        if (dto.getMedicines() != null) {
            List<PrescriptionMedicine> medicines = dto.getMedicines().stream().map(m -> {
                PrescriptionMedicine med = new PrescriptionMedicine();
                med.setName(m.getName());
                med.setDosage(m.getDosage());
                med.setDuration(m.getDuration());
                med.setInstructions(m.getInstructions());
                med.setPrescription(prescription);
                return med;
            }).collect(Collectors.toList());
            prescription.setMedicines(medicines);
        }

        prescriptionRepo.save(prescription);
        return toDto(prescription);
    }
    @Cacheable(value = CacheNames.PRESCRIPTIONs, key = "#userId")
    public List<PrescriptionDto> getUserPrescriptions(int userId) {
        return prescriptionRepo.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @Cacheable(value = CacheNames.PRESCRIPTIONs, key = "#doctorId")
    public List<PrescriptionDto> getDoctorPrescriptions(int doctorId) {
        return prescriptionRepo.findByDoctorId(doctorId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PrescriptionDto toDto(Prescription p) {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setId(p.getId());
        dto.setAppointmentId(p.getAppointment().getId());
        dto.setDiagnosis(p.getDiagnosis());
        dto.setNotes(p.getNotes());
        dto.setCreatedAt(p.getCreatedAt());

        if (p.getDoctor() != null) {
            dto.setDoctorId(p.getDoctor().getId());
            dto.setDoctorName(p.getDoctor().getName());
            dto.setDoctorSpecialization(p.getDoctor().getSpecialization());
        }
        if (p.getUser() != null) {
            dto.setUserId(p.getUser().getId());
        }
        if (p.getMedicines() != null) {
            dto.setMedicines(p.getMedicines().stream().map(m -> {
                PrescriptionDto.MedicineDto med = new PrescriptionDto.MedicineDto();
                med.setName(m.getName());
                med.setDosage(m.getDosage());
                med.setDuration(m.getDuration());
                med.setInstructions(m.getInstructions());
                return med;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
