package com.mmacedoaraujo.supportportal.resource;

import com.mmacedoaraujo.supportportal.domain.User;
import com.mmacedoaraujo.supportportal.domain.UserPrincipal;
import com.mmacedoaraujo.supportportal.exception.ExceptionHandling;
import com.mmacedoaraujo.supportportal.exception.domain.EmailExistException;
import com.mmacedoaraujo.supportportal.exception.domain.UserNotFoundException;
import com.mmacedoaraujo.supportportal.exception.domain.UsernameExistException;
import com.mmacedoaraujo.supportportal.repository.UserRepository;
import com.mmacedoaraujo.supportportal.service.UserService;
import com.mmacedoaraujo.supportportal.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mmacedoaraujo.supportportal.constant.SecurityConstant.JWT_TOKEN_HEADER;

@RestController
@RequestMapping(path = {"/", "/users"})
@AllArgsConstructor
public class UserResource extends ExceptionHandling {
    private final UserRepository userRepository;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<User> login(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
        authenticate(user.getUsername(), user.getPassword());
        User authenticatedUser = userService.findByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(authenticatedUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(authenticatedUser, jwtHeader, HttpStatus.CREATED);
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User registeredUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
