package com.example.JabaVeans.service;

import com.example.JabaVeans.dto.Order;
import com.example.JabaVeans.dto.helper.OrderStrings;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceImplTest {

    private JavaMailSender emailSender;

    private EmailService emailService;

    EmailServiceImplTest() {
        emailSender = mock(JavaMailSender.class);
        emailService = new EmailServiceImpl(emailSender, "email.example.com");

    }


    @Test
    void sendOrderConfirmationTest() {
        assertNotNull(emailSender);
        Order order = new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1));

        String text = "Przyjeliśmy zamówienie dla %s %s. \n\n " +
                "Sprawdź szczegóły oraz stan zamówienia na naszej stronie wpisjąc numer zamówienia %d.";
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        doNothing().when(emailSender).send(captor.capture());
        emailService.sendOrderConfirmation(order);


        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("email.example.com", sentMessage.getFrom());
        assertNotNull(sentMessage.getTo());
        assertEquals(order.getEmail(), sentMessage.getTo()[0]);
        assertEquals("Java Beans potwierdzenie.", sentMessage.getSubject());
        assertEquals(String.format(text, order.getFirstName(),
                order.getSurname(), order.getOrderID()), sentMessage.getText());
    }

    @Test
    void sendOrderStateChangeTest() {

        assertNotNull(emailSender);
        Order order = new Order(1, "Andrzej", "Kowalski", "emial@email",
                "randomaddress", (byte) 0, (byte) 2, new Date(), (byte) 0, BigDecimal.valueOf(1));

        String text = "Zamówienie dla %s %s zmieniło stan na %s.\n\n " +
                "Sprawdź szczegóły oraz stan zamówienia na naszej stronie wpisjąc numer zamówienia %d.";


        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        doNothing().when(emailSender).send(captor.capture());

        emailService.sendOrderStateChange(order);

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("email.example.com", sentMessage.getFrom());
        assertNotNull(sentMessage.getTo());
        assertEquals(order.getEmail(), sentMessage.getTo()[0]);
        assertEquals("Java Beans zmiana stanu zamówienia.", sentMessage.getSubject());
        assertEquals(String.format(text, order.getFirstName(), order.getSurname(),
                OrderStrings.orderStates[order.getOrderState()], order.getOrderID()), sentMessage.getText());
    }
}