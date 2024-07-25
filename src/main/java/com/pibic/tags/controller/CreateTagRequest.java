package com.pibic.tags.controller;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(
        @NotBlank(message = "Name must not be blank")
        String name
) {
}