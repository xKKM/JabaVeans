package com.example.JabaVeans.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderID;
    @NotNull
    @Length(min = 1,max = 50, message = "Zbyt długie imie.")
    private String firstName;
    @NotNull
    @Length(min = 1,max = 50, message = "Zbyt długie nazwisko.")
    private String surname;
    @NotNull
    @Email
    @Length(min = 1,max = 254, message = "Niepoprawny adres email.")
    private String email;
    @NotNull
    @Length(min = 4,max = 200, message = "Zbyt długi adres.")
    private String address;
    @NotNull
    @Min(value = 0)
    @Max(value = 3)
    private byte weight;
    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    private byte quantity;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Temporal(TemporalType.DATE)
    private Date dateOfOrder;
    private byte orderState;
    private BigDecimal totalPrice;

}
