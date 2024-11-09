package com.example.testprojectforagencyamazon.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    public final String token;
    public final String refresh;
}
