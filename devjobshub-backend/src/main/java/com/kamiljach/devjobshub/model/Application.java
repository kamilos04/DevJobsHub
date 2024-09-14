package com.kamiljach.devjobshub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    private String cvUrl;

    @ElementCollection
    private List<QuestionAndAnswer> questionsAndAnswers = new ArrayList<>();

    @ElementCollection
    private List<RadioQuestionAndAnswer> radioQuestionsAndAnswers = new ArrayList<>();

    @ElementCollection
    private List<MultipleChoiceQuestionAndAnswer> multipleChoiceQuestionsAndAnswers = new ArrayList<>();
}
