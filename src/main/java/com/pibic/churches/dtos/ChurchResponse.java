package com.pibic.churches.dtos;

import java.util.List;

public record ChurchResponse(
        Long id,
        String name,
        String description,
        String bibliographyReferences,
        String street,
        String city,
        String state,
        List<ChurchImageDto> images
) {
}
