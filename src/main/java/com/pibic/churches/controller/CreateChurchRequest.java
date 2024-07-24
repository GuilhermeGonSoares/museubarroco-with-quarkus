package com.pibic.churches.controller;

import java.util.List;

public record CreateChurchRequest(
        String name,
        String description,
        String street,
        String city,
        String state,
        String bibliographyReference,
        List<ImageChurchRequest> images
) {
}
