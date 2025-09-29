package com.fdsystem.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
    Map<String, Object> map = new HashMap<>();
    map.put("status", HttpStatus.NOT_FOUND); // 401
    map.put("error", "Bad Credentials");
    map.put("message", ex.getMessage());
    return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(InterestCalculationException.class)
  public ResponseEntity<Map<String, String>> handleInterestCalculationException(InterestCalculationException ex) {
    Map<String, String> map = new HashMap<>();
    map.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value())); // 400
    map.put("error", "Fixed Deposit Operation Error");
    map.put("message", ex.getMessage());
    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<Map<String, String>> handleTokenExpiredException(TokenExpiredException ex) {
    Map<String, String> map = new HashMap<>();
    map.put("status", String.valueOf(HttpStatus.UNAUTHORIZED.value())); // 401
    map.put("error", "Token Validation Error");
    map.put("message", ex.getMessage());
    return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
  }


  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
    Map<String, String> map = new HashMap<>();
    map.put("status", String.valueOf(HttpStatus.UNAUTHORIZED.value())); // 401
    map.put("error", "Token Validation Error");
    map.put("message", ex.getMessage());
    return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
  }

  // -------------------- Optional: Catch-all for other exceptions --------------------
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    Map<String, String> map = new HashMap<>();
    map.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())); // 500
    map.put("error", "Internal Server Error");
    map.put("message", ex.getMessage());
    return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
