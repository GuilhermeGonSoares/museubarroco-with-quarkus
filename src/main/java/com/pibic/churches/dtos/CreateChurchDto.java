package com.pibic.churches.dtos;

import java.util.List;

public record CreateChurchDto(
        String name,
        String street,
        String city,
        String state,
        String description,
        String bibliographyReferences,
        Long registeredBy,
        List<ChurchImageDto> images
)
{
}
