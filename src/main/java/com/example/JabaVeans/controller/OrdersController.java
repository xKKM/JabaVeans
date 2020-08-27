package com.example.JabaVeans.controller;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.helper.ProductPrices;
import com.example.JabaVeans.service.EmailService;
import com.example.JabaVeans.service.OrderService;
import com.example.JabaVeans.viewhelper.TablePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

@Controller
public class OrdersController {

    private final OrderService orderService;
    private final EmailService emailService;

    @Autowired
    public OrdersController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }


    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ModelAndView orders() {
        ModelAndView mv = new ModelAndView("orders-table");
        TablePage tablePage = new TablePage();
        Slice<Order> orders = orderService.findAllByDateOfOrder(tablePage.getCurrentPage(), tablePage.getSize(), "DESC");
        tablePage.update(orders);
        mv.addObject("orders", orders.getContent());
        mv.addObject("tablePage", tablePage);
        mv.addObject("sort", "DESC");
        return mv;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ModelAndView orders(@ModelAttribute("tablePage") TablePage tablePage, @ModelAttribute("sort") String sort) {
        ModelAndView mv = new ModelAndView("orders-table");
        Slice<Order> orders;
        tablePage.updatePageNumber();
        if (tablePage.getSearchStr().isBlank()) {
            orders = orderService.findAllByDateOfOrder(tablePage.getCurrentPage(), tablePage.getSize(), sort);
        } else {
            orders = orderService.findAll(tablePage.getCurrentPage(), tablePage.getSize(), tablePage.getSearchStr(), sort);
        }
        tablePage.update(orders);
        mv.addObject("orders", orders.getContent());
        mv.addObject(tablePage);
        mv.addObject(sort);

        return mv;
    }

    @PostMapping("/r/orders")
    public ModelAndView ordersRedirect() {
        ModelAndView mv = new ModelAndView("redirect:/orders");
        return mv;
    }

    @RequestMapping("/orders/details/{id}")
    public ModelAndView orderDetails(@PathVariable("id") String idStr) {
        ModelAndView mv = new ModelAndView("order-details");
        Integer id;
        Order order = null;
        boolean error = true;
        try {
            id = Integer.parseInt(idStr);
            order = orderService.findById(id);
        } catch (NumberFormatException ignored) {
        }
        if (order != null) {
            error = false;
            mv.addObject("order", order);
        }
        mv.addObject("gettingOrderError", error);
        return mv;

    }

    @RequestMapping(value = "/orders/save-state", method = RequestMethod.POST)
    public ModelAndView orderSaveState(@Valid Order order, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (bindingResult.hasErrors() == false) {
            emailService.sendOrderStateChange(orderService.save(order));
            mv.setViewName("redirect:/orders/details/" + order.getOrderID());
        } else {
            mv.setViewName("redirect:/orders");
        }
        // mv.addObject(order);
        return mv;
    }

    @GetMapping(value = "/orders/add")
    public ModelAndView order() {
        ModelAndView mv = new ModelAndView("back-order-page");
        mv.addObject(new Order());
        return mv;
    }

    @PostMapping(value = "/orders/add")
    public ModelAndView order(@Valid Order order, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mv.addObject(order);
            mv.setViewName("back-order-page");
        } else {

            order.setDateOfOrder(new Date());
            order.setOrderState((byte) 0);
            order.setTotalPrice(
                    BigDecimal.valueOf(order.getQuantity())
                            .multiply(ProductPrices.getPrice(order.getWeight()))
            );

            emailService.sendOrderConfirmation(
                    orderService.save(order));
            mv.setViewName("redirect:/orders");
        }

        return mv;
    }
}
