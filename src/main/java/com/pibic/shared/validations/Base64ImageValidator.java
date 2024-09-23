package com.pibic.shared.validations;

import com.pibic.shared.abstraction.ValidBase64Image;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;

public class Base64ImageValidator implements ConstraintValidator<ValidBase64Image, String> {
    private static final long MAX_SIZE = 12 * 1024 * 1024;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        try {
            String imageData = value.replaceFirst("^data:image/[a-zA-Z]+;base64,", "");
            byte[] decodedBytes = Base64.getDecoder().decode(imageData);
            return decodedBytes.length <= MAX_SIZE;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
