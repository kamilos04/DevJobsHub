package com.kamiljach.devjobshub.dto;

import com.kamiljach.devjobshub.mappers.ApplicationMapper;
import com.kamiljach.devjobshub.model.*;
import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.QuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestionAndAnswer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
    private Long id;

    private User user;

    private Offer offer;

    private String cvUrl;

    private List<QuestionAndAnswer> questionsAndAnswers = new ArrayList<>();

    private List<RadioQuestionAndAnswer> radioQuestionsAndAnswers = new ArrayList<>();

    private List<MultipleChoiceQuestionAndAnswer> multipleChoiceQuestionsAndAnswers = new ArrayList<>();

    public static ApplicationDto mapApplicationToApplicationDto(Application application){
        ApplicationDto applicationDto = ApplicationMapper.INSTANCE.applicationToApplicationDto(application);
        applicationDto.setOffer(null);
        applicationDto.setUser(null);
        return applicationDto;
    }
}
