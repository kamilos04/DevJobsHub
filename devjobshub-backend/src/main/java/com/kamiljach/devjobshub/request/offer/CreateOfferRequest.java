package com.kamiljach.devjobshub.request.offer;

import com.kamiljach.devjobshub.model.*;
import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestion;
import com.kamiljach.devjobshub.model.embeddable.Question;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOfferRequest {
    private String name;

    private OFFER_JOB_LEVEL jobLevel;

    private OFFER_OPERATING_MODE operatingMode;

    private Long minSalaryUoP;

    private Long maxSalaryUoP;

    private Boolean isSalaryMonthlyUoP;

    private Long minSalaryB2B;

    private Long maxSalaryB2B;

    private Boolean isSalaryMonthlyB2B;

    private Long minSalaryUZ;

    private Long maxSalaryUZ;

    private Boolean isSalaryMonthlyUZ;

//    private ArrayList<Long> candidates = new ArrayList<>();

    private String localization;


//    private ArrayList<Long> likedByUsers = new ArrayList<>();

    private ArrayList<Long> requiredTechnologies = new ArrayList<>();

    private ArrayList<Long> niceToHaveTechnologies = new ArrayList<>();

    private String aboutProject;

    private String responsibilitiesText;

    private List<String> responsibilities = new ArrayList<>();

    private List<String> requirements = new ArrayList<>();

    private List<String> niceToHave = new ArrayList<>();

    private List <String> whatWeOffer = new ArrayList<>();

    private List<Question> questions = new ArrayList<>();

    private List<RadioQuestion> radioQuestions = new ArrayList<>();

    private List<MultipleChoiceQuestion> multipleChoiceQuestions = new ArrayList<>();

    private Boolean isActive;
}
