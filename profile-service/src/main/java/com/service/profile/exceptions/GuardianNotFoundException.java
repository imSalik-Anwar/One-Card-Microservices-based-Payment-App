package com.service.profile.exceptions;

public class GuardianNotFoundException extends RuntimeException{
    public GuardianNotFoundException(String message){
        super(message);
    }
    public GuardianNotFoundException(){}
}
