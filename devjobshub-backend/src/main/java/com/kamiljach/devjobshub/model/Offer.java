package com.kamiljach.devjobshub.model;

import com.kamiljach.devjobshub.config.Constants;
import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.mappers.OfferMapper;
import com.kamiljach.devjobshub.model.embeddable.MultipleChoiceQuestion;
import com.kamiljach.devjobshub.model.embeddable.Question;
import com.kamiljach.devjobshub.model.embeddable.RadioQuestion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String firmName;

    @Enumerated(EnumType.STRING)
    private OFFER_JOB_LEVEL jobLevel;

    @Enumerated(EnumType.STRING)
    private OFFER_OPERATING_MODE operatingMode;

    @Enumerated(EnumType.STRING)
    private OFFER_SPECIALIZATION specialization;

    private Long minSalaryUoP;

    private Long maxSalaryUoP;

    private Boolean isSalaryMonthlyUoP;

    private Long minSalaryB2B;

    private Long maxSalaryB2B;

    private Boolean isSalaryMonthlyB2B;

    private Long minSalaryUZ;

    private Long maxSalaryUZ;

    private Boolean isSalaryMonthlyUZ;

    private String localization;

    private String address;

    private LocalDateTime dateTimeOfCreation;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likedOffers")
    private List<User> likedByUsers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "offer_required_technology",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private List<Technology> requiredTechnologies = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "offer_nice_to_have_technology",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private List<Technology> niceToHaveTechnologies = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String aboutProject;

    @Column(columnDefinition = "TEXT")
    private String responsibilitiesText;

    @ElementCollection
    private List<String> responsibilities = new ArrayList<>();

    @ElementCollection
    private List<String> requirements = new ArrayList<>();

    @ElementCollection
    private List<String> niceToHave = new ArrayList<>();

    @ElementCollection
    private List<String> whatWeOffer = new ArrayList<>();

    @ElementCollection
    private List<Question> questions = new ArrayList<>();

    @ElementCollection
    private List<RadioQuestion> radioQuestions = new ArrayList<>();

    @ElementCollection
    private List<MultipleChoiceQuestion> multipleChoiceQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "offer")
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "assignedAsFavourite")
    private List<Application> favouriteApplications = new ArrayList<>();

    private LocalDateTime expirationDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "offer_recruiter",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "recruiter_id")
    )
    private List<User> recruiters = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id);
    }


    public void addRequiredTechnology(Technology technology) {
        if (!requiredTechnologies.contains(technology)) {
            requiredTechnologies.add(technology);
            technology.getAssignedAsRequired().add(this);
        }
    }

    public void addNiceToHaveTechnology(Technology technology) {
        if (!niceToHaveTechnologies.contains(technology)) {
            niceToHaveTechnologies.add(technology);
            technology.getAssignedAsNiceToHave().add(this);
        }
    }

    public void addLikedByUser(User user) {
        if (!likedByUsers.contains(user)) {
            likedByUsers.add(user);
            user.getLikedOffers().add(this);
        }
    }

    public void removeNiceToHaveTechnology(Technology technology) {
        if (niceToHaveTechnologies.contains(technology)) {
            niceToHaveTechnologies.remove(technology);
            technology.getAssignedAsNiceToHave().remove(this);
        }
    }

    public void removeRequiredTechnology(Technology technology) {
        if (requiredTechnologies.contains(technology)) {
            requiredTechnologies.remove(technology);
            technology.getAssignedAsRequired().remove(this);
        }
    }

    public void addFavouriteApplication(Application application) {
        if (!favouriteApplications.contains(application)) {
            favouriteApplications.add(application);
            application.setAssignedAsFavourite(this);
        }
    }

    public void removeFavouriteApplication(Application application) {
        if (favouriteApplications.contains(application)) {
            favouriteApplications.remove(application);
            application.setAssignedAsFavourite(null);
        }
    }

    public void addRecruiter(User user) {
        if (!recruiters.contains(user)) {
            recruiters.add(user);
            user.getOffers().add(this);
        }
    }

    public void removeRecruiter(User user) {
        if (recruiters.contains(user)) {
            recruiters.remove(user);
            user.getOffers().remove(this);
        }
    }


    public OfferDto mapToOfferDto() {
        OfferDto newOfferDto = OfferMapper.INSTANCE.offerToOfferDto(this);
        newOfferDto.setDateTimeOfCreation(this.getDateTimeOfCreation().format(Constants.dateTimeFormatter));
        newOfferDto.setExpirationDate(this.getExpirationDate().format(Constants.dateTimeFormatter));
        newOfferDto.setRequiredTechnologies(this.getRequiredTechnologies().stream().map(element -> element.mapToTechnologyDtoShallow()).collect(Collectors.toList()));
        newOfferDto.setNiceToHaveTechnologies(this.getNiceToHaveTechnologies().stream().map(element -> element.mapToTechnologyDtoShallow()).collect(Collectors.toList()));
        newOfferDto.setRecruiters(this.getRecruiters().stream().map(element -> element.mapToUserDtoShallow()).toList());

        return newOfferDto;
    }

    public OfferDto mapToOfferDtoShallow() {
        OfferDto newOfferDto = OfferMapper.INSTANCE.offerToOfferDto(this);
        newOfferDto.setDateTimeOfCreation(this.getDateTimeOfCreation().format(Constants.dateTimeFormatter));
        newOfferDto.setExpirationDate(this.getExpirationDate().format(Constants.dateTimeFormatter));
        newOfferDto.setRequiredTechnologies(this.getRequiredTechnologies().stream().map(element -> element.mapToTechnologyDtoShallow()).collect(Collectors.toList()));
        newOfferDto.setNiceToHaveTechnologies(this.getNiceToHaveTechnologies().stream().map(element -> element.mapToTechnologyDtoShallow()).collect(Collectors.toList()));
        return newOfferDto;
    }
}
