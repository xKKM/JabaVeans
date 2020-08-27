package com.example.JabaVeans.controller;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.helper.ProductPrices;
import com.example.JabaVeans.service.EmailService;
import com.example.JabaVeans.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final EmailService emailService;

    @Autowired
    public OrderController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @GetMapping(value = "")
    public ModelAndView order() {
        ModelAndView mv = new ModelAndView("order-page");
        mv.addObject(new Order());
        return mv;
    }

    @PostMapping(value = "")
    public ModelAndView order(@Valid Order order, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mv.addObject(order);
            mv.setViewName("order-page");
        } else {

            order.setDateOfOrder(new Date());
            order.setOrderState((byte) 0);
            order.setTotalPrice(
                    BigDecimal.valueOf(order.getQuantity())
                            .multiply(ProductPrices.getPrice(order.getWeight()))
            );

            emailService.sendOrderConfirmation(
                    orderService.save(order));
            mv.setViewName("redirect:/order/order-success");
        }

        return mv;
    }

    @GetMapping(value = "/check-order")
    public ModelAndView checkOrder(Boolean orderIdError) {
        ModelAndView mv = new ModelAndView("check-order");
        mv.addObject("orderID", "");
        mv.addObject("orderIdError", (orderIdError != null ? orderIdError : false));
        return mv;
    }

    @PostMapping(value = "/check-order")
    public ModelAndView checkOrder(String orderID) {
        ModelAndView mv = new ModelAndView("check-order-info");
        Integer id;
        Order order = null;

        mv.addObject("orderIdError", false);
        try {
            id = Integer.parseInt(orderID);
            order = orderService.findById(id);
        } catch (NumberFormatException e) {
            mv.addObject("orderIdError", true);
            mv.setViewName("redirect:/order/check-order");
        }

        if (order != null) {
            mv.addObject(order);
            mv.addObject("orderID", "");
        }  else {
            mv.addObject("orderIdError", true);
            mv.setViewName("redirect:/order/check-order");
        }
        return mv;
    }

    @GetMapping(value = "/order-success")
    public ModelAndView orderSucc() {
        ModelAndView mv = new ModelAndView("order-success");
        return mv;
    }

}
