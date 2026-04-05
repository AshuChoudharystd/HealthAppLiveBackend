package org.example.healthappbackendjava.controller;

import jakarta.validation.Valid;
import org.example.healthappbackendjava.dto.LoginDto;
import org.example.healthappbackendjava.entity.Admin;
import org.example.healthappbackendjava.entity.Doctor;
import org.example.healthappbackendjava.entity.User;
import org.example.healthappbackendjava.repository.AdminRepository;
import org.example.healthappbackendjava.repository.DoctorRepository;
import org.example.healthappbackendjava.repository.UserRepository;
import org.example.healthappbackendjava.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtUtil jwtUtil;;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;

    public AuthController(AuthenticationManager manager, JwtUtil jwtUtil, UserRepository userRepository, DoctorRepository doctorRepository, AdminRepository adminRepository) {
        this.manager = manager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        String userId = jwtUtil.extractUserId(token);

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            return ResponseEntity.ok(Map.of("id", u.getId(), "email", u.getEmail(), "role", u.getRole().name(), "name", u.getName() != null ? u.getName() : ""));
        }
        Optional<Doctor> docOpt = doctorRepository.findByEmail(email);
        if (docOpt.isPresent()) {
            Doctor d = docOpt.get();
            return ResponseEntity.ok(Map.of("id", d.getId(), "email", d.getEmail(), "role", d.getRole().name(), "name", d.getName() != null ? d.getName() : ""));
        }
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin a = adminOpt.get();
            return ResponseEntity.ok(Map.of("id", a.getId(), "email", a.getEmail(), "role", a.getRole().name(), "name", a.getName() != null ? a.getName() : ""));
        }
        return ResponseEntity.status(404).body(Map.of("error", "User not found"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginRequest) {
        try {
            Authentication auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            String email = auth.getName();

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                return ResponseEntity.ok(jwtUtil.generateToken(userOpt.get().getEmail(), userOpt.get().getId()));
            }
            Optional<Doctor> docOpt = doctorRepository.findByEmail(email);
            if (docOpt.isPresent()) {
                return ResponseEntity.ok(jwtUtil.generateToken(docOpt.get().getEmail(), docOpt.get().getId()));
            }
            Optional<Admin> adminOpt = adminRepository.findByEmail(email);
            if (adminOpt.isPresent()) {
                return ResponseEntity.ok(jwtUtil.generateToken(adminOpt.get().getEmail(), adminOpt.get().getId()));
            }
            return ResponseEntity.status(404).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

}
