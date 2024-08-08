package com.pibic.churches.dtos;

import com.pibic.churches.Church;

import java.util.List;

public record ChurchResponse(
        Long id,
        String name,
        String description,
        String bibliographyReferences,
        boolean isPublished,
        String street,
        String city,
        String state,
        List<ImageResponse> images,
        List<PaintingResponse> paintings
) {
    public record ImageResponse(String url, String photographer) {
    }
    public record TagResponse(Long id, String name) {
    }
    public record PaintingResponse(
            Long id,
            String title,
            String dateOfCreation,
            String placement,
            List<ImageResponse> images,
            List<TagResponse> tags
    ) {
    }

    public static ChurchResponse fromChurch(Church church){
        return new ChurchResponse(
                church.getId(),
                church.getName(),
                church.getDescription(),
                church.getBibliographyReferences(),
                church.isPublished(),
                church.getAddress().street(),
                church.getAddress().city(),
                church.getAddress().state(),
                church.getImages().stream()
                        .map(image -> new ImageResponse(image.getUrl(), image.getPhotographer()))
                        .toList(),
                church.getPaintings().stream()
                        .map(painting -> new PaintingResponse(
                                painting.getId(),
                                painting.getTitle(),
                                painting.getDateOfCreation(),
                                painting.getPlacement(),
                                painting.getImages().stream()
                                        .map(image -> new ImageResponse(image.getUrl(), image.getPhotographer()))
                                        .toList(),
                                painting.getTags().stream()
                                        .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                                        .toList()
                        ))
                        .toList()
        );
    }

}
