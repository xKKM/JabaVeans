package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.User;
import com.example.JabaVeans.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User findUser(String username) {
        return userRepo.findById(username).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Slice<User> findAllWhereRoleNotLike(int page, int size, String excludedRole) {
        return userRepo.findAllByRoleNotLike(excludedRole, PageRequest.of(page, size));
    }

    @Override
    public Slice<User> searchAllWhereRoleNotLikeLVL99(int page, int size, String searchStr) {
        return userRepo.searchAll(searchStr, PageRequest.of(page, size));
    }
}
