package com.fis.boportalservice.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUser {
    String[] loginFields(); // Fields from LoginUserInfo

    String[] reqFields(); // Corresponding fields from RequestBody
}
