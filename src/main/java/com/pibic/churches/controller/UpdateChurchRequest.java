package com.pibic.churches.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        @NotNull(message = "Referências bibliográficas deve ser uma lista")
        List<String> bibliographyReference,
        @NotNull(message = "Fontes bibliográficas deve ser uma lista")
        List<String> bibliographySource,
        @NotNull(message = "Images para serem removidas deve ser uma lista")
        List<String> imageUrlsToBeRemoved,
        @NotNull(message = "Deve ser uma lista de novas imagens")
        List<@Valid ImageChurchRequest> images
) {
}
