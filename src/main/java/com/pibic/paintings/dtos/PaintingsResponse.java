package com.pibic.paintings.dtos;


import com.pibic.paintings.Painting;

import java.util.List;

public record PaintingsResponse(
        Long id,
        String title,
        String description,
        String artisan,
        String dateOfCreation,
        String placement,
        String registeredBy,
        boolean isPublished,
        List<ImageResponse> images,
        ChurchResponse church,
        List<TagResponse> tags
) {
    public record TagResponse(Long id, String name) {}
    public record ImageResponse(Long id, String url, String photographer) {}
    public record ChurchResponse(
            Long id,
            String name,
            String city,
            String state,
            String street
    ){}

    public static PaintingsResponse fromPainting(Painting painting){
        return new PaintingsResponse(
                painting.getId(),
                painting.getTitle(),
                painting.getDescription(),
                painting.getArtisan(),
                painting.getDateOfCreation(),
                painting.getPlacement(),
                painting.getRegisteredBy().getName(),
                painting.isPublished(),
                painting.getImages().stream()
                        .map(image -> new ImageResponse(image.getId(), image.getUrl(), image.getPhotographer()))
                        .toList(),
                new ChurchResponse(
                        painting.getChurch().getId(),
                        painting.getChurch().getName(),
                        painting.getChurch().getAddress().city(),
                        painting.getChurch().getAddress().state(),
                        painting.getChurch().getAddress().street()
                ),
                painting.getTags().stream()
                        .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                        .toList()
        );
    }
}

