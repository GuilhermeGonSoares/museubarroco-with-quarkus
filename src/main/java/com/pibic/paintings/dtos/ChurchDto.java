package com.pibic.paintings.dtos;

import java.util.List;

public record ChurchDto(
        Long id,
        String name,
        String city,
        String state,
        String street,
        List<ImageDto> images
){}
