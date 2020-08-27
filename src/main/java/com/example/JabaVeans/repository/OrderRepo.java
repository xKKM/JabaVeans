package com.example.JabaVeans.repository;

import com.example.JabaVeans.dto.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends PagingAndSortingRepository<Order, Integer> {
    @Query(value = "select o from Order o where o.orderID = :id or o.email like %:email%")
    Slice<Order> findOrdersByEmailOrOrderID(@Param("email") String email, @Param("id") Integer id, Pageable pageable);
}
