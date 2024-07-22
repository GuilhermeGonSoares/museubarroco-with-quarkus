package com.pibic.tags;

import com.pibic.tags.dtos.TagResponse;
import com.pibic.tags.dtos.CreateTagResponse;
import com.pibic.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class TagServices {
    @Inject
    TagRepository tagRepository;
    @Inject
    UserRepository userRepository;

    public CreateTagResponse createTag(String name, Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var isUniqueName = !tagRepository.findByName(name).isPresent();
        var tag = Tag.create(name, user, isUniqueName);
        tagRepository.persist(tag);
        return new CreateTagResponse(tag.getId(), tag.getName());
    }

    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .toList();
    }

    public TagResponse getTagByName(String name) {
        var tag = tagRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        return new TagResponse(tag.getId(), tag.getName());
    }

    public TagResponse updateTag(Long tagId, String newName, Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var tag = tagRepository.findById(tagId);
        if (tag == null) {
            throw new NotFoundException("Tag not found");
        }
        var isUniqueName = !tagRepository.findByName(newName).isPresent();
        tag.updateName(newName, isUniqueName, user);
        tagRepository.persist(tag);
        return new TagResponse(tag.getId(), tag.getName());
    }
}
