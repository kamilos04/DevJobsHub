package com.kamiljach.devjobshub.model;

import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.QuestionAndAnswer;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestionAndAnswer;
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favouriteApplications")
    private List<Offer> assignedAsFavourite = new ArrayList<>();

    public void setOffer(Offer offer){
        if(!offer.getApplications().contains(this)){
            offer.getApplications().add(this);
            this.offer = offer;
        }
    }

    public void setUser(User user){
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
}
