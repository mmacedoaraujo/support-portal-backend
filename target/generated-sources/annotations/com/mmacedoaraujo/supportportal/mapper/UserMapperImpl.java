package com.mmacedoaraujo.supportportal.mapper;

import com.mmacedoaraujo.supportportal.domain.User;
import java.util.Arrays;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-22T20:18:03-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_352 (Azul Systems, Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User updateUser(User user, User userToUpdate) {
        if ( user == null ) {
            return null;
        }

        if ( user.getId() != null ) {
            userToUpdate.setId( user.getId() );
        }
        if ( user.getUserId() != null ) {
            userToUpdate.setUserId( user.getUserId() );
        }
        if ( user.getFirstName() != null ) {
            userToUpdate.setFirstName( user.getFirstName() );
        }
        if ( user.getLastName() != null ) {
            userToUpdate.setLastName( user.getLastName() );
        }
        if ( user.getUsername() != null ) {
            userToUpdate.setUsername( user.getUsername() );
        }
        if ( user.getPassword() != null ) {
            userToUpdate.setPassword( user.getPassword() );
        }
        if ( user.getEmail() != null ) {
            userToUpdate.setEmail( user.getEmail() );
        }
        if ( user.getProfileImageUrl() != null ) {
            userToUpdate.setProfileImageUrl( user.getProfileImageUrl() );
        }
        if ( user.getLastLoginDate() != null ) {
            userToUpdate.setLastLoginDate( user.getLastLoginDate() );
        }
        if ( user.getLastLoginDateDisplay() != null ) {
            userToUpdate.setLastLoginDateDisplay( user.getLastLoginDateDisplay() );
        }
        if ( user.getJoinDate() != null ) {
            userToUpdate.setJoinDate( user.getJoinDate() );
        }
        if ( user.getRole() != null ) {
            userToUpdate.setRole( user.getRole() );
        }
        String[] authorities = user.getAuthorities();
        if ( authorities != null ) {
            userToUpdate.setAuthorities( Arrays.copyOf( authorities, authorities.length ) );
        }
        userToUpdate.setEnabled( user.isEnabled() );
        userToUpdate.setNonLocked( user.isNonLocked() );

        return userToUpdate;
    }
}
