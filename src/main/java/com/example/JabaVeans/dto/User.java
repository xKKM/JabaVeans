package com.example.JabaVeans.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Może zawierać tylko litery i cyfry")
    @Length(min = 4, max = 254, message = "Nazwa uzytkownika musi mieć długość 4-254 znaków")
    private String username;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*_]*$",
            message = "Może zawierać tylko litery i cyfry oraz zanki specjalne !@#$%^&*_")
    @Length(min = 4, max = 72, message = "Hasło musi mieć długość 4-72 znaków")
    private String password;
    private boolean enabled;
    private String role;

}
