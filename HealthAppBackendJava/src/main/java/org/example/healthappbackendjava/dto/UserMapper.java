package org.example.healthappbackendjava.dto;

import org.example.healthappbackendjava.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User dtoToUser(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setGender(dto.getGender());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        user.setAge(dto.getAge());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfilePicture(dto.getProfilePicture());
        user.setRole(dto.getRole());
        return user;
    }

    public UserDto userToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
//        dto.setPassword(user.getPassword());
        dto.setGender(user.getGender());
        dto.setRole(user.getRole());
        dto.setHeight(user.getHeight());
        dto.setWeight(user.getWeight());
        dto.setAge(user.getAge());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setAppointments(user.getAppointments());
        return dto;
    }
}
