package com.example.healthyeatsuserservice.controllers;

import com.example.healthyeatsuserservice.controllers.responses.APIResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
        return new ResponseEntity<Object>("Incorrect username or password", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        APIResponse response = new APIResponse(false,"Incorrect username or password" , exception.getMessage());
        return new ResponseEntity<Object>(response, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        APIResponse response = new APIResponse(false,"You do not have permission to perform that action" , exception.getMessage());
        return new ResponseEntity<Object>(
               response , new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleFallThroughException(Exception exception, WebRequest request) {
        APIResponse response = new APIResponse(false, exception.getMessage());
        return new ResponseEntity<Object>(response, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
}
