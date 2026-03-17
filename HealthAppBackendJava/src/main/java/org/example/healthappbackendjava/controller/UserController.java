package org.example.healthappbackendjava.controller;

import org.example.healthappbackendjava.dto.DoctorDto;
import org.example.healthappbackendjava.dto.UserDto;
import org.example.healthappbackendjava.entity.Appointments;
import org.example.healthappbackendjava.entity.Doctor;
import org.example.healthappbackendjava.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int id, @Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
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

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(userService.getAllDoctors());
    }

    @GetMapping("/doctors/{docId}")
    public ResponseEntity<DoctorDto> getDoctor(@PathVariable int docId) {
        return ResponseEntity.ok(userService.getDoctor(docId));
    }
}
