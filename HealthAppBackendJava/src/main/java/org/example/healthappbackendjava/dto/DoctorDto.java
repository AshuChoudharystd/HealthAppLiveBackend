package org.example.healthappbackendjava.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.healthappbackendjava.entity.Appointments;
import org.example.healthappbackendjava.enums.Gender;
import org.example.healthappbackendjava.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DoctorDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String name;
    private String specialization;
    private String email;
    private String phoneNumber;
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;
    private Gender gender;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    private String profilePicture;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int rating;
    private double consultationFee;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Appointments> appointments;
    private LocalTime appointmentSlot;

    public LocalTime getAppointmentSlot() { return appointmentSlot; }
    public void setAppointmentSlot(LocalTime appointmentSlot) { this.appointmentSlot = appointmentSlot; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public List<Appointments> getAppointments() { return appointments; }
    public void setAppointments(List<Appointments> appointments) { this.appointments = appointments; }
}
