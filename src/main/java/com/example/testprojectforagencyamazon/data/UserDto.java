package com.example.testprojectforagencyamazon.data;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;

    @NotBlank(message = "username required")
    @Size(min = 2, max = 30, message = "username: min: {min}, max: {max} characters")
    private String username;

    @NotBlank(message = "password required")
    @Pattern(regexp = "^.{8,100}$", flags = Pattern.Flag.UNICODE_CASE, message = "invalid password")
    private String password;

    private String role;
}

