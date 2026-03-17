package org.example.healthappbackendjava.dto;

import org.example.healthappbackendjava.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin dtoToAdmin(AdminDto dto) {
        Admin admin = new Admin();
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setRole(dto.getRole());
        return admin;
    }

    public AdminDto adminToDto(Admin admin) {
        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
//        dto.setPassword(admin.getPassword());
        dto.setRole(admin.getRole());
        return dto;
    }
}
