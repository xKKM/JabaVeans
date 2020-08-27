package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.Order;

public interface EmailService {
    void sendOrderConfirmation(Order order);
    void sendOrderStateChange(Order order);
}
