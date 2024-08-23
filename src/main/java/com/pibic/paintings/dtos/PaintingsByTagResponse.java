package com.pibic.paintings.dtos;


import com.pibic.paintings.Painting;

import java.time.LocalDateTime;
import java.util.List;

public record PaintingsByTagResponse(
        Long id,
        String title,
        String description,
        String artisan,
        String dateOfCreation,
        String placement,
        String registeredBy,
        boolean isPublished,
        LocalDateTime submittedAt,
        LocalDateTime updatedAt,
        List<ImageResponse> images
) {
    public record ImageResponse(Long id, String url, String photographer) {}
    public static PaintingsByTagResponse fromPainting(Painting painting){
        return new PaintingsByTagResponse(
                painting.getId(),
                painting.getTitle(),
                painting.getDescription(),
                painting.getArtisan(),
                painting.getDateOfCreation(),
                painting.getPlacement(),
                painting.getRegisteredBy().getName(),
                painting.isPublished(),
                painting.getSubmittedAt(),
                painting.getUpdatedAt(),
                painting.getImages().stream()
                        .map(image -> new ImageResponse(image.getId(), image.getUrl(), image.getPhotographer()))
                        .toList()
        );
    }
}

