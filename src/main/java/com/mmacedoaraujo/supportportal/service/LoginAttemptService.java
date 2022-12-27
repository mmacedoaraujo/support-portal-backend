package com.mmacedoaraujo.supportportal.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mmacedoaraujo.supportportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    public static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    public static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> loginAttemptCache;
    private final UserRepository userRepository;

    public LoginAttemptService(UserRepository userRepository) {
        super();
        loginAttemptCache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
        this.userRepository = userRepository;
    }

    public void evictUserFromLoginAttemptCache(String username) {
        loginAttemptCache.invalidate(username);
    }

    public void addUserToLoginAttemptCache(String username) throws ExecutionException {
        int attempts = 0;
        attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
        loginAttemptCache.put(username, attempts);
    }

    public boolean exceededMaxAttempt(String username) throws ExecutionException {
        return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
    }
}


