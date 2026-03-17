package org.example.healthappbackendjava.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.example.healthappbackendjava.enums.Gender;
import org.example.healthappbackendjava.enums.Role;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String specialization;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;
    private Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String profilePicture;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
    private int rating;
    private double consultationFee;
    private LocalTime appointmentSlot;

    public LocalTime getAppointmentSlot() { return appointmentSlot; }
    public void setAppointmentSlot(LocalTime appointmentSlot) { this.appointmentSlot = appointmentSlot; }

    @JsonManagedReference("doctor-appointments")
    @OneToMany(mappedBy = "doctor")
    private List<Appointments> appointments;

    public List<Appointments> getAppointments() { return appointments; }
    public void setAppointments(List<Appointments> appointments) { this.appointments = appointments; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
}
