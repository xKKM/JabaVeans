package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    User user;

    UserDetailsImpl userDetails;

    public UserDetailsImplTest() {
        user = new User("user","qwerty",true,"LVL22");
        userDetails = new UserDetailsImpl(user);
    }

    @Test
    void getAuthorities_shouldReturnSingleAuthority() {
        assertFalse(userDetails.getAuthorities().isEmpty());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("LVL22",userDetails.getAuthorities().toArray()[0].toString());
    }

    @Test
    void getPassword_shouldReturnPassword() {
        assertEquals("qwerty", userDetails.getPassword());
    }

    @Test
    void getUsername_shouldReturnUserName() {
        assertEquals("user", userDetails.getUsername());
    }

    @Test
    void isAccountNonExpired_shouldReturnTrue() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_shouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_shouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_whenUserEnabled_shouldReturnTrue() {
        User otherUser = new User("qqqq","qwerty",true,"LVL22");
        UserDetails otherDetails = new UserDetailsImpl(otherUser);
        assertEquals(otherUser.isEnabled(), otherDetails.isEnabled());
        assertTrue(otherDetails.isEnabled());
    }

    @Test
    void isEnabled_whenUserDisabled_shouldReturnFalse() {
        User otherUser = new User("tttt","qwerty",false,"LVL22");
        UserDetails otherDetails = new UserDetailsImpl(otherUser);
        assertEquals(otherUser.isEnabled(), otherDetails.isEnabled());
        assertFalse(otherDetails.isEnabled());
    }

}