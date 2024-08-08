package com.pibic.tags.controller;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(
        @NotBlank(message = "Nome não pode ser vazio ou nulo")
        String name
) {
}