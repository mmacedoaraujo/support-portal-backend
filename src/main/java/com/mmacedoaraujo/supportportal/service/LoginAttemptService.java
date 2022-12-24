package com.mmacedoaraujo.supportportal.service;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    public static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    public static final int ATTEMPT_INCREMENT = 1;
}
