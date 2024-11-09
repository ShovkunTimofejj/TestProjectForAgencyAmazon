package com.example.testprojectforagencyamazon.mapper;

import com.example.testprojectforagencyamazon.data.UserDto;
import com.example.testprojectforagencyamazon.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }
        return buildUserDto(user);
    }

    public User fromDto(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return buildUser(userDto);
    }

    private UserDto buildUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    private User buildUser(UserDto userDto) {
        return new User(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getRole()
        );
    }
}

