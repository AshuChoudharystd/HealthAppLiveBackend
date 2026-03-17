package org.example.healthappbackendjava.service;

import org.example.healthappbackendjava.dto.DoctorDto;
import org.example.healthappbackendjava.dto.DoctorMapper;
import org.example.healthappbackendjava.dto.UserDto;
import org.example.healthappbackendjava.dto.UserMapper;
import org.example.healthappbackendjava.entity.Appointments;
import org.example.healthappbackendjava.entity.Doctor;
import org.example.healthappbackendjava.entity.User;
import org.example.healthappbackendjava.enums.AppointmentStatus;
import org.example.healthappbackendjava.enums.Role;
import org.example.healthappbackendjava.repository.AppointmentRepository;
import org.example.healthappbackendjava.repository.DoctorRepository;
import org.example.healthappbackendjava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    final private DoctorRepository docRepo;
    final private UserRepository userRepo;
    final private AppointmentRepository apRepo;
    final private DoctorMapper mapper;
    final private UserMapper uMapper;

    public DoctorService(DoctorRepository docRepo, UserRepository userRepo, AppointmentRepository apRepo, DoctorMapper mapper, UserMapper uMapper) {
        this.docRepo = docRepo;
        this.userRepo = userRepo;
        this.apRepo = apRepo;
        this.mapper = mapper;
        this.uMapper = uMapper;
    }

//    create Doctor
    public DoctorDto createDoctor(DoctorDto dto){
        Doctor doctor = mapper.dtoToDoctor(dto);
        doctor.setRole(Role.DOCTOR);
        docRepo.save(doctor);
        return mapper.doctorToDto(doctor);
    }

//    Update Doctor Profile
    public DoctorDto updateDoctor(int id,DoctorDto dto){
        Doctor doctor = docRepo.findById(id).orElseThrow(()->new RuntimeException("Doctor not found"));
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(dto.getPassword());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setProfilePicture(dto.getProfilePicture());
        docRepo.save(doctor);
        return mapper.doctorToDto(doctor);
    }

//    Get All Appointments
    public List<Appointments> getDoctorAppointments(int docId){
        List<Appointments> appointments = apRepo.findByDoctorId(docId);
        return appointments;
    }

//    get Appointment
    public Appointments getDoctorAppointment(int appId) {
        Appointments appointment = apRepo.findById(appId).orElseThrow(() -> new RuntimeException("Appointment not found"));
        return appointment;
    }

//    cancel Appointment
    public Appointments cancelAppointment(int appId){
        Appointments appointment = apRepo.findById(appId).orElseThrow(()->new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        apRepo.save(appointment);
        return appointment;
    }

//    get user profile
    public UserDto getUserProfile(int userId){
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        return uMapper.userToDto(user);
    }

}
