package com.pibic.churches.dtos;

import java.util.List;

public record UpdateChurchDto(
        Long id,
        String name,
        String description,
        String bibliographyReferences,
        String bibliographySource,
        String street,
        String city,
        String state,
        List<String> imageUrlsToBeRemoved,
        List<ChurchImageDto> images,
        Long updatedBy
) {
}
