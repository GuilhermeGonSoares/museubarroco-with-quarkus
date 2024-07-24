package com.pibic.paintings.controller;

import java.util.List;

public record UpdatePaintingRequest(
        String title,
        String description,
        String dateOfCreation,
        String bibliographySource,
        String bibliographyReference,
        String placement,
        String artisan,
        Long churchId,
        List<String> urlImagesToRemove,
        List<ImagePaintingRequest> images,
        List<String> urlEngravingsToRemove,
        List<EngravingRequest> engravings,
        List<Long> tagIds
) {
}
