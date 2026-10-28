package com.scaler.bookMyShow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private int id;
    private String name;
    private String email;
    private String token;
}
