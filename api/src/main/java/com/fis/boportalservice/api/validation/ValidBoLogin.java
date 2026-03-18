package com.fis.boportalservice.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidBoLoginValidator.class)
@Documented
public @interface ValidBoLogin {
  String message() default "password or encryptedPassword is required";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
