package com.fdsystem.backend.exception;

public class CustomerNotFoundException extends Exception{
  public CustomerNotFoundException(String message){
    super(message);
  }
}
