package com.pibic.paintings.dtos;

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
        List<ImageResponse> images,
        List<EngravingResponse> engravings,
        ChurchResponse church,
        List<TagResponse> tags
) {
}

record ImageResponse(
        String url,
        String photographer
){}

record EngravingResponse(
        String name,
        String url,
        String createdBy
){}

record ChurchResponse(
        Long id,
        String name,
        String city,
        String state,
        String street,
        List<ImageResponse> images
){}

record TagResponse(
        Long id,
        String name
){}