package com.kamiljach.devjobshub.request.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Size(min = 1, max = 100, message = "email must be between 1 and 100 characters")
    @NotBlank(message = "email can not be blank")
    private String email;

    @Size(min = 1, max = 100, message = "password must be between 1 and 100 characters")
    @NotBlank(message = "password can not be blank")
    private String password;
}
