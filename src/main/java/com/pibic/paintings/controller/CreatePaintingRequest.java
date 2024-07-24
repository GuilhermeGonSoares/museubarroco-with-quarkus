package com.pibic.paintings.controller;

import java.util.List;

public record CreatePaintingRequest(
        String title,
        String description,
        String dateOfCreation,
        String bibliographySource,
        String bibliographyReference,
        String placement,
        String artisan,
        Long churchId,
        List<ImagePaintingRequest> images,
        List<EngravingRequest> engravingRequests,
        List<Long> tagIds
) {
}
