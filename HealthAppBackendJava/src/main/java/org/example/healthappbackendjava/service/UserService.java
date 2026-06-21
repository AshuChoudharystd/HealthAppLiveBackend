package org.example.healthappbackendjava.service;

import org.example.healthappbackendjava.config.CacheNames;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    final private UserRepository repo;
    final private UserMapper mapper;
    final private AppointmentRepository apRepo;
    final private DoctorRepository docRepo;
    final private DoctorMapper dMapper;
    final private FileStorageService fileStorageService;
    final private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, UserMapper mapper, AppointmentRepository apRepo, DoctorRepository docRepo, DoctorMapper dMapper,
                       PasswordEncoder passwordEncoder, FileStorageService fileStorageService) {
        this.repo = repo;
        this.mapper = mapper;
        this.apRepo = apRepo;
        this.docRepo = docRepo;
        this.dMapper = dMapper;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    private void updateAppointmentStatus(Appointments appointment){
        if(appointment.getStatus() == AppointmentStatus.FINISHED
        ||appointment.getStatus() == AppointmentStatus.CANCELLED){
            return ;
        }

        LocalDateTime appointmentTime = LocalDateTime.of(
                appointment.getAppointmentDate(),
                appointment.getAppointmentEndTime()
        );

        if(appointmentTime.isBefore(LocalDateTime.now())) {
            appointment.setStatus(AppointmentStatus.FINISHED);
            apRepo.save(appointment);
        }
    }

//    get User by id
    @Cacheable(value = CacheNames.USER, key = "#id")
    public UserDto getUser(int id) {
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.userToDto(user);
    }

//    create user
    public UserDto createUser(UserDto dto){
        User user = mapper.dtoToUser(dto);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        repo.save(user);
        return mapper.userToDto(user);
    }

//    update User profile
    @CacheEvict(value = CacheNames.USERs, key = "#id")
    public UserDto updateUser(int id, UserDto dto){
        User user = repo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setGender(dto.getGender());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        user.setAge(dto.getAge());
        user.setPhoneNumber(dto.getPhoneNumber());
        repo.save(user);
        return mapper.userToDto(user);
    }

//    update the profile picture
    public UserDto updateProfilePicture(int id, MultipartFile file){
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = fileStorageService.uploadFile(file);

        user.setProfilePicture(imageUrl);

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
    @Caching(evict = {
            @CacheEvict(value = CacheNames.APPOINTMENTs, key = "#userId"),
            @CacheEvict(value = CacheNames.APPOINTMENTs, allEntries = true)
    })
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
        List<Appointments> appointments= apRepo.findByUserId(userId);

        appointments.forEach(this::updateAppointmentStatus);

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
        updateAppointmentStatus(appointment);
        return appointment;
    }

//    get all doctors
    @Cacheable(value = CacheNames.DOCTORs, key = "'all'")
    public List<Doctor> getAllDoctors(){
        return docRepo.findAll();
    }

//    get Doctor
    @Cacheable(value = CacheNames.DOCTOR, key = "#docId")
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
