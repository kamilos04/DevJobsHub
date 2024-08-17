package com.kamiljach.devjobshub.request.technology;

import com.kamiljach.devjobshub.model.Offer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTechnologyRequest {

    private String name;

    private ArrayList<Long> assignedAsRequiredIds = new ArrayList<>();

    private ArrayList<Long> assignedAsNiceToHaveIds = new ArrayList<>();
}
