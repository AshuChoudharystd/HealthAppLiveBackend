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
public class UserService {
    final private UserRepository repo;
    final private UserMapper mapper;
    final private AppointmentRepository apRepo;
    final private DoctorRepository docRepo;
    final private DoctorMapper dMapper;

    public UserService(UserRepository repo, UserMapper mapper, AppointmentRepository apRepo, DoctorRepository docRepo, DoctorMapper dMapper) {
        this.repo = repo;
        this.mapper = mapper;
        this.apRepo = apRepo;
        this.docRepo = docRepo;
        this.dMapper = dMapper;
    }

//    create user
    public UserDto createUser(UserDto dto){
        User user = mapper.dtoToUser(dto);
        user.setRole(Role.USER);
        repo.save(user);
        return mapper.userToDto(user);
    }

//    update User profile
    public UserDto updateUser(int id,UserDto dto){
        User user = repo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setGender(dto.getGender());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        user.setAge(dto.getAge());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfilePicture(dto.getProfilePicture());
        repo.save(user);
        return mapper.userToDto(user);
    }

//    delete User profile
    public String deleteUser(int id){
        User user = repo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        repo.delete(user);
        return "User deleted successfully";
    }

//    book appointment
    public Appointments bookAppointment(int userId, int doctorId, Appointments appointment){
        User user = repo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        Doctor doc = docRepo.findById(doctorId).orElseThrow(()->new RuntimeException("Doctor not found"));
        try{
            if(doc.getAppointmentSlot().equals(appointment.getAppointmentTime())){
                appointment.setStatus(AppointmentStatus.YET_TO_START);
                appointment.setUser(user);
                appointment.setDoctor(doc);
                apRepo.save(appointment);
                return appointment;
            }else{
                throw new RuntimeException("Doctor is not available at this time");
            }
        }catch (Exception e){
            throw new RuntimeException("Error booking appointment: " + e.getMessage());
        }
    }

//    List All Appointments
    public List<Appointments> getAllAppointments(int userId){
        return apRepo.findByUserId(userId);
    }

//    cancel appointment
    public String cancelAppointment(int id){
        Appointments appointment = apRepo.findById(id).orElseThrow(()->new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        apRepo.save(appointment);
        return "Appointment cancelled successfully";
    }

//    Reschedule Appointment
    public Appointments rescheduleAppointment(int id, Appointments newAppointmentData){
        Appointments appointment = apRepo.findById(id).orElseThrow(()->new RuntimeException("Appointment not found"));
        appointment.setAppointmentDate(newAppointmentData.getAppointmentDate());
        appointment.setAppointmentTime(newAppointmentData.getAppointmentTime());
        appointment.setAppointmentEndTime(newAppointmentData.getAppointmentEndTime());
        apRepo.save(appointment);
        return appointment;
    }

//    get User Appointment
    public Appointments getAppointment(int appId) {
        Appointments appointment = apRepo.findById(appId).orElseThrow(()->new RuntimeException("Appointment not found"));
        return appointment;
    }

//    get all doctors
    public List<Doctor> getAllDoctors(){
        return docRepo.findAll();
    }

//    get Doctor
    public DoctorDto getDoctor(int docId) {
        Doctor doctor = docRepo.findById(docId).orElseThrow(()->new RuntimeException("Doctor not found"));
        return dMapper.doctorToDto(doctor);
    }

//    Appointment finished
    public Appointments appointmentFinished(int appId){
        Appointments appointment = apRepo.findById(appId).orElseThrow(()->new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.FINISHED);
        apRepo.save(appointment);
        return appointment;
    }

}
