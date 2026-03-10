package com.fis.boportalservice.api.validation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUser {
  String[] loginFields(); // Fields from LoginUserInfo

  String[] reqFields(); // Corresponding fields from RequestBody
}
