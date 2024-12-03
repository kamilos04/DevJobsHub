package com.kamiljach.devjobshub.request.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(min = 1, max = 100, message = "email must be between 1 and 100 characters")
    @NotBlank(message = "email can not be blank")
    private String email;

    @Size(min = 1, max = 100, message = "password must be between 1 and 100 characters")
    @NotBlank(message = "password can not be blank")
    private String password;

    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    @NotBlank(message = "name can not be blank")
    private String name;

    @Size(min = 1, max = 100, message = "surname must be between 1 and 100 characters")
    @NotBlank(message = "surname can not be blank")
    private String surname;

    @NotNull(message = "isFirm can not be null")
    private Boolean isFirm;
}
