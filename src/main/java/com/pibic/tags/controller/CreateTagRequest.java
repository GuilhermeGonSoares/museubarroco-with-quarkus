package com.pibic.tags.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTagRequest(
        @NotBlank(message = "Name must not be blank")
        @NotNull(message = "Name must not be null")
        String name
) {
}