package com.kamiljach.devjobshub.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultipleChoiceQuestionAndAnswer {
    private Integer number;

    private String question;

    private List<String> possibleAnswers = new ArrayList<>();

    private List<Integer> answers = new ArrayList<>();
}
