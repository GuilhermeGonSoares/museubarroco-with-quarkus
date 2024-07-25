package com.pibic.paintings.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePaintingRequest(
        @NotBlank(message = "Title is required")
        String title,
        String description,
        String dateOfCreation,
        String bibliographySource,
        List<String> bibliographyReference,
        String placement,
        String artisan,
        @NotNull(message = "Church ID is required")
        Long churchId,
        @Size(min = 1, message = "At least one image is required")
        List<@Valid ImagePaintingRequest> images,
        List<@Valid EngravingRequest> engravingRequests,
        List<Long> tagIds
) {
}
