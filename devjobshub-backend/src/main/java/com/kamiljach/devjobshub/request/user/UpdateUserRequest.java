package com.kamiljach.devjobshub.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotNull
    private Long id;

    @Size(min = 1, max = 100, message = "email must be between 1 and 100 characters")
    @NotBlank(message = "email can not be blank")
    private String email;

    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    @NotBlank(message = "name can not be blank")
    private String name;

    private String surname;
}
