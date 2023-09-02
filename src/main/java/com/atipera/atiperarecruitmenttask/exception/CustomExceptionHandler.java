package com.atipera.atiperarecruitmenttask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {GitHubUserNotFoundException.class})
    public ResponseEntity<Object> handleGitHubUserNotFoundException
             (GitHubUserNotFoundException gitHubUserNotFoundException) {
        CustomException customException = new CustomException(
                HttpStatus.NOT_FOUND.value(),
                gitHubUserNotFoundException.getMessage()
        );

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NotAcceptableTypeException.class})
    public ResponseEntity<Object> handleNotAcceptableTypeException
            (NotAcceptableTypeException notAcceptableTypeException) {
        CustomException customException = new CustomException(
                HttpStatus.NOT_ACCEPTABLE.value(),
                notAcceptableTypeException.getMessage()
        );

        return new ResponseEntity<>(customException, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException
            (BadRequestException badRequestException) {
        CustomException customException = new CustomException(
                HttpStatus.BAD_REQUEST.value(),
                badRequestException.getMessage()
        );

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Object> handleInternalServerErrorException
            (InternalServerErrorException internalServerErrorException) {
        CustomException customException = new CustomException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                internalServerErrorException.getMessage()
        );

        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
