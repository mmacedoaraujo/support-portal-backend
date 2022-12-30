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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.mmacedoaraujo.supportportal.constant.SecurityConstant.JWT_TOKEN_HEADER;

@RestController
@RequestMapping(path = {"/", "/users"})
@AllArgsConstructor
public class UserResource extends ExceptionHandling {
    private final UserRepository userRepository;

    private final UserService userService;
    private final AuthenticationManager userResourceAuthenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User authenticatedUser = userService.findByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(authenticatedUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(authenticatedUser, jwtHeader, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        User registeredUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isEnabled") String isEnabled,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {

        User user = userService.addNewUser(firstName, lastName, username, email, role,
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isEnabled), profileImage);

        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        userResourceAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
