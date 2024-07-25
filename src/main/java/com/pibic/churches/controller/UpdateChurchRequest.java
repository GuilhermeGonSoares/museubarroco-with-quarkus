package com.pibic.churches.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateChurchRequest(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        String street,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "State is required")
        String state,
        String bibliographyReference,
        List<String> imageUrlsToBeRemoved,
        List<@Valid ImageChurchRequest> images
) {
}
