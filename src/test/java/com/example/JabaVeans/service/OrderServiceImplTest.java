package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.repository.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {

    @MockBean
    OrderRepo orderRepo;

    @Autowired
    OrderService orderService;


    @Test
    void save_shouldReturnOrder() {
        Order order = new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1));
        when(orderRepo.save(order)).thenAnswer(i -> i.getArguments()[0]);

        Order orderC = orderService.save(order);

        verify(orderRepo, times(1)).save(order);
        assertEquals(order.toString(), orderC.toString());


    }

    @Test
    void findById_whenOrderNotFound_shouldReturnNull() {
        when(orderRepo.findById(1)).thenReturn(Optional.empty());

        Order orderC = orderService.findById(1);

        verify(orderRepo, times(1)).findById(1);
        assertNull(orderC);
    }

    @Test
    void findById_whenOrderFound_shouldReturnOrder() {
        Order order = new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1));
        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        Order orderC = orderService.findById(1);

        verify(orderRepo, times(1)).findById(1);
        assertEquals(order.toString(), orderC.toString());
    }

    @Test
    void deleteById_shouldCallOrderRepoDeleteById() {
        orderService.deleteById(1);
        verify(orderRepo, times(1)).deleteById(1);
    }

    @Test
    void findAllByDateOfOrder_shouldReturnSliceWithOrders() {
        List<Order> list = new ArrayList<>();
        list.add(new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1)));
        when(orderRepo.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "dateOfOrder"))).thenReturn(new PageImpl<>(list));

        Slice<Order> slice = orderService.findAllByDateOfOrder(0, 10, "DESC");
        verify(orderRepo, times(1)).findAll(any(PageRequest.class));
        assertEquals(1, slice.getContent().size());

    }

    @Test
    void findAll_shouldReturnSliceWithOrders() {
        List<Order> list = new ArrayList<>();

        list.add(new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1)));
        when(orderRepo.findOrdersByEmailOrOrderID("1", 1, PageRequest.of(0, 10, Sort.Direction.DESC, "dateOfOrder"))).thenReturn(new PageImpl<>(list));

        Slice<Order> slice = orderService.findAll(0, 10, "1", "DESC");
        verify(orderRepo, times(1)).findOrdersByEmailOrOrderID(eq("1"), eq(1), any(PageRequest.class));
        assertEquals(1, slice.getContent().size());
    }

}