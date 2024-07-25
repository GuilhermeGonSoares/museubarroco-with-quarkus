package com.pibic.churches.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateChurchRequest(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        String street,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "State is required")
        String state,
        List<String> bibliographyReference,
        @Size(min = 1, message = "At least one image is required")
        List<@Valid ImageChurchRequest> images
) {
}
