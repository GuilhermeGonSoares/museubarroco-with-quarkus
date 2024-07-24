package com.pibic.churches.dtos;

import java.util.List;

public record CreateChurchDto(
        String name,
        String description,
        String bibliographyReferences,
        String street,
        String city,
        String state,
        Long registeredBy,
        List<ChurchImageDto> images
)
{
}
