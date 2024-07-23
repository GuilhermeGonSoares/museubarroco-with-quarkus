package com.pibic.paintings.dtos;

import java.util.List;

public record CreatePaintingDto(
        String title,
        String description,
        String artisan,
        String dateOfCreation,
        String bibliographySource,
        String bibliographyReference,
        String placement,
        Long churchId,
        Long registeredById,
        List<ImageDto> images,
        List<EngravingDto> engravings,
        List<Long> tagsIds
)
{
}
