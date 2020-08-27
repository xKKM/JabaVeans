package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.User;
import com.example.JabaVeans.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserDetailsServiceImplTest {

    @MockBean
    UserRepo userRepo;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWithFoundUser() {
        User user = new User("qqq","www",true,"LVL11");
        when(userRepo.findUserByUsername("qqq")).thenReturn(user);
        UserDetails userDetails= userDetailsService.loadUserByUsername(user.getUsername());
        verify(userRepo, times(1)).findUserByUsername(user.getUsername());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

    }
}