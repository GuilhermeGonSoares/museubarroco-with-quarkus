package com.pibic.paintings.dtos;

import com.pibic.tags.dtos.TagResponse;

import java.util.List;

public record PaintingResponse(
        Long id,
        String title,
        String description,
        String artisan,
        String dateOfCreation,
        String bibliographySource,
        String bibliographyReference,
        String placement,
        String registeredBy,
        List<ImageDto> images,
        List<EngravingDto> engravings,
        ChurchDto church,
        List<TagResponse> tags
) {
}

