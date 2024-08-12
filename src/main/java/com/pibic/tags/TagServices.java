package com.pibic.tags;

import com.pibic.tags.dtos.TagResponse;
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
        var isUniqueName = !tagRepository.findPublishedByName(name).isPresent();
        var tag = Tag.create(name, user, isUniqueName);
        tagRepository.persist(tag);
        return new CreateTagDto(tag.getId(), tag.getName());
    }

    public List<TagResponse> getAllTags() {
        return tagRepository
                .list("""
                        SELECT t
                        FROM Tag t
                        WHERE t.isPublished = true
                        AND EXISTS (
                            SELECT p
                            FROM Painting p
                            JOIN p.tags pt
                            WHERE pt.id = t.id
                        )
                        """)
                .stream()
                .map(TagResponse::fromTag)
                .toList();
    }

    public TagResponse getTagByName(String name) {
        var tag = tagRepository.findPublishedByName(name)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        return new TagResponse(tag.getId(), tag.getName());
    }

    @Transactional
    public TagResponse updateTag(Long tagId, String newName, Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var tag = tagRepository.findById(tagId);
        if (tag == null) {
            throw new NotFoundException("Tag not found");
        }
        var isUniqueName = !tagRepository.findPublishedByName(newName).isPresent();
        tag.updateName(newName, isUniqueName, user);
        return new TagResponse(tag.getId(), tag.getName());
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
