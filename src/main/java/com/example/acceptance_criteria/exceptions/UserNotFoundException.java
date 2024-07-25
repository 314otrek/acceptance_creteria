package com.example.acceptance_criteria.exceptions;

import javax.management.remote.JMXServerErrorException;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}
