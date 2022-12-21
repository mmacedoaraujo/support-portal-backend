package com.mmacedoaraujo.supportportal.resource;

import com.mmacedoaraujo.supportportal.exception.ExceptionHandling;
import com.mmacedoaraujo.supportportal.exception.domain.UserNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/", "/users"})
public class UserResource extends ExceptionHandling {

    @GetMapping("/home")
    public String showUser() throws UserNotFoundException {
        throw new UserNotFoundException("The user is not found");
    }
}
