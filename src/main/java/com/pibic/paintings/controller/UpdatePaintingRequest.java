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
        @NotNull(message = "Fonte da bibliografia deve ser uma lista")
        List<String> bibliographySource,
        @NotNull(message = "Referências bibliográficas deve ser uma lista")
        List<String> bibliographyReference,
        String placement,
        String artisan,
        @NotNull(message = "Igreja é obrigatória")
        Long churchId,
        @NotNull(message = "Deve ser uma lista de imagens para serem removidas")
        List<String> urlImagesToRemove,
        @NotNull(message = "Deve ser uma lista de novas imagens")
        List<@Valid ImagePaintingRequest> images,
        @NotNull(message = "Deve ser uma lista de gravuras para serem removidas")
        List<String> urlEngravingsToRemove,
        @NotNull(message = "Deve ser uma lista de novas gravuras")
        List<@Valid EngravingRequest> engravings,
        @NotNull(message = "Deve ser uma lista de tags")
        List<Long> tagIds
) {
}
