package com.pibic.shared.abstraction;

import com.pibic.shared.validations.Base64ImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Base64ImageValidator.class)
public @interface ValidBase64Image {
    String message() default "Invalid base64 image format or size exceeds maximum allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
