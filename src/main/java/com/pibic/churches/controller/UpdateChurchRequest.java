package com.pibic.churches.controller;

import java.util.List;

public record UpdateChurchRequest(
        String name,
        String description,
        String street,
        String city,
        String state,
        String bibliographyReference,
        List<String> imageUrlsToBeRemoved,
        List<ImageChurchRequest> images
) {
}
