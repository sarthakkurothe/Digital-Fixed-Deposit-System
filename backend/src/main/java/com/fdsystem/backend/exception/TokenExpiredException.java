package com.fdsystem.backend.exception;

public class TokenExpiredException extends Exception{
  public TokenExpiredException(String message){
    super(message);
  }
}
