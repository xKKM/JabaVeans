package com.example.JabaVeans.viewhelper;

import com.example.JabaVeans.validation.StringMatchConstraint;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@StringMatchConstraint(field = "newPass1", fieldMatch = "newPass2", message = "Hasła nie są takie same.")
public class PassProperties {
    @NotNull
    @NotBlank(message = "Podaj aktualne hasło.")
    String oldPass;
    @NotNull
    @Length(min = 4, max = 72, message = "Długość hasła musi mieć długość 4-72 znaków.")
    String newPass1;
    @NotNull
    @Length(min = 4, max = 72, message = "Długość hasła musi mieć długość 4-72 znaków.")
    String newPass2;

    public PassProperties() {
        oldPass = "";
        newPass1 = "";
        newPass2 = "";
    }
}
