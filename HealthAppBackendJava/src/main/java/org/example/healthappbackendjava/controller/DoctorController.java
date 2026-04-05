package org.example.healthappbackendjava.controller;

import org.example.healthappbackendjava.dto.DoctorDto;
import org.example.healthappbackendjava.dto.PrescriptionDto;
import org.example.healthappbackendjava.dto.UserDto;
import org.example.healthappbackendjava.entity.Appointments;
import org.example.healthappbackendjava.service.DoctorService;
import org.example.healthappbackendjava.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;

    public DoctorController(DoctorService doctorService, PrescriptionService prescriptionService) {
        this.doctorService = doctorService;
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@Valid @RequestBody DoctorDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctor(@PathVariable int id) {
        return ResponseEntity.ok(doctorService.getDoctor(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable int id, @RequestBody DoctorDto dto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }

    @GetMapping("/{docId}/appointments")
    public ResponseEntity<List<Appointments>> getDoctorAppointments(@PathVariable int docId) {
        return ResponseEntity.ok(doctorService.getDoctorAppointments(docId));
    }

    @GetMapping("/appointments/{appId}")
    public ResponseEntity<Appointments> getDoctorAppointment(@PathVariable int appId) {
        return ResponseEntity.ok(doctorService.getDoctorAppointment(appId));
    }

    @PatchMapping("/appointments/{appId}/cancel")
    public ResponseEntity<Appointments> cancelAppointment(@PathVariable int appId) {
        return ResponseEntity.ok(doctorService.cancelAppointment(appId));
    }

    @PostMapping("/appointments/{appId}/prescription")
    public ResponseEntity<PrescriptionDto> createPrescription(@PathVariable int appId, @RequestBody PrescriptionDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.createPrescription(appId, dto));
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDto>> getDoctorPrescriptions(@RequestParam int doctorId) {
        return ResponseEntity.ok(prescriptionService.getDoctorPrescriptions(doctorId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable int userId) {
        return ResponseEntity.ok(doctorService.getUserProfile(userId));
    }
}
