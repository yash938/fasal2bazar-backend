package com.agriRoot.Exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFound extends UsernameNotFoundException {

    public UserNotFound(String message){
        super(message);
    }

    public UserNotFound(){
        super("User is not found");
    }
}
