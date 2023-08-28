package com.educative.ecommerce.exceptions;

public class AuthenticationFailException extends IllegalArgumentException{
    public AuthenticationFailException(String mgs){
        super(mgs);
    }
}
