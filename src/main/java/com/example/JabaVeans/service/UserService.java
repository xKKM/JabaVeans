package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.User;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

public interface UserService {
    User findUser(String username);
    User saveUser(User user);

    Slice<User> findAllWhereRoleNotLike(int page, int size, String excludedRole);
    Slice<User> searchAllWhereRoleNotLikeLVL99(int page, int size, String searchStr);
}
