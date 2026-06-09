package com.github.lkjin41.tasksmanagement.tasks.controller;

import com.github.lkjin41.tasksmanagement.tasks.controller.dto.error.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(
            Exception e
    ) {
        log.info("handle exception: {}", e.getMessage());

        ErrorResponseDto errorToResponse = new ErrorResponseDto(
                "internal server error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorToResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(
            EntityNotFoundException e
    ){
        log.info("handle EntityNotFoundException: {}", e.getMessage());

        ErrorResponseDto errorToResponse = new ErrorResponseDto(
                "couldn't find task by id",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorToResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        log.info("handle IllegalArgumentException: {}", e.getMessage());

        ErrorResponseDto errorToResponse = new ErrorResponseDto(
                "bad argument in request",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorToResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(
            IllegalStateException e
    ) {
        log.info("handle IllegalStateException: {}", e.getMessage());

        ErrorResponseDto errorToResponse = new ErrorResponseDto(
                "bad request",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorToResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        log.info("handle MethodArgumentNotValidException: {}", e.getMessage());

        ErrorResponseDto errorToResponse = new ErrorResponseDto(
                "failed on request validation",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorToResponse);
    }

}
