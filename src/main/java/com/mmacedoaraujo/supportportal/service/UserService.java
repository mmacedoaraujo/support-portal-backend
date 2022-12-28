package com.mmacedoaraujo.supportportal.service;

import com.mmacedoaraujo.supportportal.domain.User;
import com.mmacedoaraujo.supportportal.exception.domain.EmailExistException;
import com.mmacedoaraujo.supportportal.exception.domain.UserNotFoundException;
import com.mmacedoaraujo.supportportal.exception.domain.UsernameExistException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email, String password) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;

    List<User> getUsers();

    User findByUsername(String username);

    User findByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage);

    User updateUser(String currentUsername, String newFirstName, String newLastName, String username, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage);

    void deleteUser(Long id);

    void resetPassword(String email);

    User updateProfileImage(String username, MultipartFile profileImage);
}
