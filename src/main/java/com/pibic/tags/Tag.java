package com.pibic.tags;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private boolean isPublished;

    public Tag() {
    }

    private Tag(String name, boolean isPublished) {
        this.name = name;
        this.isPublished = isPublished;
    }

    public static Tag create(String name, boolean isAdmin, boolean isUniqueName) {
        var isPublished = isAdmin;
        if (!isUniqueName) {
            throw new IllegalArgumentException("Tag name must be unique");
        }
        return new Tag(name, isPublished);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void updateName(String name, boolean isUniqueName) {
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
