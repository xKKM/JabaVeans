package com.example.JabaVeans.controller;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.helper.ProductPrices;
import com.example.JabaVeans.service.EmailService;
import com.example.JabaVeans.service.OrderService;
import com.example.JabaVeans.viewhelper.TablePage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;
import utils.ObjectConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(authorities = {"LVL99"})
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private EmailService emailService;

    @Test
    void ordersGet_shouldRender_And_ModelContainsListFromFirstSliceWithAllAvailableOrders() throws Exception {
        TablePage tablePage = new TablePage();
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1)));
        Slice<Order> slice = new SliceImpl<>(orders);
        when(orderService.findAllByDateOfOrder(tablePage.getCurrentPage(), tablePage.getSize(), "DESC")).thenReturn(slice);

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("tablePage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
                .andExpect(MockMvcResultMatchers.model().attribute("orders", slice.getContent()))
                .andReturn().getModelAndView();

        verify(orderService, times(1)).findAllByDateOfOrder(tablePage.getCurrentPage(), tablePage.getSize(), "DESC");

        assertNotNull(mv);
        assertEquals(1, ((List<Order>) mv.getModel().get("orders")).size());

        tablePage = (TablePage) mv.getModel().get("tablePage");
        assertEquals(0, tablePage.getCurrentPage());
        assertFalse(tablePage.isHasNext());
        assertFalse(tablePage.isHasPrev());

    }

    @Test
    void ordersPost_whenSearchStrIsBlank_shouldRender_And_ModelContainsListFromSliceOfOrders() throws Exception {
        TablePage tablePage = new TablePage();
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1)));
        Slice<Order> slice = new SliceImpl<>(orders);
        when(orderService.findAllByDateOfOrder(tablePage.getCurrentPage(), tablePage.getSize(), "DESC")).thenReturn(slice);

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(ObjectConverter.toParams(tablePage, objectMapper))
                .param("sort", "DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("tablePage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
                .andExpect(MockMvcResultMatchers.model().attribute("orders", slice.getContent()))
                .andReturn().getModelAndView();

        verify(orderService, times(1)).findAllByDateOfOrder(tablePage.getCurrentPage(), tablePage.getSize(), "DESC");

        assertNotNull(mv);
        assertEquals(1, ((List<Order>) mv.getModel().get("orders")).size());

        tablePage = (TablePage) mv.getModel().get("tablePage");
        assertEquals(0, tablePage.getCurrentPage());
        assertFalse(tablePage.isHasNext());
        assertFalse(tablePage.isHasPrev());

    }

    @Test
    void ordersPost_whenSearchStrIsNotBlank_shouldRender_And_ModelContainsListFromSliceOfOrders() throws Exception {
        TablePage tablePage = new TablePage();
        tablePage.setSearchStr("email");
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1)));
        Slice<Order> slice = new SliceImpl<>(orders);
        when(orderService.findAll(anyInt(), anyInt(), eq("email"), eq("DESC"))).thenReturn(slice);

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(ObjectConverter.toParams(tablePage, objectMapper))
                .param("sort", "DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("tablePage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
                .andExpect(MockMvcResultMatchers.model().attribute("orders", slice.getContent()))
                .andReturn().getModelAndView();

        verify(orderService, times(1))
                .findAll(tablePage.getCurrentPage(), tablePage.getSize(), tablePage.getSearchStr(), "DESC");

        assertNotNull(mv);
        assertEquals(1, ((List<Order>) mv.getModel().get("orders")).size());

        tablePage = (TablePage) mv.getModel().get("tablePage");
        assertFalse(tablePage.isHasNext());
        assertFalse(tablePage.isHasPrev());

    }

    @Test
    void ordersRedirectPost_shouldRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/r/orders")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/orders"));
    }

    @Test
    void orderDetailsGet_whenOrderIdCantBeParsed_ModelShouldNotContainOrder_And_AttributeGettingOrderErrorIsTrue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/details/asdf")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("order"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("gettingOrderError"))
                .andExpect(MockMvcResultMatchers.model().attribute("gettingOrderError", true))
                .andExpect(MockMvcResultMatchers.view().name("order-details"));
    }

    @Test
    void orderDetailsGet_whenOrderCantBeFound_ModelShouldNotContainOrder_And_AttributeGettingOrderErrorIsTrue() throws Exception {
        when(orderService.findById(1)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/details/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("order"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("gettingOrderError"))
                .andExpect(MockMvcResultMatchers.model().attribute("gettingOrderError", true))
                .andExpect(MockMvcResultMatchers.view().name("order-details"));
        verify(orderService, times(1)).findById(1);
    }

    @Test
    void orderDetailsGet_whenOrderCanBeFound_ModelShouldContainOrder_And_AttributeGettingOrderErrorIsFalse() throws Exception {
        Order order = new Order();
        when(orderService.findById(1)).thenReturn(order);
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/details/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
                .andExpect(MockMvcResultMatchers.model().attribute("order", order))
                .andExpect(MockMvcResultMatchers.model().attributeExists("gettingOrderError"))
                .andExpect(MockMvcResultMatchers.model().attribute("gettingOrderError", false))
                .andExpect(MockMvcResultMatchers.view().name("order-details"));
        verify(orderService, times(1)).findById(1);
    }

    @Test
    void orderSaveStatePost_whenOrderIsNotValid_shouldRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/save-state")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("firstName", "")
                .param("surname", "")
                .param("email", "")
                .param("address", "")
                .param("weight", String.valueOf(2))
                .param("quantity", String.valueOf(3))
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("order"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/orders"));
    }

    @Test
    void orderSaveStatePost_whenOrderIsValid_shouldSaveChanges() throws Exception {
        Order order = new Order(1, "qqq", "www", "qqq@example", "The address",
                (byte) 2, (byte) 3, new Date(), (byte) 0, BigDecimal.valueOf(0));
        when(orderService.save(any(Order.class))).thenReturn(order);
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/save-state")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("orderID", "1")
                .param("firstName", "qqq")
                .param("surname", "www")
                .param("email", "qqq@example")
                .param("address", "The address")
                .param("weight", "1")
                .param("quantity", "1")
                .param("dateOfOrder", "2020/08/18")
                .param("totalPrice", "1")
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/orders/details/" + order.getOrderID()));

        verify(emailService, times(1)).sendOrderStateChange(any(Order.class));
        verify(orderService, times(1)).save(any(Order.class));
    }

    @Test
    void orderAddGet_shouldRender_And_ModelContainsOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
                .andExpect(MockMvcResultMatchers.view().name("back-order-page"));
    }

    @Test
    void orderAddPost_whenOrderNotValid_shouldReturnSameViewNameAndModelOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("orderID", "1")
                .param("firstName", "qqq")
                .param("surname", "www")
                .param("email", "")
                .param("address", "")
                .param("weight", "1")
                .param("quantity", "1")
                .param("dateOfOrder", "2020/08/18")
                .param("totalPrice", "1")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
                .andExpect(MockMvcResultMatchers.view().name("back-order-page"));
    }

    @Test
    void orderAddPost_whenOrderIsValid_shouldSaveOrderAndRedirects() throws Exception {

        when(orderService.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/orders/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("firstName", "Qwerty")
                .param("surname", "Asdf")
                .param("email", "asdf@example.com")
                .param("address", "The address")
                .param("weight", String.valueOf(2))
                .param("quantity", String.valueOf(3))
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/orders"))
                .andReturn().getModelAndView();


        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderService, times(1)).save(captor.capture());
        Order savedOrder = captor.getValue();

        verify(emailService, times(1)).sendOrderConfirmation(savedOrder);
        assertEquals(0, savedOrder.getOrderState());
        assertNotNull(savedOrder.getDateOfOrder());
        assertEquals(BigDecimal.valueOf(savedOrder.getQuantity()).multiply(ProductPrices.getPrice(savedOrder.getWeight())), savedOrder.getTotalPrice());

    }
}