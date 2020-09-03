package com.example.JabaVeans.config;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.User;
import com.example.JabaVeans.repository.OrderRepo;
import com.example.JabaVeans.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class DemoConfig {

    private static UserRepo userRepo;
    private static OrderRepo orderRepo;
    private static PasswordEncoder passwordEncoder;

    public DemoConfig(@Value("${demo.testing:no}") String enabled, UserRepo userRepo, OrderRepo orderRepo, PasswordEncoder passwordEncoder) {
        if (enabled.equals("enabled")) {
            DemoConfig.userRepo = userRepo;
            DemoConfig.orderRepo = orderRepo;
            DemoConfig.passwordEncoder = passwordEncoder;
            addExampleUsers();
            addExampleOrders();
        }
    }



    private static void addExampleUsers() {
        List<User> users = new LinkedList<>();
        users.add(new User("qwer", passwordEncoder.encode("qwer"), true, "LVL99"));
        users.add(new User("mariusz", passwordEncoder.encode("mariusz"), true, "LVL1"));
        users.add(new User("pawel", passwordEncoder.encode("pawel"), true, "LVL1"));
        users.add(new User("szeryf", passwordEncoder.encode("szeryf"), true, "LVL30"));
        userRepo.saveAll(users);

    }

    private static void addExampleOrders() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Order> orders = Arrays.asList(objectMapper.readValue(Paths.get("orders.json").toFile(), Order[].class));
            orderRepo.saveAll(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
