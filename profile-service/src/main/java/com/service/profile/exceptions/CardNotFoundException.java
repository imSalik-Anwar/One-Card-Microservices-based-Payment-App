package com.service.profile.exceptions;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(String message){
        super(message);
    }
    public CardNotFoundException(){}
}
