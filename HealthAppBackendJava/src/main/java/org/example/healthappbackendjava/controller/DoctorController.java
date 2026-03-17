package org.example.healthappbackendjava.controller;

import org.example.healthappbackendjava.dto.DoctorDto;
import org.example.healthappbackendjava.dto.UserDto;
import org.example.healthappbackendjava.entity.Appointments;
import org.example.healthappbackendjava.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(dto));
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

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable int userId) {
        return ResponseEntity.ok(doctorService.getUserProfile(userId));
    }
}
