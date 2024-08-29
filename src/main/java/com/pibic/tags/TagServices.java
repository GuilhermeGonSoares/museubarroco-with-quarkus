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
        var existingTag = tagRepository
                .find("lower(name)", name.toLowerCase())
                .firstResultOptional().orElse(null);
        if (existingTag == null){
            var tag = Tag.create(name, user);
            tagRepository.persist(tag);
            return new CreateTagDto(tag.getId(), tag.getName());
        }
        if (user.isAdmin() && !existingTag.isPublished()) {
            existingTag.publish();
            return new CreateTagDto(existingTag.getId(), existingTag.getName());
        }
        if (user.isAdmin() && existingTag.isPublished()) {
            throw new IllegalArgumentException("Tag already published");
        }
        existingTag.addTagToUser(user);
        return new CreateTagDto(existingTag.getId(), existingTag.getName());
    }

    public List<TagResponse> getAllPublishedTags() {
        return tagRepository
                .list("isPublished = ?1", true)
                .stream()
                .map(TagResponse::fromTag)
                .toList();
    }

    public TagResponse getTagByName(String name) {
        var tag = tagRepository.findPublishedByName(name)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        return new TagResponse(tag.getId(), tag.getName());
    }

    public List<TagResponse> getAvailableTags(Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (user.isAdmin()) {
            return getAllPublishedTags();
        }
        return tagRepository
                .find("isPublished = true or ?1 member of users", user)
                .stream()
                .map(TagResponse::fromTag)
                .toList();
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
        if (!user.isAdmin() && !(tag.getUsers().contains(user))) {
            throw new IllegalArgumentException("Only admin or tag owner can delete tag");
        }

        tag.removeTagFromUser(user);
        tag.unpublish();

        if (tag.getUsers().isEmpty()) {
            var isTagRelatedToPainting = tagRepository.count("""
            SELECT COUNT(pt)
            FROM Painting pt
            JOIN pt.tags t
            WHERE t.id = ?1
            """, tagId) > 0;
            if (!isTagRelatedToPainting) {
                tagRepository.delete(tag);
            }
        }
    }
}
