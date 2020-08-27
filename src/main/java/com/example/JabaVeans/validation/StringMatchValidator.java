package com.example.JabaVeans.validation;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringMatchValidator implements ConstraintValidator<StringMatchConstraint, Object> {
   private String field;
   private String fieldMatch;

   public void initialize(StringMatchConstraint constraint) {
      this.field = constraint.field();
      this.fieldMatch = constraint.fieldMatch();
   }

   public boolean isValid(Object obj, ConstraintValidatorContext context) {

         Object fieldValue = new BeanWrapperImpl(obj)
                 .getPropertyValue(field);
         Object fieldMatchValue = new BeanWrapperImpl(obj)
                 .getPropertyValue(fieldMatch);

         if (fieldValue != null && fieldMatchValue != null) {
            return fieldValue.equals(fieldMatchValue);
         }
         return false;

   }
}
