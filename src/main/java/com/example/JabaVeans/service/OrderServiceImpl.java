package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.repository.OrderRepo;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;

    @Autowired
    public OrderServiceImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public Order save(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public Order findById(Integer order) {
        return orderRepo.findById(order).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        orderRepo.deleteById(id);
    }

    @Override
    public Slice<Order> findAllByDateOfOrder(int page, int size, String direction) {
        return orderRepo.findAll(PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.DESC), "dateOfOrder")));
    }

    @Override
    public Slice<Order> findAll(int page, int size, String searchStr, String direction) {
        return orderRepo.findOrdersByEmailOrOrderID(searchStr,
                NumberUtils.isParsable(searchStr) ? Integer.parseInt(searchStr) : null,
                PageRequest.of(page, size,
                        Sort.by(Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.DESC), "dateOfOrder")
                ));
    }

}
