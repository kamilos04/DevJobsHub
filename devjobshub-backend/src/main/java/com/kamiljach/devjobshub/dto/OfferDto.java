package com.kamiljach.devjobshub.dto;

import com.kamiljach.devjobshub.config.Constants;
import com.kamiljach.devjobshub.mappers.OfferMapper;
import com.kamiljach.devjobshub.model.*;
import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestion;
import com.kamiljach.devjobshub.model.embeddable.Question;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    private Long id;

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

    private List<ApplicationDto> applications = new ArrayList<>();

    private String localization;

    private String dateTimeOfCreation;

    private List<User> likedByUsers = new ArrayList<>();

    private List<TechnologyDto> requiredTechnologies = new ArrayList<>();

    private List<TechnologyDto> niceToHaveTechnologies = new ArrayList<>();

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

    private String expirationDate;

//    public OfferDto(Offer offer){
//        this.id = offer.getId();
//        this.name = offer.getName();
//        this.jobLevel = offer.getJobLevel();
//        this.operatingMode = offer.getOperatingMode();
//        this.minSalary = offer.getMinSalary();
//        this.maxSalary = offer.getMaxSalary();
//        this.isSalaryMonthly = offer.getIsSalaryMonthly();
//        this.localization = offer.getLocalization();
//        this.dateTimeOfCreation = offer.getDateTimeOfCreation();
//        this.aboutProject = offer.getAboutProject();
//        this.responsibilitiesText = offer.getResponsibilitiesText();


//        OfferDto newOfferDto = OfferMapper.INSTANCE.offerToOfferDto(offer);

//        offer.getResponsibilities().stream().forEach(element -> this.responsibilities.add(element));
//        offer.getRequirements().stream().forEach(element -> this.requirements.add(element));
//        offer.getNiceToHave().stream().forEach(element -> this.niceToHave.add(element));
//        offer.getWhatWeOffer().stream().forEach(element -> this.whatWeOffer.add(element));
//        To do candidates, likedByUsers, requiredTechnolgies, niceToHaveTechnologies
//    }

    public static OfferDto mapOfferToOfferDto(Offer offer){
        OfferDto newOfferDto = OfferMapper.INSTANCE.offerToOfferDto(offer);
        newOfferDto.setDateTimeOfCreation(offer.getDateTimeOfCreation().format(Constants.dateTimeFormatter));
        newOfferDto.setExpirationDate(offer.getExpirationDate().format(Constants.dateTimeFormatter));
        newOfferDto.setRequiredTechnologies( offer.getRequiredTechnologies().stream().map(element -> TechnologyDto.mapTechnologyToTechnologyDto(element)).collect(Collectors.toList()) );
        newOfferDto.setNiceToHaveTechnologies(offer.getNiceToHaveTechnologies().stream().map(element -> TechnologyDto.mapTechnologyToTechnologyDto(element)).collect(Collectors.toList()));
        newOfferDto.setApplications(offer.getApplications().stream().map(element -> ApplicationDto.mapApplicationToApplicationDto(element)).collect(Collectors.toList()));


        return newOfferDto;
    }
}