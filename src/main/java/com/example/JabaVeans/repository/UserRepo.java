package com.example.JabaVeans.repository;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends PagingAndSortingRepository<User, String> {
    User findUserByUsername(String username);

    @Query(value = "select u from User u where u.username like %:searchString% and u.role not like 'LVL99' ")
    Slice<User> searchAll(@Param("searchString") String str, Pageable pageable);
    Slice<User> findAllByRoleNotLike(String excludedRole, Pageable pageable);
}
