package com.kamiljach.devjobshub.request.application;

import com.kamiljach.devjobshub.mappers.ApplicationMapper;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.QuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestionAndAnswer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationRequest {
    @NotBlank(message = "cvUrl can not be blank")
    private String cvUrl;

    private List<QuestionAndAnswer> questionsAndAnswers = new ArrayList<>();

    private List<RadioQuestionAndAnswer> radioQuestionsAndAnswers = new ArrayList<>();

    private List<MultipleChoiceQuestionAndAnswer> multipleChoiceQuestionsAndAnswers = new ArrayList<>();

    public Application mapToApplication(){
        Application application = ApplicationMapper.INSTANCE.createApplicationRequestToApplication(this);
        return application;
    }
}
