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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginDto user) throws Exception {
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        if(auth.isAuthenticated()) {

            String email = auth.getName();

            // 🔥 check all tables
            Optional<User> userOpt = userRepository.findByEmail(email);
            if(userOpt.isPresent()){
                User u = userOpt.get();
                return jwtUtil.generateToken(u.getEmail(),u.getId());
            }

            Optional<Doctor> docOpt = doctorRepository.findByEmail(email);
            if(docOpt.isPresent()){
                Doctor d = docOpt.get();
                return jwtUtil.generateToken(d.getEmail(),d.getId());
            }

            Optional<Admin> adminOpt = adminRepository.findByEmail(email);
            if(adminOpt.isPresent()){
                Admin a = adminOpt.get();
                return jwtUtil.generateToken(a.getEmail(),a.getId());
            }

            throw new RuntimeException("User not found");
        }

        return "Login Failed";
    }

}
