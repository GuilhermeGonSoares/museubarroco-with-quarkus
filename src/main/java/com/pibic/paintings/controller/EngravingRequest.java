package com.pibic.paintings.controller;

import com.pibic.shared.abstraction.ValidBase64Image;
import jakarta.validation.constraints.NotBlank;

public record EngravingRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        @NotBlank(message = "Imagem é obrigatória")
        @ValidBase64Image
        String base64Image,
        String createdBy
) {
}
