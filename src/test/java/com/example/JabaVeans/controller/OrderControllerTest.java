package com.example.JabaVeans.controller;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.helper.ProductPrices;
import com.example.JabaVeans.service.EmailService;
import com.example.JabaVeans.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;
import utils.ObjectConverter;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private EmailService emailService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void orderGet_Renders_And_ContainsBlankOrder() throws Exception {

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.get("/order"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
                .andExpect(MockMvcResultMatchers.view().name("order-page"))
                .andReturn().getModelAndView();

        assertNotNull(mv);

        Order order = (Order) mv.getModel().get("order");
        assertEquals(order.toString(), new Order().toString());

    }

    @Test
    void orderPost_whenOrderNotValid_returnsSameOrder() throws Exception {
        Order order = new Order();
        order.setFirstName("Qwerty");


        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/order")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(ObjectConverter.toParams(order, objectMapper))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
                .andExpect(MockMvcResultMatchers.view().name("order-page"))
                .andReturn().getModelAndView();


        assertNotNull(mv);
        assertEquals(order.toString(), mv.getModel().get("order").toString());

    }

    @Test
    void orderPost_whenOrderIsValid_shouldSaveOrderWithUpdatedFields_And_SendEmail_And_Redirect() throws Exception {

        when(orderService.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/order")
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
                .andExpect(MockMvcResultMatchers.view().name("redirect:/order/order-success"))
                .andReturn().getModelAndView();


        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderService, times(1)).save(captor.capture());
        Order savedOrder = captor.getValue();

        verify(emailService, times(1)).sendOrderConfirmation(savedOrder);
        assertEquals(0, savedOrder.getOrderState());
        assertNotNull(savedOrder.getDateOfOrder());
        assertEquals(BigDecimal.valueOf(savedOrder.getQuantity()).multiply(ProductPrices.getPrice(savedOrder.getWeight())), savedOrder.getTotalPrice());
    }

    @Test
    void checkOrderGet_renders_And_modelContainsAttributes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/check-order")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderID"))
                .andExpect(MockMvcResultMatchers.model().attribute("orderID",""))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderIdError"))
                .andExpect(MockMvcResultMatchers.model().attribute("orderIdError",false))
                .andExpect(MockMvcResultMatchers.view().name("check-order"));


    }

    @Test
    void checkOrderPost_whenOrderIdNotParsable_shouldRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/order/check-order")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("orderID", "asdf")
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderIdError"))
                .andExpect(MockMvcResultMatchers.model().attribute("orderIdError",true))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/order/check-order"));

    }

    @Test
    void checkOrderPost_whenOrderNotFound_shouldRedirect() throws Exception {
        when(orderService.findById(anyInt())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/check-order")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("orderID", "2")
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderIdError"))
                .andExpect(MockMvcResultMatchers.model().attribute("orderIdError",true))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/order/check-order"));

    }

    @Test
    void checkOrderPost_whenOrderFound_shouldAddOrderToModel_AndRenders() throws Exception {
        Order order = new Order(1,"qqq","www","qqq@example","The address",
                (byte) 2, (byte) 3, new Date(), (byte) 0, BigDecimal.valueOf(0));
        when(orderService.findById(anyInt())).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/check-order")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("orderID", "2")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
                .andExpect(MockMvcResultMatchers.model().attribute("order",order))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderIdError"))
                .andExpect(MockMvcResultMatchers.model().attribute("orderIdError",false))
                .andExpect(MockMvcResultMatchers.view().name("check-order-info"));

    }

    @Test
    void orderSuccGetRenders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/order-success")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order-success"));
    }
}