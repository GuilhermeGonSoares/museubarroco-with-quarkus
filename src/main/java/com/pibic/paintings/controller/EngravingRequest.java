package com.pibic.paintings.controller;

public record EngravingRequest(
        String name,
        String base64Image,
        String createdBy
) {
}
