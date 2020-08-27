package com.example.JabaVeans.validation;

import com.example.JabaVeans.viewhelper.PassProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StringMatchValidatorTest {

    @Autowired
    Validator validator;

    @Test
    void whenPassPropertiesContainsMatchingStrings_isValid() {
        PassProperties passProperties = new PassProperties();
        passProperties.setOldPass("qwer");
        passProperties.setNewPass1("rewq");
        passProperties.setNewPass2("rewq");

        assertTrue(validator.validate(passProperties).isEmpty());

    }

    @Test
    void whenPassPropertiesDoNotContainMatchingStrings_isNotValid() {

        PassProperties passProperties = new PassProperties();
        passProperties.setOldPass("qwer");
        passProperties.setNewPass1("asdw");
        passProperties.setNewPass2("rewq");

        assertFalse(validator.validate(passProperties).isEmpty());
    }
}