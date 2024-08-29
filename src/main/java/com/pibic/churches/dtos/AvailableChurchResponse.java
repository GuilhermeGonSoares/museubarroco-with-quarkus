package com.pibic.churches.dtos;

import com.pibic.churches.Church;

public record AvailableChurchResponse(
        Long id,
        String name
) {
    public static AvailableChurchResponse fromChurch(Church church) {
        return new AvailableChurchResponse(church.getId(), church.getName());
    }
}
