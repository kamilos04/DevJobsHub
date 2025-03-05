package com.kamiljach.devjobshub.service.impl;


import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.ApplicationRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.request.offer.SearchOffersRequest;
import com.kamiljach.devjobshub.response.PageResponse;
import com.kamiljach.devjobshub.service.ApplicationService;
import com.kamiljach.devjobshub.service.OfferService;
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
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private UserRepository userRepository;
    private TechnologyRepository technologyRepository;

    private UtilityService utilityService;

    private UserService userService;

    private ApplicationService applicationService;

    private ApplicationRepository applicationRepository;

    public OfferServiceImpl(OfferRepository offerRepository, UserRepository userRepository, TechnologyRepository technologyRepository, UtilityService utilityService, UserService userService, ApplicationService applicationService, ApplicationRepository applicationRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
        this.utilityService = utilityService;
        this.userService = userService;
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public OfferDto createOffer(CreateOfferRequest createOfferRequest, String jwt) throws TechnologyNotFoundByIdException, NoFirmAccountCanNotDoThatException {
        User user = userService.findUserByJwt(jwt);
        //Validation account type
        utilityService.isFirmOrThrowException(user);

        Offer newOffer = createOfferRequest.mapToOffer();
        newOffer.setDateTimeOfCreation(LocalDateTime.now());

        newOffer.addRecruiter(user);
        userRepository.save(user);

        //Adding required technologies
        if(createOfferRequest.getRequiredTechnologies() != null){
            ArrayList<Technology> requiredTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getRequiredTechnologies());
            requiredTechnologies.forEach(element -> {
                newOffer.addRequiredTechnology(element);
                technologyRepository.save(element);
            });

        }

        //Adding nice to have technologies
        if(createOfferRequest.getNiceToHaveTechnologies() != null){
            ArrayList<Technology> niceToHaveTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getNiceToHaveTechnologies());
            niceToHaveTechnologies.forEach(element -> {
                newOffer.addNiceToHaveTechnology(element);
                technologyRepository.save(element);
            });
        }

        offerRepository.save(newOffer);
        return newOffer.mapToOfferDtoShallow();
    }

    @Transactional(rollbackFor = Exception.class)
    public OfferDto updateOffer(CreateOfferRequest createOfferRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException, NoPermissionException {
        Offer offerToUpdate = offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);

        //Validate permissions
        User user = userService.findUserByJwt(jwt);
        validatePermissionUpdateOffer(user, offerToUpdate);

        Offer offer = createOfferRequest.mapToExistingOffer(offerToUpdate);

        //Update nice to have technologies
        if(createOfferRequest.getNiceToHaveTechnologies() != null){
            for(int i = offer.getNiceToHaveTechnologies().size()-1; i >= 0; i--){
                Technology technology = offer.getNiceToHaveTechnologies().get(i);
                offer.removeNiceToHaveTechnology(technology);
                technologyRepository.save(technology);
            }
            ArrayList<Technology> niceToHaveTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getNiceToHaveTechnologies());
            niceToHaveTechnologies.forEach(element -> {
                offer.addNiceToHaveTechnology(element);
                technologyRepository.save(element);
            });
        }

        //Update required technologies
        if(createOfferRequest.getRequiredTechnologies() != null){
            for(int i = offer.getRequiredTechnologies().size()-1; i >= 0; i--){
                Technology technology = offer.getRequiredTechnologies().get(i);
                offer.removeRequiredTechnology(technology);
                technologyRepository.save(technology);
            }
            ArrayList<Technology> requiredTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getRequiredTechnologies());
            requiredTechnologies.forEach(element -> {
                offer.addRequiredTechnology(element);
                technologyRepository.save(element);
            });
        }

        offerRepository.save(offer);
        return offer.mapToOfferDtoShallow();

    }

    @Transactional
    public PageResponse<OfferDto> searchOffer(SearchOffersRequest searchOffersRequest, String jwt){
        Pageable pageable;
        if(searchOffersRequest.getSortingDirection().equals("dsc")){
            pageable = PageRequest.of(searchOffersRequest.getPageNumber(), searchOffersRequest.getNumberOfElements(), Sort.by(searchOffersRequest.getSortBy()).descending());
        }
        else{
            pageable = PageRequest.of(searchOffersRequest.getPageNumber(), searchOffersRequest.getNumberOfElements(), Sort.by(searchOffersRequest.getSortBy()).ascending());
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        Page<Offer> page = offerRepository.searchOffers(searchOffersRequest.getText(), searchOffersRequest.getJobLevels(), searchOffersRequest.getOperatingModes(), searchOffersRequest.getSpecializations(), searchOffersRequest.getLocalizations(), searchOffersRequest.getTechnologies(), currentDateTime, pageable);

        ArrayList<Offer> offers = new ArrayList<>(page.getContent());

        ArrayList<OfferDto> offersDto = new ArrayList<>();

        //Being logged in is optional
        if(jwt != null){
            User user = userService.findUserByJwt(jwt);
            offers.forEach(element -> {
                OfferDto offerDtoToAdd = element.mapToOfferDtoShallow();
                if(user.getLikedOffers().contains(element)){
                    offerDtoToAdd.setIsLiked(true);
                }
                else{
                    offerDtoToAdd.setIsLiked(false);
                }
                offersDto.add(offerDtoToAdd);
            });
        }
        else{
            offers.forEach(element -> {offersDto.add(element.mapToOfferDtoShallow());});
        }



        PageResponse<OfferDto> pageResponse = new PageResponse<OfferDto>(offersDto, page);
        return pageResponse;
    }

    public OfferDto getOffer(Long offerId, String jwt) throws OfferNotFoundByIdException {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);

        OfferDto offerDto = offer.mapToOfferDtoShallow();

        //Being logged in is optional
        if(jwt != null){
            User user = userService.findUserByJwt(jwt);

            if(user.getLikedOffers().contains(offer)){
                offerDto.setIsLiked(true);
            }
            else{
                offerDto.setIsLiked(false);
            }

            if(offer.getRecruiters().contains(user)){
                offerDto.setIsRecruiter(true);
            }
            else{
                offerDto.setIsRecruiter(false);
            }
        }
        return offerDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOfferById(Long id, String jwt) throws OfferNotFoundByIdException, NoPermissionException {
        Offer offer = offerRepository.findById(id).orElseThrow(OfferNotFoundByIdException::new);
        User userFromJwt = userService.findUserByJwt(jwt);

        //Validate permission
        validatePermissionDeleteOfferById(userFromJwt, offer);

        //Remove connections with users who liked offer
        for(int i = offer.getLikedByUsers().size()-1; i>=0; i--){
            User user = offer.getLikedByUsers().get(i);
            user.removeLikedOffer(offer);
            userRepository.save(user);
        }

        //Remove connections with required technologies
        for(int i = offer.getRequiredTechnologies().size()-1; i>=0; i--){
            Technology technology = offer.getRequiredTechnologies().get(i);
            offer.removeRequiredTechnology(technology);
            technologyRepository.save(technology);
        }

        //Remove connections with nice to have technologies
        for(int i = offer.getNiceToHaveTechnologies().size()-1; i>=0; i--){
            Technology technology = offer.getNiceToHaveTechnologies().get(i);
            offer.removeNiceToHaveTechnology(technology);
            technologyRepository.save(technology);
        }

        //Remove application from offer
        for(int i = offer.getApplications().size()-1; i>=0; i--){
            Application application = offer.getApplications().get(i);
            applicationService.deleteApplication(application);
        }

        //Remove recruiters from offer
        for (int i = offer.getRecruiters().size()-1; i>=0; i--){
            User recruiter = offer.getRecruiters().get(i);
            offer.removeRecruiter(recruiter);
            userRepository.save(recruiter);
        }

        offerRepository.delete(offer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void likeOffer(Long id, String jwt) throws OfferNotFoundByIdException, OfferIsAlreadyLikedByUserException, NoPermissionException {
        User user = userService.findUserByJwt(jwt);
        Offer offer = offerRepository.findById(id).orElseThrow(OfferNotFoundByIdException::new);

        if (user.getLikedOffers().contains(offer)){
            throw new OfferIsAlreadyLikedByUserException();
        }
        user.addLikedOffer(offer);
        userRepository.save(user);
        offerRepository.save(offer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeLikeOffer(Long id, String jwt) throws OfferNotFoundByIdException, OfferIsNotLikedByUserException, NoPermissionException {
        User user = userService.findUserByJwt(jwt);

        Offer offer = offerRepository.findById(id).orElseThrow(OfferNotFoundByIdException::new);

        if (!user.getLikedOffers().contains(offer)){
            throw new OfferIsNotLikedByUserException();
        }
        user.removeLikedOffer(offer);
        userRepository.save(user);
        offerRepository.save(offer);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addRecruiterToOffer(Long offerId, String recruiterEmail, String jwt) throws OfferNotFoundByIdException, UserIsAlreadyRecruiterException, NoPermissionException, UserNotFoundByEmailException {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);
        User recruiter = userRepository.findByEmail(recruiterEmail).orElseThrow(UserNotFoundByEmailException::new);
        User user = userService.findUserByJwt(jwt);

        validatePermissionAddRecruiterToOffer(user, offer);

        if (offer.getRecruiters().contains(recruiter)) throw new UserIsAlreadyRecruiterException();

        offer.addRecruiter(recruiter);
        userRepository.save(recruiter);
        offerRepository.save(offer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeRecruiterFromOffer(Long offerId, String recruiterEmail, String jwt) throws OfferNotFoundByIdException, UserIsNotRecruiterException, NoPermissionException, UserNotFoundByEmailException {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);
        User recruiter = userRepository.findByEmail(recruiterEmail).orElseThrow(UserNotFoundByEmailException::new);
        User user = userService.findUserByJwt(jwt);

        validatePermissionRemoveRecruiterFromOffer(user, offer, recruiter);

        if (!offer.getRecruiters().contains(recruiter)) throw new UserIsNotRecruiterException();

        offer.removeRecruiter(recruiter);
        userRepository.save(recruiter);
        offerRepository.save(offer);
    }

    public PageResponse<OfferDto> getLikedOffersFromJwt(Integer numberOfElements, Integer pageNumber, String jwt) {
        User user = userService.findUserByJwt(jwt);

        Pageable pageable = PageRequest.of(pageNumber, numberOfElements, Sort.by("expirationDate").ascending());
        LocalDateTime currentDateTime = LocalDateTime.now();
        Page<Offer> offersPage = offerRepository.getLikedOffersFromUser(user.getId(), currentDateTime, pageable);
        ArrayList<Offer> offers = new ArrayList<>(offersPage.getContent());

        ArrayList<OfferDto> offersDto = new ArrayList<>();
        offers.forEach(element -> {
            OfferDto offerDtoToAdd = element.mapToOfferDtoShallow();
            if(user.getLikedOffers().contains(element)){
                offerDtoToAdd.setIsLiked(true);
            }
            else{
                offerDtoToAdd.setIsLiked(false);
            }
            offersDto.add(offerDtoToAdd);
        });



        return new PageResponse<>(offersDto, offersPage);
    }

    public PageResponse<OfferDto> getOffersFromRecruiter(Long recruiterId, Boolean isActive, Integer numberOfElements, Integer pageNumber, String sortBy, String sortDirection, String jwt) throws UserNotFoundByIdException, NoPermissionException {
        User recruiter = userRepository.findById(recruiterId).orElseThrow(UserNotFoundByIdException::new);
        User user = userService.findUserByJwt(jwt);
        validatePermissionGetOffersFromRecruiter(user, recruiter);

        Pageable pageable;
        if(sortDirection.equals("asc")) pageable = PageRequest.of(pageNumber, numberOfElements, Sort.by(sortBy).ascending());
        else pageable = PageRequest.of(pageNumber, numberOfElements, Sort.by(sortBy).descending());

        LocalDateTime currentDateTime = LocalDateTime.now();
        Page<Offer> offersPage = offerRepository.getOffersFromRecruiter(recruiterId, isActive, currentDateTime, pageable);
        ArrayList<Offer> offers = new ArrayList<>(offersPage.getContent());

        ArrayList<OfferDto> offersDto = new ArrayList<>();
        offers.forEach(element -> {offersDto.add(element.mapToOfferDtoShallow_Recruiter());});

        return new PageResponse<>(offersDto, offersPage);
    }


    public void validatePermissionUpdateOffer(User user, Offer offer) throws NoPermissionException {
        if (user.getIsAdmin()){
            return;
        }
        else if (offer.getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }

    public void validatePermissionDeleteOfferById(User user, Offer offer) throws NoPermissionException {
        if (user.getIsAdmin()){
            return;
        }
        else if (offer.getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }


    public void validatePermissionAddRecruiterToOffer(User user, Offer offer) throws NoPermissionException {
        if (offer.getRecruiters().contains(user)){
            return;
        }
        throw new NoPermissionException();
    }

    public void validatePermissionRemoveRecruiterFromOffer(User user, Offer offer, User recruiter) throws NoPermissionException {
        if (offer.getRecruiters().contains(user)){
            if(!user.equals(recruiter)){
                return;
            }
        }
        throw new NoPermissionException();
    }


    public void validatePermissionGetOffersFromRecruiter(User user, User recruiter) throws NoPermissionException {
        if (user.getIsAdmin()){
            return;
        }
        else if (recruiter.equals(user)){
            return;
        }
        throw new NoPermissionException();
    }
}
