package com.example.JabaVeans.repository;

import com.example.JabaVeans.dto.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepoTest {

    @Autowired
    UserRepo userRepo;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void findUserByUsername_returnsUser() {
        entityManager.persist(new User("asd","asd",true,"LVL22"));
        User user = userRepo.findUserByUsername("asd");
        assertNotNull(user);
    }

    @Test
    void searchAll_returnsSliceWithAllSearchedUsersExcludedLVL99Role() {
        entityManager.persist(new User("asd","asd",true,"LVL22"));
        entityManager.persist(new User("wsx","qwe",false,"LVL77"));
        entityManager.persist(new User("qaz","zxc",true,"LVL99"));
        Slice<User> users = userRepo.searchAll("s", PageRequest.of(0,3));
        assertEquals(2,users.getContent().size());
    }

    @Test
    void findAllByRoleNotLike_returnsSliceWithAllUsersNotHavingSpecifiedRole() {
        entityManager.persist(new User("asd","asd",true,"LVL22"));
        entityManager.persist(new User("wsx","qwe",false,"LVL77"));
        Slice<User> users = userRepo.findAllByRoleNotLike("LVL77", PageRequest.of(0,3));
        assertEquals(1,users.getContent().size());
    }
}