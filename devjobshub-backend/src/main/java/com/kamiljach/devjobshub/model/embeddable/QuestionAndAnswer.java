package com.kamiljach.devjobshub.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAndAnswer {
    private Integer number;

    private String question;

    private String answer;
}
