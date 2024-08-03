package com.pibic.paintings.controller;

import jakarta.validation.constraints.NotBlank;

public record AddAnswer(
        @NotBlank(message = "A resposta não pode ser vazia")
        String response
) {
}
