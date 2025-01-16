package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.mappers.ApplicationMapper;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.model.embeddable.*;
import com.kamiljach.devjobshub.repository.ApplicationRepository;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.response.PageResponse;
import com.kamiljach.devjobshub.service.ApplicationService;
import com.kamiljach.devjobshub.service.UserService;
import com.kamiljach.devjobshub.service.UtilityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private OfferRepository offerRepository;

    private ApplicationRepository applicationRepository;

    private UserRepository userRepository;
    private UserService userService;

    private UtilityService utilityService;

    public ApplicationServiceImpl(OfferRepository offerRepository, ApplicationRepository applicationRepository, UserRepository userRepository, UserService userService, UtilityService utilityService) {
        this.offerRepository = offerRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.utilityService = utilityService;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApplicationDto applyForOffer(CreateApplicationRequest createApplicationRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, QuestionOrAnswerIsIncorrectException, OfferExpiredException, FirmAccountCanNotDoThatException, UserAlreadyAppliedForThisOfferException {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);

        User user = userService.findUserByJwt(jwt);
        utilityService.isFirmFalseOrThrowException(user);
        ifUserAlreadyAppliedForOfferThrowException(user, offer);


        if(LocalDateTime.now().isAfter(offer.getExpirationDate())){
            throw new OfferExpiredException();
        }

        Application newApplication = ApplicationMapper.INSTANCE.createApplicationRequestToApplication(createApplicationRequest);

        validateAllQuestionsInApplication(newApplication, offer);

        newApplication.setDateTimeOfCreation(LocalDateTime.now());


        newApplication.putUser(user);
        userRepository.save(user);


        newApplication.putOffer(offer);
        offerRepository.save(offer);

        applicationRepository.save(newApplication);
        return Application.mapApplicationToApplicationDtoShallow(newApplication);
    }

    public ApplicationDto getApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if(optionalApplication.isEmpty()){throw new ApplicationNotFoundByIdException();}
        return Application.mapApplicationToApplicationDtoShallow(optionalApplication.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException {
        Application application = applicationRepository.findById(id).orElseThrow(ApplicationNotFoundByIdException::new);

        deleteApplication(application);
    }

    @Transactional
    public void deleteApplication(Application application){
        Offer offer = application.getOffer();
        User user = application.getUser();
        application.removeOffer();
        application.removeUser();
        offerRepository.save(offer);
        userRepository.save(user);
        applicationRepository.delete(application);
    }


    public void ifUserAlreadyAppliedForOfferThrowException(User user, Offer offer) throws UserAlreadyAppliedForThisOfferException {
        for (Application application : user.getApplications()){
            if(application.getOffer().equals(offer)) throw new UserAlreadyAppliedForThisOfferException();
        }
    }

    public PageResponse<ApplicationDto> getApplicationsFromOffer(Long offerId, Integer numberOfElements, Integer pageNumber, String jwt) throws OfferNotFoundByIdException {
        Offer offer= offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);
        Pageable pageable = PageRequest.of(pageNumber, numberOfElements, Sort.by("dateTimeOfCreation").ascending());


        Page<Application> page = applicationRepository.getApplicationsFromOffer(offerId, pageable);
        ArrayList<Application> applications = new ArrayList<>(page.getContent());
        ArrayList<ApplicationDto> applicationDtos = new ArrayList<>();

        applications.forEach(element -> {applicationDtos.add(Application.mapApplicationToApplicationDtoShallow(element));});

        PageResponse<ApplicationDto> pageResponse = new PageResponse<ApplicationDto>(applicationDtos, page);
        return pageResponse;
    }

    public PageResponse<ApplicationDto> getFavouriteApplicationsFromOffer(Long offerId, Integer numberOfElements, Integer pageNumber, String jwt) throws OfferNotFoundByIdException {
        Offer offer= offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);
        Pageable pageable = PageRequest.of(pageNumber, numberOfElements, Sort.by("dateTimeOfCreation").ascending());


        Page<Application> page = applicationRepository.getFavouriteApplicationsFromOffer(offerId, pageable);
        ArrayList<Application> applications = new ArrayList<>(page.getContent());
        ArrayList<ApplicationDto> applicationDtos = new ArrayList<>();

        applications.forEach(element -> {applicationDtos.add(Application.mapApplicationToApplicationDtoShallow(element));});

        PageResponse<ApplicationDto> pageResponse = new PageResponse<ApplicationDto>(applicationDtos, page);
        return pageResponse;
    }

    public boolean validateQuestion(QuestionAndAnswer questionAndAnswer, Question question){
        if(!questionAndAnswer.getNumber().equals(question.getNumber())){
            return(false);
        }
        if(!questionAndAnswer.getQuestion().equals(question.getQuestion())){
            return(false);
        }
        return(true);

    }

    public boolean validateRadioQuestion(RadioQuestionAndAnswer radioQuestionAndAnswer, RadioQuestion radioQuestion){
        if(!radioQuestionAndAnswer.getNumber().equals(radioQuestion.getNumber())){
            return false;
        }
        if(!radioQuestionAndAnswer.getQuestion().equals(radioQuestion.getQuestion())){
            return false;
        }

        if(!(radioQuestionAndAnswer.getAnswer()>=0 && radioQuestionAndAnswer.getAnswer()<radioQuestion.getPossibleAnswers().size())){
            return false;
        }
        return(true);

    }

    public boolean validateMultipleChoiceQuestion(MultipleChoiceQuestionAndAnswer multipleChoiceQuestionAndAnswer, MultipleChoiceQuestion multipleChoiceQuestion){
        if(!multipleChoiceQuestionAndAnswer.getNumber().equals(multipleChoiceQuestion.getNumber())){
            return(false);
        }
        if(!multipleChoiceQuestionAndAnswer.getQuestion().equals(multipleChoiceQuestion.getQuestion())){
            return(false);
        }
        for(int i = 0; i < multipleChoiceQuestionAndAnswer.getAnswers().size(); i++){
            Integer answer = multipleChoiceQuestionAndAnswer.getAnswers().get(i);
            if(!(answer>=0 && answer < multipleChoiceQuestion.getPossibleAnswers().size())){
                return false;
            }
        }
        return(true);

    }

    public void validateAllQuestionsInApplication(Application application, Offer offer) throws QuestionOrAnswerIsIncorrectException {
        for(int i = 0; i < application.getQuestionsAndAnswers().size(); i++){
            if(!validateQuestion(application.getQuestionsAndAnswers().get(i), offer.getQuestions().get(i))){
                throw new QuestionOrAnswerIsIncorrectException();
            }
        }

        for(int i = 0; i < application.getRadioQuestionsAndAnswers().size(); i++){
            if(!validateRadioQuestion(application.getRadioQuestionsAndAnswers().get(i), offer.getRadioQuestions().get(i))){
                throw new QuestionOrAnswerIsIncorrectException();
            }
        }

        for(int i = 0; i < application.getMultipleChoiceQuestionsAndAnswers().size(); i++){
            if(!validateMultipleChoiceQuestion(application.getMultipleChoiceQuestionsAndAnswers().get(i), offer.getMultipleChoiceQuestions().get(i))){
                throw new QuestionOrAnswerIsIncorrectException();
            }
        }


    }
}
