package com.pibic.paintings.controller;

import jakarta.validation.Valid;

import java.util.List;

public record AddSuggestionRequest(
        String reason,
        List<@Valid ImagePaintingRequest> images
) {
}
