package com.service.profile.exceptions;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String message){
        super(message);
    }
    public InvalidRequestException(){}
}
