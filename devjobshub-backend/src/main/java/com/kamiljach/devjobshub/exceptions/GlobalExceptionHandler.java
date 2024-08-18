package com.kamiljach.devjobshub.exceptions;

import com.kamiljach.devjobshub.errors.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAccountAlreadyExistsException(AccountAlreadyExistsException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Account already exists");
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserNotFoundByJwtException.class)
    public ResponseEntity<ApiError> handleInvalidJwtExceptionException(UserNotFoundByJwtException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "User not found, invalid jwt");
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(OfferNotFoundByIdException.class)
    public ResponseEntity<ApiError> handleOfferNotFoundByIdException(OfferNotFoundByIdException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Offer not found, invalid ID");
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(TechnologyWithThisNameAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleTechnologyWithThisNameAlreadyExistsException(TechnologyWithThisNameAlreadyExistsException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Technology with this name already exists");
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }
}
