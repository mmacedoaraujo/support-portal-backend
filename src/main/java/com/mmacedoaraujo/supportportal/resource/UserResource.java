package com.mmacedoaraujo.supportportal.resource;

import com.mmacedoaraujo.supportportal.exception.domain.ExceptionHandling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserResource extends ExceptionHandling {

    @GetMapping("/home")
    public String showUser() {
        return "application works";
    }
}
