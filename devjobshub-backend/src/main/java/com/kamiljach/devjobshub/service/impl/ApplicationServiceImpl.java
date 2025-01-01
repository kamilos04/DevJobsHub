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
import com.kamiljach.devjobshub.service.ApplicationService;
import com.kamiljach.devjobshub.service.UserService;
import com.kamiljach.devjobshub.service.UtilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public ApplicationDto applyForOffer(CreateApplicationRequest createApplicationRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException, QuestionOrAnswerIsIncorrectException, OfferExpiredException, FirmAccountCanNotDoThatException, UserAlreadyAppliedForThisOfferException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}

        User user = userService.findUserByJwt(jwt);
        Offer offer = optionalOffer.get();
        utilityService.isFirmFalseOrThrowException(user);
        ifUserAlreadyAppliedForOfferThrowException(user, offer);


        if(LocalDateTime.now().isAfter(offer.getExpirationDate())){
            throw new OfferExpiredException();
        }

        Application newApplication = ApplicationMapper.INSTANCE.createApplicationRequestToApplication(createApplicationRequest);

        validateAllQuestionsInApplication(newApplication, optionalOffer.get());

        newApplication.setDateTimeOfCreation(LocalDateTime.now());


        newApplication.setUser(user);
        userRepository.save(user);


        newApplication.setOffer(offer);
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
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if(optionalApplication.isEmpty()){throw new ApplicationNotFoundByIdException();}
        utilityService.deleteApplication(optionalApplication.get());

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

    public void ifUserAlreadyAppliedForOfferThrowException(User user, Offer offer) throws UserAlreadyAppliedForThisOfferException {
        for (Application application : user.getApplications()){
            if(application.getOffer().equals(offer)) throw new UserAlreadyAppliedForThisOfferException();
        }
    }


}
