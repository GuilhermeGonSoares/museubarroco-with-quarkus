package com.pibic.paintings.dtos;

import java.util.List;

public record UpdatePaintingDto(
        Long id,
        String title,
        String description,
        String artisan,
        String dateOfCreation,
        String bibliographySource,
        String bibliographyReference,
        String placement,
        List<String> imagesUrlsToRemove,
        List<ImageDto> images,
        List<String> engravingsUrlsToRemove,
        List<EngravingDto> engravings,
        List<Long> tags,
        Long churchId,
        Long userId
) {
}
