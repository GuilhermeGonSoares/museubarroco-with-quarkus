package com.pibic.tags;

import com.pibic.tags.dtos.CreateTagResponse;
import com.pibic.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

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

}
