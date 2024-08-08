package com.pibic.churches.controller;

import com.pibic.shared.abstraction.ValidBase64Image;
import jakarta.validation.constraints.NotBlank;

public record ImageChurchRequest(
        @NotBlank(message = "Imagem é obrigatória")
        @ValidBase64Image
        String base64Image,
        String photographer
) {
}
