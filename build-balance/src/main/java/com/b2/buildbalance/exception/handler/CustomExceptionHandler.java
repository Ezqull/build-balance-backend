package com.b2.buildbalance.exception.handler;


import com.b2.buildbalance.exception.IllegalArgumentException;
import com.b2.buildbalance.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .status(404)
                        .message(ex.getMessage())
                        .build(),
                NOT_FOUND
        );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .status(400)
                        .message(ex.getMessage())
                        .build(),
                BAD_REQUEST
        );
    }
}
