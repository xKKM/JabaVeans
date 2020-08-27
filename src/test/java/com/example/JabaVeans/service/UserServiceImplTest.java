package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.User;
import com.example.JabaVeans.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Test
    void findUser_whenUserNotFound_shouldReturnNull() {
        when(userRepo.findById("user")).thenReturn(Optional.empty());
        assertNull(userService.findUser("user"));
        verify(userRepo,times(1)).findById("user");
    }

    @Test
    void findUser_whenUserFound_shouldReturnUser() {
        User user = new User("user","user",true,"LVL1");
        when(userRepo.findById(user.getUsername())).thenReturn(Optional.of(user));
        assertEquals(user ,userService.findUser(user.getUsername()));
        verify(userRepo,times(1)).findById(user.getUsername());
    }

    @Test
    void saveUser_shouldReturnUser() {
        User user = new User("user","user",true,"LVL1");
        when(userRepo.save(user)).thenAnswer(i -> i.getArguments()[0]);

        assertEquals(user, userService.saveUser(user));

        verify(userRepo, times(1)).save(user);
    }

    @Test
    void findAllWhereRoleNotLike_shouldReturnSliceWithUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User("user","user",true,"LVL1"));

        when(userRepo.findAllByRoleNotLike("LVL99",PageRequest.of(0, 10))).thenReturn(new PageImpl<>(list));

        Slice<User> slice = userService.findAllWhereRoleNotLike(0, 10, "LVL99");
        verify(userRepo, times(1)).findAllByRoleNotLike(eq("LVL99"),any(PageRequest.class));
        assertEquals(1, slice.getContent().size());
    }

    @Test
    void searchAllWhereRoleNotLikeLVL99_shouldReturnSliceWithUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User("user","user",true,"LVL1"));

        when(userRepo.searchAll("us",PageRequest.of(0, 10))).thenReturn(new PageImpl<>(list));

        Slice<User> slice = userService.searchAllWhereRoleNotLikeLVL99(0, 10, "us");
        verify(userRepo, times(1)).searchAll(eq("us"),any(PageRequest.class));
        assertEquals(1, slice.getContent().size());

    }
}