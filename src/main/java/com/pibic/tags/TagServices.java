package com.pibic.tags;

import com.pibic.tags.dtos.TagDto;
import com.pibic.tags.dtos.CreateTagDto;
import com.pibic.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class TagServices {
    @Inject
    TagRepository tagRepository;
    @Inject
    UserRepository userRepository;

    @Transactional
    public CreateTagDto createTag(String name, Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var isUniqueName = !tagRepository.findByName(name).isPresent();
        var tag = Tag.create(name, user, isUniqueName);
        tagRepository.persist(tag);
        return new CreateTagDto(tag.getId(), tag.getName());
    }

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .toList();
    }

    public TagDto getTagByName(String name) {
        var tag = tagRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        return new TagDto(tag.getId(), tag.getName());
    }

    @Transactional
    public TagDto updateTag(Long tagId, String newName, Long userId) {
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
        return new TagDto(tag.getId(), tag.getName());
    }

    @Transactional
    public void deleteTag(Long tagId, Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var tag = tagRepository.findById(tagId);
        if (tag == null) {
            throw new NotFoundException("Tag not found");
        }
        if(tag.isPublished() && !user.isAdmin()) {
            throw new IllegalArgumentException("Only admin can delete published tag");
        }
        if (!user.isAdmin() && !(user.getId() == tag.getUser().getId())) {
            throw new IllegalArgumentException("Only admin or tag owner can delete tag");
        }
        tagRepository.delete(tag);
    }
}
