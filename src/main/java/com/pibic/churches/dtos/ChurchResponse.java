package com.pibic.churches.dtos;

import java.util.List;

public record ChurchDto(
        String name,
        String description,
        String bibliographyReferences,
        String street,
        String city,
        String state,
        List<ChurchImageDto> images
) {
}
