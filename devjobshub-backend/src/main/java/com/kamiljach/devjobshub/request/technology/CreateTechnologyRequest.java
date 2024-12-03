package com.kamiljach.devjobshub.request.technology;

import com.kamiljach.devjobshub.model.Offer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTechnologyRequest {
    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    @NotBlank(message = "name can not be blank")
    private String name;
}
