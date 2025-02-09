package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
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

        Application newApplication = createApplicationRequest.mapToApplication();

        newApplication.setIsFavourite(false);

        validateAllQuestionsInApplication(newApplication, offer);

        newApplication.setDateTimeOfCreation(LocalDateTime.now());


        newApplication.putUser(user);
        userRepository.save(user);


        newApplication.putOffer(offer);
        offerRepository.save(offer);

        applicationRepository.save(newApplication);
        return newApplication.mapToApplicationDtoShallow();
    }

    public ApplicationDto getApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException, NoPermissionException {
        Application application = applicationRepository.findById(id).orElseThrow(ApplicationNotFoundByIdException::new);
        User user = userService.findUserByJwt(jwt);
        validatePermissionGetApplicationById(user, application);
        return application.mapToApplicationDtoShallow();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException, NoPermissionException {
        Application application = applicationRepository.findById(id).orElseThrow(ApplicationNotFoundByIdException::new);
        User user = userService.findUserByJwt(jwt);
        validatePermissionDeleteApplicationById(user, application);
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


    public PageResponse<ApplicationDto> getApplicationsFromOffer(Long offerId, Integer numberOfElements, Integer pageNumber, Boolean isFavourite, String jwt) throws OfferNotFoundByIdException, NoPermissionException {
        Offer offer= offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);
        User user = userService.findUserByJwt(jwt);
        validatePermissionGetApplicationsFromOffer(user, offer);

        Pageable pageable = PageRequest.of(pageNumber, numberOfElements, Sort.by("dateTimeOfCreation").ascending());

        Page<Application> page;
        if(isFavourite){
            page = applicationRepository.getFavouriteApplicationsFromOffer(offerId, pageable);
        }
        else{
            page = applicationRepository.getApplicationsFromOffer(offerId, pageable);
        }

        ArrayList<Application> applications = new ArrayList<>(page.getContent());
        ArrayList<ApplicationDto> applicationDtos = new ArrayList<>();

        applications.forEach(element -> {applicationDtos.add(element.mapToApplicationDtoShallow());});

        PageResponse<ApplicationDto> pageResponse = new PageResponse<ApplicationDto>(applicationDtos, page);
        return pageResponse;
    }


    @Transactional(rollbackFor = Exception.class)
    public void addApplicationToFavourites(Long applicationId, String jwt) throws ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException, NoPermissionException {
        User user = userService.findUserByJwt(jwt);
        Application application = applicationRepository.findById(applicationId).orElseThrow(ApplicationNotFoundByIdException::new);
        Offer offer = application.getOffer();

        validatePermissionAddApplicationToFavourites(user, offer);


        if(application.getIsFavourite()){throw new ApplicationAlreadyIsInFavouritesException();}

        application.setIsFavourite(true);
        applicationRepository.save(application);
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeApplicationFromFavourites(Long applicationId, String jwt) throws ApplicationNotFoundByIdException, ApplicationIsNotInFavouritesException, NoPermissionException {
        User user = userService.findUserByJwt(jwt);
        Application application = applicationRepository.findById(applicationId).orElseThrow(ApplicationNotFoundByIdException::new);
        Offer offer = application.getOffer();

        validatePermissionRemoveApplicationFromFavourites(user, offer);

        if(!application.getIsFavourite()){throw new ApplicationIsNotInFavouritesException();}

        application.setIsFavourite(false);
        applicationRepository.save(application);

    }


    public void validatePermissionRemoveApplicationFromFavourites(User user, Offer offer) throws NoPermissionException {
        if (offer.getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }

    public void validatePermissionAddApplicationToFavourites(User user, Offer offer) throws NoPermissionException {
        if (offer.getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }

    public void ifUserAlreadyAppliedForOfferThrowException(User user, Offer offer) throws UserAlreadyAppliedForThisOfferException {
        for (Application application : user.getApplications()){
            if(application.getOffer().equals(offer)) throw new UserAlreadyAppliedForThisOfferException();
        }
    }

    public void validateQuestion(QuestionAndAnswer questionAndAnswer, Question question) throws QuestionOrAnswerIsIncorrectException {
        if(!questionAndAnswer.getNumber().equals(question.getNumber())){
            throw new QuestionOrAnswerIsIncorrectException();
        }
        if(!questionAndAnswer.getQuestion().equals(question.getQuestion())){
            throw new QuestionOrAnswerIsIncorrectException();
        }

    }

    public void validateRadioQuestion(RadioQuestionAndAnswer radioQuestionAndAnswer, RadioQuestion radioQuestion) throws QuestionOrAnswerIsIncorrectException {
        if(!radioQuestionAndAnswer.getNumber().equals(radioQuestion.getNumber())){
            throw new QuestionOrAnswerIsIncorrectException();
        }
        if(!radioQuestionAndAnswer.getQuestion().equals(radioQuestion.getQuestion())){
            throw new QuestionOrAnswerIsIncorrectException();
        }

        if(!(radioQuestionAndAnswer.getAnswer()>=0 && radioQuestionAndAnswer.getAnswer()<radioQuestion.getPossibleAnswers().size())){
            throw new QuestionOrAnswerIsIncorrectException();
        }

    }

    public void validateMultipleChoiceQuestion(MultipleChoiceQuestionAndAnswer multipleChoiceQuestionAndAnswer, MultipleChoiceQuestion multipleChoiceQuestion) throws QuestionOrAnswerIsIncorrectException {
        if(!multipleChoiceQuestionAndAnswer.getNumber().equals(multipleChoiceQuestion.getNumber())){
            throw new QuestionOrAnswerIsIncorrectException();
        }
        if(!multipleChoiceQuestionAndAnswer.getQuestion().equals(multipleChoiceQuestion.getQuestion())){
            throw new QuestionOrAnswerIsIncorrectException();
        }
        for(int i = 0; i < multipleChoiceQuestionAndAnswer.getAnswers().size(); i++){
            Integer answer = multipleChoiceQuestionAndAnswer.getAnswers().get(i);
            if(!(answer>=0 && answer < multipleChoiceQuestion.getPossibleAnswers().size())){
                throw new QuestionOrAnswerIsIncorrectException();
            }
        }

    }

    public void validateAllQuestionsInApplication(Application application, Offer offer) throws QuestionOrAnswerIsIncorrectException {
        if(application.getQuestionsAndAnswers().size() != offer.getQuestions().size()){
            throw new QuestionOrAnswerIsIncorrectException();
        }
        for(int i = 0; i < application.getQuestionsAndAnswers().size(); i++){
            validateQuestion(application.getQuestionsAndAnswers().get(i), offer.getQuestions().get(i));
        }

        if(application.getRadioQuestionsAndAnswers().size() != offer.getRadioQuestions().size()){
            throw new QuestionOrAnswerIsIncorrectException();
        }
        for(int i = 0; i < application.getRadioQuestionsAndAnswers().size(); i++){
            validateRadioQuestion(application.getRadioQuestionsAndAnswers().get(i), offer.getRadioQuestions().get(i));
        }

        if(application.getMultipleChoiceQuestionsAndAnswers().size() != offer.getMultipleChoiceQuestions().size()){
            throw new QuestionOrAnswerIsIncorrectException();
        }
        for(int i = 0; i < application.getMultipleChoiceQuestionsAndAnswers().size(); i++){
            validateMultipleChoiceQuestion(application.getMultipleChoiceQuestionsAndAnswers().get(i), offer.getMultipleChoiceQuestions().get(i));
        }


    }

    public void validatePermissionGetApplicationById(User user, Application application) throws NoPermissionException {
        if(user.getIsAdmin()){
            return;
        }
        if (application.getOffer().getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }

    public void validatePermissionDeleteApplicationById(User user, Application application) throws NoPermissionException {
        if(user.getIsAdmin()){
            return;
        }
        if (application.getOffer().getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }


    public void validatePermissionGetApplicationsFromOffer(User user, Offer offer) throws NoPermissionException {
        if(user.getIsAdmin()){
            return;
        }
        if (offer.getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }
}
