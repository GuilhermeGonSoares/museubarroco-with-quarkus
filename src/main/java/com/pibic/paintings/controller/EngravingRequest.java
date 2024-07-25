package com.pibic.paintings.controller;

import com.pibic.shared.abstraction.ValidBase64Image;
import jakarta.validation.constraints.NotBlank;

public record EngravingRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Base64Image is required")
        @ValidBase64Image
        String base64Image,
        String createdBy
) {
}
