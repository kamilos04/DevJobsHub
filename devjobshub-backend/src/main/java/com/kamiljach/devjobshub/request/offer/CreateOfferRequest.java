package com.kamiljach.devjobshub.request.offer;

import com.kamiljach.devjobshub.model.*;
import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestion;
import com.kamiljach.devjobshub.model.embeddable.Question;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOfferRequest {
    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    @NotBlank(message = "name can not be blank")
    private String name;

    @NotNull
    private OFFER_JOB_LEVEL jobLevel;

    @NotNull
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

    @Size(min = 1, max = 100, message = "localization must be between 1 and 100 characters")
    @NotBlank(message = "localization can not be blank")
    private String localization;

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

    @NotBlank(message = "expirationDate can not be blank")
    private String expirationDate;
}
