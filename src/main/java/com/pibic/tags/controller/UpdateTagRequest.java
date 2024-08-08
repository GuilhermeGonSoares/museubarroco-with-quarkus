package com.pibic.tags.controller;

import jakarta.validation.constraints.NotBlank;

public record UpdateTagRequest(
        @NotBlank(message = "Nome não pode ser vazio ou nulo")
        String name
) {
}
