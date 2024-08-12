package com.pibic.churches.dtos;

import com.pibic.churches.Church;

import java.util.List;

public record ChurchesResponse(
        Long id,
        String name,
        String description,
        String[] bibliographyReferences,
        String[] bibliographySource,
        boolean isPublished,
        String street,
        String city,
        String state,
        List<ImageResponse> images
) {
    public record ImageResponse(Long id, String url, String photographer) {
    }

    public static ChurchesResponse fromChurch(Church church) {
        return new ChurchesResponse(
                church.getId(),
                church.getName(),
                church.getDescription(),
                church.getBibliographyReferences() == null ? new String[0] : church.getBibliographyReferences().split("\n"),
                church.getBibliographySource() == null ? new String[0] : church.getBibliographySource().split("\n"),
                church.isPublished(),
                church.getAddress().street(),
                church.getAddress().city(),
                church.getAddress().state(),
                church.getImages().stream()
                        .map(image -> new ImageResponse(image.getId() ,image.getUrl(), image.getPhotographer()))
                        .toList()
        );
    }
}
