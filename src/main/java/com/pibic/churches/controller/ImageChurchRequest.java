package com.pibic.churches.controller;

public record ImageChurchRequest(
        String base64Image,
        String photographer
) {
}
