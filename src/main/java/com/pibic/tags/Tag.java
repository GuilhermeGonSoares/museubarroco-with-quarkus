package com.pibic.tags;

import com.pibic.users.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public User getUser() {
        return user;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void updateName(String name, boolean isUniqueName, User updatedBy) {
        if(isPublished && !updatedBy.isAdmin()) {
            throw new IllegalArgumentException("Only admin can update published tag name");
        }
        if (!updatedBy.isAdmin() && !(updatedBy.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("Only admin or tag owner can update published tag name");
        }
        if (!isUniqueName) {
            throw new IllegalArgumentException("Tag name must be unique");
        }
        this.name = name;
    }

    public void publish() {
        this.isPublished = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
