package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.Order;
import org.springframework.data.domain.Slice;

import java.util.List;


public interface OrderService {
    Order save(Order order);

    Order findById(Integer order);

    void deleteById(Integer id);

    Slice<Order> findAllByDateOfOrder(int page, int size, String direction);

    Slice<Order> findAll(int page, int size, String searchStr, String direction);

}
