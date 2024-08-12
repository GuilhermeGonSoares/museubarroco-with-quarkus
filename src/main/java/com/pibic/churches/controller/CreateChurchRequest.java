package com.pibic.churches.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateChurchRequest(
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
        @Size(min = 1, message = "Deve ter pelo menos uma imagem")
        List<@Valid ImageChurchRequest> images
) {
}
