package com.pibic.tags.dtos;

import com.pibic.tags.Tag;

public record TagResponse(Long id, String name) {
    public static TagResponse fromTag(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
