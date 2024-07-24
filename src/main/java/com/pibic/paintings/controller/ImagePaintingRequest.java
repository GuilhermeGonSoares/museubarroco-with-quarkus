package com.pibic.paintings.controller;

public record ImagePaintingRequest(
        String base64Image,
        String photographer
) {
}
