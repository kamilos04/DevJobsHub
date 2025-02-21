package com.kamiljach.devjobshub.exceptions;

import com.kamiljach.devjobshub.response.ApiError;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAccountAlreadyExistsException(AccountAlreadyExistsException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserNotFoundByJwtException.class)
    public ResponseEntity<ApiError> handleInvalidJwtExceptionException(UserNotFoundByJwtException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(OfferNotFoundByIdException.class)
    public ResponseEntity<ApiError> handleOfferNotFoundByIdException(OfferNotFoundByIdException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(TechnologyWithThisNameAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleTechnologyWithThisNameAlreadyExistsException(TechnologyWithThisNameAlreadyExistsException ex){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(TechnologyNotFoundByNameException.class)
    public ResponseEntity<ApiError> handleTechnologyNotFoundByNameException(TechnologyNotFoundByNameException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(TechnologyNotFoundByIdException.class)
    public ResponseEntity<ApiError> handleTechnologyNotFoundByIdException(TechnologyNotFoundByIdException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ApplicationNotFoundByIdException.class)
    public ResponseEntity<ApiError> handleApplicationNotFoundByIdException(ApplicationNotFoundByIdException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(QuestionOrAnswerIsIncorrectException.class)
    public ResponseEntity<ApiError> handleQuestionOrAnswerIsIncorrectException(QuestionOrAnswerIsIncorrectException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(OfferIsAlreadyLikedByUserException.class)
    public ResponseEntity<ApiError> handleOfferIsAlreadyLikedByUserException(OfferIsAlreadyLikedByUserException ex){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(OfferIsNotLikedByUserException.class)
    public ResponseEntity<ApiError> handleOfferIsNotLikedByUserException(OfferIsNotLikedByUserException ex){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ApplicationAlreadyIsInFavouritesException.class)
    public ResponseEntity<ApiError> handleApplicationAlreadyIsInFavouritesException(ApplicationAlreadyIsInFavouritesException ex){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ApplicationIsNotInFavouritesException.class)
    public ResponseEntity<ApiError> handleApplicationIsNotInFavouritesException(ApplicationIsNotInFavouritesException ex){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(OfferExpiredException.class)
    public ResponseEntity<ApiError> handleOfferExpiredException(OfferExpiredException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<ApiError> handleUserNotFoundByIdException(UserNotFoundByIdException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserIsAlreadyRecruiterException.class)
    public ResponseEntity<ApiError> handleUserIsAlreadyRecruiterException(UserIsAlreadyRecruiterException ex){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserIsNotRecruiterException.class)
    public ResponseEntity<ApiError> handleUserIsNotRecruiterException(UserIsNotRecruiterException ex){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,"Invalid username or password", ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequestException(InvalidRequestException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    @ExceptionHandler(UserNotFoundByEmailException.class)
    public ResponseEntity<ApiError> handleUserNotFoundByEmailException(UserNotFoundByEmailException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(JwtIsBlockedException.class)
    public ResponseEntity<ApiError> handleJwtIsOnBlackListException(JwtIsBlockedException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(FirmAccountCanNotDoThatException.class)
    public ResponseEntity<ApiError> handleFirmAccountCanNotDoThatException(FirmAccountCanNotDoThatException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(NoFirmAccountCanNotDoThatException.class)
    public ResponseEntity<ApiError> handleNotFirmAccountCanNotDoThatException(NoFirmAccountCanNotDoThatException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserAlreadyAppliedForThisOfferException.class)
    public ResponseEntity<ApiError> handleUserAlreadyAppliedForThisOfferException(UserAlreadyAppliedForThisOfferException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ApiError> handleNoPermissionException(NoPermissionException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
