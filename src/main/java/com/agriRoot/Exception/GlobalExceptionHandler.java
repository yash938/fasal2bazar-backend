package com.agriRoot.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<AllException> userNotFoundException(UsernameNotFoundException ex){
        AllException userIsNotFound = AllException.builder().message("User is not found").status(HttpStatus.NOT_FOUND).success(false).localDate(LocalDate.now()).build();
        return new ResponseEntity<>(userIsNotFound,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleApiException(MethodArgumentNotValidException ex){
        Map<String,String> errors  = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError) error).getField();
            String errorName =error.getDefaultMessage();
            errors.put(fieldName,errorName);
        });

        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
