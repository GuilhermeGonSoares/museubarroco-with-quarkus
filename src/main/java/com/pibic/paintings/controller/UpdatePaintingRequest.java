package com.pibic.paintings.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdatePaintingRequest(
        @NotBlank(message = "Title is required")
        String title,
        String description,
        String dateOfCreation,
        String bibliographySource,
        List<String> bibliographyReference,
        String placement,
        String artisan,
        @NotBlank(message = "Church ID is required")
        Long churchId,
        List<String> urlImagesToRemove,
        List<@Valid ImagePaintingRequest> images,
        List<String> urlEngravingsToRemove,
        List<@Valid EngravingRequest> engravings,
        List<Long> tagIds
) {
}
