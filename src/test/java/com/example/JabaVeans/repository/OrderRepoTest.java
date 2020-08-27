package com.example.JabaVeans.repository;

import com.example.JabaVeans.dto.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = {
        "logging.level.ROOT= WARN",
        "logging.level.org.springframework.test.context.transaction= INFO",
        "logging.level.org.hibernate.SQL= DEBUG",
        "logging.pattern.console=%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"
})
class OrderRepoTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderRepo orderRepo;

    @Test
    void whenOrdersWithGivenEmailOrIDsPresent_returnsSliceWithThem() {
        Order order1 = new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1));
        Order order2 = new Order(2, "Andrzej", "Kowalski", "asd@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1));
        Order order3 = new Order(3, "Andrzej", "Kowalski", "asd@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1));
        entityManager.merge(order1);
        entityManager.merge(order2);
        entityManager.merge(order3);
        Slice<Order> slice = orderRepo.findOrdersByEmailOrOrderID("emial@email", 2, PageRequest.of(0, 3));
        System.out.println(orderRepo.findById(3));
        Assertions.assertEquals(slice.getContent().size(), 2);
    }
}