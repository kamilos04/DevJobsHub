package com.kamiljach.devjobshub.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultipleChoiceQuestion {
    private Integer number;

    private String question;

    private List<String> possibleAnswers = new ArrayList<>();
}
