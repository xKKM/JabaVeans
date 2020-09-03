package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.helper.OrderStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final String from;

    private final String demo;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, @Value("${spring.mail.username}") String from,
                            @Value("${demo.testing:no}") String demo) {
        this.emailSender = emailSender;
        this.from = from;
        this.demo = demo;
    }



    @Override
    @Async
    public void sendOrderConfirmation(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(order.getEmail());
        message.setSubject("Java Beans potwierdzenie.");

        String text = "Przyjeliśmy zamówienie dla %s %s. \n\n " +
                "Sprawdź szczegóły oraz stan zamówienia na naszej stronie wpisjąc numer zamówienia %d.";

        message.setText(
                String.format(
                        text,
                        order.getFirstName(),
                        order.getSurname(),
                        order.getOrderID()));
        if(!demo.equals("enabled")) {
            emailSender.send(message);
        }
    }

    @Override
    @Async
    public void sendOrderStateChange(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(order.getEmail());
        message.setSubject("Java Beans zmiana stanu zamówienia.");

        String text = "Zamówienie dla %s %s zmieniło stan na %s.\n\n " +
                "Sprawdź szczegóły oraz stan zamówienia na naszej stronie wpisjąc numer zamówienia %d.";

        message.setText(
                String.format(
                        text,
                        order.getFirstName(),
                        order.getSurname(),
                        OrderStrings.orderStates[order.getOrderState()],
                        order.getOrderID()));
        if(!demo.equals("enabled")) {
            emailSender.send(message);
        }
    }
}
