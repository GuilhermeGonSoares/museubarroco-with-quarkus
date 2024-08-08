package com.pibic.churches.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateChurchRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        String description,
        String street,
        @NotBlank(message = "Cidade é obrigatória")
        String city,
        @NotBlank(message = "Estado é obrigatório")
        String state,
        List<String> bibliographyReference,
        List<String> imageUrlsToBeRemoved,
        List<@Valid ImageChurchRequest> images
) {
}
