package org.example.healthappbackendjava.security;

import org.example.healthappbackendjava.entity.Admin;
import org.example.healthappbackendjava.entity.Doctor;
import org.example.healthappbackendjava.entity.User;
import org.example.healthappbackendjava.repository.AdminRepository;
import org.example.healthappbackendjava.repository.DoctorRepository;
import org.example.healthappbackendjava.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

// Custom UserDetailsService to load user/admin from database for authentication
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;

    public CustomUserDetailService(UserRepository userRepository, AdminRepository adminRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First, try to find in User table
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            // Build UserDetails with USER role
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities("ROLE_" + (user.getRole() != null ? user.getRole().name() : "USER"))
                    .build();
        }

        Optional<Doctor> docOpt = doctorRepository.findByEmail(email);
        if(docOpt.isPresent()) {
            Doctor doc = docOpt.get();
            // Build UserDetails with USER role
            return org.springframework.security.core.userdetails.User
                    .withUsername(doc.getEmail())
                    .password(doc.getPassword())
                    .authorities("ROLE_" + (doc.getRole() != null ? doc.getRole().name() : "DOCTOR"))
                    .build();
        }

        // If not found in User table, try Admin table
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if(adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            // Build UserDetails with ADMIN role
            return org.springframework.security.core.userdetails.User
                    .withUsername(admin.getEmail())
                    .password(admin.getPassword())
                    .authorities("ROLE_" + (admin.getRole() != null ? admin.getRole().name() : "ADMIN"))
                    .build();
        }

        // If not found in either table, throw exception
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

