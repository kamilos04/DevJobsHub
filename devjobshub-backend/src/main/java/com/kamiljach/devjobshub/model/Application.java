package com.kamiljach.devjobshub.model;

import com.kamiljach.devjobshub.config.Constants;
import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.mappers.ApplicationMapper;
import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.QuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestionAndAnswer;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    private String cvUrl;

    private LocalDateTime dateTimeOfCreation;

    private APPLICATION_STATUS status;

    @ElementCollection
    private List<QuestionAndAnswer> questionsAndAnswers = new ArrayList<>();

    @ElementCollection
    private List<RadioQuestionAndAnswer> radioQuestionsAndAnswers = new ArrayList<>();

    @ElementCollection
    private List<MultipleChoiceQuestionAndAnswer> multipleChoiceQuestionsAndAnswers = new ArrayList<>();



    public void putOffer(Offer offer){
        if(!offer.getApplications().contains(this)){
            offer.getApplications().add(this);
            this.offer = offer;
        }
    }

    public void putUser(User user){
        if(!user.getApplications().contains(this)){
            user.getApplications().add(this);
            this.user = user;
        }
    }

    public void removeOffer(){
        if(this.offer.getApplications().contains(this)){
            this.offer.getApplications().remove(this);
            this.offer = null;
        }
    }

    public void removeUser(){
        if(this.user.getApplications().contains(this)){
            this.user.getApplications().remove(this);
            this.user = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application application = (Application) o;
        return Objects.equals(id, application.id);
    }

    public ApplicationDto mapToApplicationDtoShallow(){
        ApplicationDto applicationDto = ApplicationMapper.INSTANCE.applicationToApplicationDto(this);
        applicationDto.setOffer(null);
        applicationDto.setUser(null);

        applicationDto.setDateTimeOfCreation(this.getDateTimeOfCreation().format(Constants.dateTimeFormatter));

        return applicationDto;
    }


}
