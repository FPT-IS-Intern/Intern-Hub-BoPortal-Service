package com.fis.boportalservice.api.validation;

import com.fis.boportalservice.api.dto.request.BoLoginRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class ValidBoLoginValidator implements ConstraintValidator<ValidBoLogin, BoLoginRequest> {

  @Override
  public boolean isValid(BoLoginRequest value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    boolean hasUsername = StringUtils.hasText(value.username())
        || StringUtils.hasText(value.encryptedUsername());
    boolean hasPassword = StringUtils.hasText(value.password())
        || StringUtils.hasText(value.encryptedPassword());

    if (hasUsername && hasPassword) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    if (!hasUsername) {
      context.buildConstraintViolationWithTemplate("username or encryptedUsername is required")
          .addPropertyNode("username")
          .addConstraintViolation();
    }
    if (!hasPassword) {
      context.buildConstraintViolationWithTemplate("password or encryptedPassword is required")
          .addPropertyNode("password")
          .addConstraintViolation();
    }
    return false;
  }
}
