package com.pibic.paintings.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdatePaintingRequest(
        @NotBlank(message = "Título é obrigatório")
        String title,
        String description,
        String dateOfCreation,
        String bibliographySource,
        List<String> bibliographyReference,
        String placement,
        String artisan,
        @NotNull(message = "Igreja é obrigatória")
        Long churchId,
        List<String> urlImagesToRemove,
        List<@Valid ImagePaintingRequest> images,
        List<String> urlEngravingsToRemove,
        List<@Valid EngravingRequest> engravings,
        List<Long> tagIds
) {
}
