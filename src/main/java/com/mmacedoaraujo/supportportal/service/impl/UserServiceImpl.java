package com.mmacedoaraujo.supportportal.service.impl;

import com.mmacedoaraujo.supportportal.domain.User;
import com.mmacedoaraujo.supportportal.domain.UserPrincipal;
import com.mmacedoaraujo.supportportal.exception.domain.EmailExistException;
import com.mmacedoaraujo.supportportal.exception.domain.UserNotFoundException;
import com.mmacedoaraujo.supportportal.exception.domain.UsernameExistException;
import com.mmacedoaraujo.supportportal.repository.UserRepository;
import com.mmacedoaraujo.supportportal.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            log.error("User not found by username: " + username);
            throw new UsernameNotFoundException("User not found by username: " + username);
        } else {
            user.setLasLoginDate(user.getLasLoginDate());
            user.setLasLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info("Returning found user by username: " + username);
            return userPrincipal;
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        return null;
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException("No user found by username " + currentUsername);
            }

            User userByUsername = findUserByUsername(newUsername);
            if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {
                throw new UsernameExistException("Username already exists");
            }

            User userByEmail = findUserByEmail(newEmail);
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                throw new EmailExistException("Email already exists");
            }

            return currentUser;
        } else {
            User userByUsername = findUserByUsername(newUsername);
            if (userByUsername != null) {
                throw new UsernameExistException("Username already exists");
            }

            User userByEmail = findUserByEmail(newEmail);
            if (userByEmail != null) {
                throw new UsernameExistException("Email already exists");
            }

            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}
