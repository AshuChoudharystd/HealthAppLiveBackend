package org.example.healthappbackendjava.service;

import org.example.healthappbackendjava.dto.DoctorDto;
import org.example.healthappbackendjava.dto.DoctorMapper;
import org.example.healthappbackendjava.dto.UserDto;
import org.example.healthappbackendjava.dto.UserMapper;
import org.example.healthappbackendjava.entity.Admin;
import org.example.healthappbackendjava.enums.Role;
import org.example.healthappbackendjava.repository.AdminRepository;
import org.example.healthappbackendjava.repository.DoctorRepository;
import org.example.healthappbackendjava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    final private AdminRepository repo;
    final private UserRepository uRepo;
    final private DoctorRepository dRepo;
    final private DoctorMapper dMapper;
    final private UserMapper uMapper;

    public AdminService(AdminRepository repo, UserRepository uRepo, DoctorRepository dRepo, DoctorMapper dMapper, UserMapper uMapper) {
        this.repo = repo;
        this.uRepo = uRepo;
        this.dRepo = dRepo;
        this.dMapper = dMapper;
        this.uMapper = uMapper;
    }

//    create admin profile
    public Admin createAdmin(Admin admin){
        admin.setRole(Role.ADMIN);
        repo.save(admin);
        return admin;
    }

//    get all users
    public List<UserDto> getAllUsers(){
        return uRepo.findAll().stream().map(uMapper::userToDto).toList();
    }

//    get All Doctors
    public List<DoctorDto> getAllDoctors(){
        return dRepo.findAll().stream().map(dMapper::doctorToDto).toList();
    }

}
