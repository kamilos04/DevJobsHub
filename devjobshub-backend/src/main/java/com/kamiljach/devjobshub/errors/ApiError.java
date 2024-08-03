package com.kamiljach.devjobshub.errors;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data

public class ApiError {
    private HttpStatus status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    public ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this.status = status;
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        timestamp = LocalDateTime.now();
        this.message = message;
    }
    public ApiError(HttpStatus status, String message, Throwable e) {
        this.status = status;
        timestamp = LocalDateTime.now();
        this.message = message;
        this.debugMessage = e.getLocalizedMessage();
    }

}
