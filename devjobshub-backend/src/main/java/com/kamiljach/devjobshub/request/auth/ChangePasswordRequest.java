package com.kamiljach.devjobshub.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @Size(min = 1, max = 100, message = "newPassword must be between 1 and 100 characters")
    @NotBlank(message = "newPassword can not be blank")
    private String newPassword;
}
