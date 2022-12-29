package com.mmacedoaraujo.supportportal.service.impl;

import com.mmacedoaraujo.supportportal.domain.User;
import com.mmacedoaraujo.supportportal.domain.UserPrincipal;
import com.mmacedoaraujo.supportportal.exception.domain.EmailExistException;
import com.mmacedoaraujo.supportportal.exception.domain.EmailNotFoundException;
import com.mmacedoaraujo.supportportal.exception.domain.UserNotFoundException;
import com.mmacedoaraujo.supportportal.exception.domain.UsernameExistException;
import com.mmacedoaraujo.supportportal.repository.UserRepository;
import com.mmacedoaraujo.supportportal.service.EmailService;
import com.mmacedoaraujo.supportportal.service.LoginAttemptService;
import com.mmacedoaraujo.supportportal.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

import static com.mmacedoaraujo.supportportal.constant.FileConstant.DEFAULT_USER_IMAGE_PATH;
import static com.mmacedoaraujo.supportportal.constant.UserServiceImplConstant.*;
import static com.mmacedoaraujo.supportportal.enumeration.Role.ROLE_USER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final EmailService emailService;
    private final LoginAttemptService loginAttemptService;

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error(USER_NOT_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDate(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info("Returning found user by username: " + username);
            return userPrincipal;
        }
    }


    @Override
    public User register(String firstName, String lastName, String username, String email, String password) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = User.builder().userId(generateUserId())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .email(email)
                .joinDate(new Date())
                .password(encodePassword(password))
                .isEnabled(true)
                .isNonLocked(true)
                .role(ROLE_USER.name())
                .authorities(ROLE_USER.getAuthorities())
                .profileImageUrl(getTemporaryProfileImageUrl(username))
                .build();
        userRepository.save(user);
        emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String password, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = User.builder().userId(generateUserId())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .email(email)
                .joinDate(new Date())
                .password(encodePassword(password))
                .isEnabled(true)
                .isNonLocked(true)
                .role(getRoleEnumName(role).name())
                .authorities(getRoleEnumName(role).getAuthorities)
                .profileImageUrl(getTemporaryProfileImageUrl(username))
                .build();

        userRepository.save(user);
        saveProfileImage(user, profileImage);
        return user;
    }


    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String username, String password, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User currentUser = validateNewUsernameAndEmail(currentUsername, username, newEmail);
        currentUser = User.builder()
                .firstName(newFirstName)
                .lastName(newLastName)
                .joinDate(new Date())
                .username(username)
                .email(newEmail)
                .isEnabled(true)
                .isNonLocked(true)
                .role(getRoleEnumName(role).name())
                .build();
        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);
        return currentUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);

    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException {
        User userByEmail = userRepository.findByEmail(email);
        if (userByEmail == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String newPassword = encodePassword(generatePassword());
        userByEmail.setPassword(newPassword);
        userRepository.save(userByEmail);
        emailService.sendNewPasswordEmail(userByEmail.getFirstName(), newPassword, userByEmail.getEmail());

    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(User user, profileImage);
        return user;
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private void saveProfileImage(User user, MultipartFile profileImage) {
    }

    private Role getRoleEnumName(String role) {
    }

    private String encodePassword(String password) {
        return encoder().encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private void validateLoginAttempt(User user) {
        if (user.isNonLocked()) {
            if (loginAttemptService.hasExceededMaxAttempt(user.getUsername())) {
                user.setNonLocked(false);
            } else {
                user.setNonLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User currentUser = findByUsername(currentUsername);
        User userByNewUsername = findByUsername(newUsername);
        User userByNewEmail = findByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            if (currentUser == null) {
                throw new UserNotFoundException(USER_NOT_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new UsernameExistException(EMAIL_ALREADY_EXISTS);
            }

            return null;
        }
    }
}
