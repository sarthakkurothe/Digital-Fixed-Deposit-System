package com.fdsystem.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UsernameNotFoundException.class)
  public Map<String, String> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException){
    Map<String, String> map = new HashMap<>();
    map.put("status", String.valueOf(HttpStatus.NOT_FOUND));
    map.put("error", "Bad Credentials");
    map.put("message", usernameNotFoundException.getMessage());
    return map;
  }

  @ExceptionHandler(InterestCalculationException.class)
  public Map<String, String> handleFixedDepositOperationException(InterestCalculationException ex){
    Map<String, String> map = new HashMap<>();
    map.put("status", String.valueOf(HttpStatus.BAD_REQUEST));
    map.put("error", "Fixed Deposit Operation Error");
    map.put("message", ex.getMessage());
    return map;
  }
}
