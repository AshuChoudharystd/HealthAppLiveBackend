package org.example.healthappbackendjava.controller;

import org.example.healthappbackendjava.dto.DoctorDto;
import org.example.healthappbackendjava.dto.PrescriptionDto;
import org.example.healthappbackendjava.dto.UserDto;
import org.example.healthappbackendjava.entity.Appointments;
import org.example.healthappbackendjava.entity.Doctor;
import org.example.healthappbackendjava.service.PrescriptionService;
import org.example.healthappbackendjava.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PrescriptionService prescriptionService;

    public UserController(UserService userService, PrescriptionService prescriptionService) {
        this.userService = userService;
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int id, @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @PutMapping(value = "/{id}/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateProfilePicture(
            @PathVariable int id,
            @RequestParam("profilePicture") MultipartFile file) {

        return ResponseEntity.ok(
                userService.updateProfilePicture(id, file)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PostMapping("/{userId}/appointments/{doctorId}")
    public ResponseEntity<Appointments> bookAppointment(@PathVariable int userId, @PathVariable int doctorId, @RequestBody Appointments appointment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.bookAppointment(userId, doctorId, appointment));
    }

    @GetMapping("/{userId}/appointments")
    public ResponseEntity<List<Appointments>> getAllAppointments(@PathVariable int userId) {
        return ResponseEntity.ok(userService.getAllAppointments(userId));
    }

    @GetMapping("/appointments/{appId}")
    public ResponseEntity<Appointments> getAppointment(@PathVariable int appId) {
        return ResponseEntity.ok(userService.getAppointment(appId));
    }

    @PatchMapping("/appointments/{appId}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable int appId) {
        return ResponseEntity.ok(userService.cancelAppointment(appId));
    }

    @PutMapping("/appointments/{appId}/reschedule")
    public ResponseEntity<Appointments> rescheduleAppointment(@PathVariable int appId, @RequestBody Appointments newAppointmentData) {
        return ResponseEntity.ok(userService.rescheduleAppointment(appId, newAppointmentData));
    }

    @PatchMapping("/appointments/{appId}/finish")
    public ResponseEntity<Appointments> appointmentFinished(@PathVariable int appId) {
        return ResponseEntity.ok(userService.appointmentFinished(appId));
    }

    @GetMapping("/{userId}/prescriptions")
    public ResponseEntity<List<PrescriptionDto>> getUserPrescriptions(@PathVariable int userId) {
        return ResponseEntity.ok(prescriptionService.getUserPrescriptions(userId));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(userService.getAllDoctors());
    }

    @GetMapping("/doctors/{docId}")
    public ResponseEntity<DoctorDto> getDoctor(@PathVariable int docId) {
        return ResponseEntity.ok(userService.getDoctor(docId));
    }
}
