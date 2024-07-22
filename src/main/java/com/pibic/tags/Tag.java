package com.pibic.tags;

import com.pibic.users.User;
import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private boolean isPublished;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Tag() {
    }

    private Tag(String name, boolean isPublished, User user) {
        this.name = name;
        this.isPublished = isPublished;
        this.user = user;
    }

    public static Tag create(String name, User user, boolean isUniqueName) {
        var isPublished = user.isAdmin();
        if (!isUniqueName) {
            throw new IllegalArgumentException("Tag name must be unique");
        }
        return new Tag(name, isPublished, user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void updateName(String name, boolean isUniqueName, User updatedBy) {
        if(isPublished && !updatedBy.isAdmin()) {
            throw new IllegalArgumentException("Only admin can update published tag name");
        }
        if (!updatedBy.isAdmin() && !(updatedBy.getId() == user.getId())) {
            throw new IllegalArgumentException("Only admin or tag owner can update published tag name");
        }
        if (!isUniqueName) {
            throw new IllegalArgumentException("Tag name must be unique");
        }
        this.name = name;
    }

    public void publish(boolean isAdmin) {
        if (isAdmin)
            this.isPublished = true;
    }
}
