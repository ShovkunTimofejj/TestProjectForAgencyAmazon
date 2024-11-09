package com.example.testprojectforagencyamazon.service.entityService;

import com.example.testprojectforagencyamazon.user.Role;
import com.example.testprojectforagencyamazon.data.UserDto;
import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.example.testprojectforagencyamazon.mapper.UserMapper;
import com.example.testprojectforagencyamazon.model.User;
import com.example.testprojectforagencyamazon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableCaching
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseContainer register(UserDto userDto) {
        ResponseContainer responseContainer = new ResponseContainer();

        if (userDto == null) {
            logError("UserDto is null");
            return responseContainer.setErrorMessageAndStatusCode("UserDto is null",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (isNullOrEmpty(userDto.getUsername())) {
            logError("Username is null");
            return responseContainer.setErrorMessageAndStatusCode("Username is null",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (isNullOrEmpty(userDto.getPassword())) {
            logError("Password is null");
            return responseContainer.setErrorMessageAndStatusCode("Password is null",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            logError("User already exists");
            return responseContainer.setErrorMessageAndStatusCode("User already exists",
                    HttpStatus.BAD_REQUEST.value());
        }

        User user = userMapper.fromDto(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        setRole(user, userDto.getRole());

        User savedUser = userRepository.save(user);
        responseContainer.setCreatedResult(userMapper.toResponseDto(savedUser));

        return responseContainer;
    }

    private boolean isNullOrEmpty(String str) {
        return !StringUtils.hasText(str);
    }

    private void logError(String message) {
        log.error(message);
    }

    private void setRole(User user, String role) {
        switch (role != null ? role.toUpperCase() : "") {
            case "ADMIN":
                user.setRole(Role.ADMIN.toString());
                break;
            case "MANAGER":
                user.setRole(Role.MANAGER.toString());
                break;
            default:
                user.setRole(Role.USER.toString());
                break;
        }
    }
}


