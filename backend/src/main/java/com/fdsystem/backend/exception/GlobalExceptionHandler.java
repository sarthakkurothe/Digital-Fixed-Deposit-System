package com.fdsystem.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UsernameNotFoundException.class)
  public Map<String, Object> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException){
    Map<String, Object> map = new HashMap<>();
    map.put("status", 404);
    map.put("error", "Bad Credentials");
    map.put("message", usernameNotFoundException.getMessage());
    return map;
  }
}
