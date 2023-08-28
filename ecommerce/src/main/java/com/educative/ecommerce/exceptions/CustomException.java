package com.educative.ecommerce.exceptions;

public class CustomException extends IllegalArgumentException{

    public CustomException(String message){
        super(message);
    }
}
