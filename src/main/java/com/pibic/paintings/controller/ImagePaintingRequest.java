package com.pibic.paintings.controller;

import com.pibic.shared.abstraction.ValidBase64Image;
import jakarta.validation.constraints.NotBlank;

public record ImagePaintingRequest(
        @NotBlank(message = "Base64Image é obrigatório")
        @ValidBase64Image
        String base64Image,
        String photographer
) {
}
