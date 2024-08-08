package com.pibic.paintings.dtos;


import com.pibic.paintings.Painting;

import java.time.LocalDateTime;
import java.util.List;

public record PaintingResponse(
        Long id,
        String title,
        String description,
        String artisan,
        String dateOfCreation,
        String placement,
        String registeredBy,
        boolean isPublished,
        String bibliographySource,
        String bibliographyReference,
        LocalDateTime submittedAt,
        LocalDateTime updatedAt,
        ChurchResponse church,
        List<ImageResponse> images,
        List<EngravingResponse> engravings,
        List<TagResponse> tags
) {
    public record TagResponse(Long id, String name) {}
    public record ImageResponse(Long id, String url, String photographer) {}
    public record EngravingResponse(Long id, String name, String url, String createdBy) {}
    public record ChurchResponse(
            Long id,
            String name,
            String city,
            String state,
            String street
    ){}

    public static PaintingResponse fromPainting(Painting painting){
        return new PaintingResponse(
                painting.getId(),
                painting.getTitle(),
                painting.getDescription(),
                painting.getArtisan(),
                painting.getDateOfCreation(),
                painting.getPlacement(),
                painting.getRegisteredBy().getName(),
                painting.isPublished(),
                painting.getBibliographySource(),
                painting.getBibliographyReference(),
                painting.getSubmittedAt(),
                painting.getUpdatedAt(),
                new ChurchResponse(
                        painting.getChurch().getId(),
                        painting.getChurch().getName(),
                        painting.getChurch().getAddress().city(),
                        painting.getChurch().getAddress().state(),
                        painting.getChurch().getAddress().street()
                ),
                painting.getImages().stream()
                        .map(image -> new ImageResponse(image.getId(), image.getUrl(), image.getPhotographer()))
                        .toList(),
                painting.getEngravings().stream()
                        .map(engraving -> new EngravingResponse(engraving.getId(), engraving.getName(), engraving.getUrl(), engraving.getCreatedBy()))
                        .toList(),
                painting.getTags().stream()
                        .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                        .toList()
        );
    }
}

