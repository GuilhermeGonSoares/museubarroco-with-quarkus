package com.pibic.tags;

import com.pibic.users.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean isPublished;
    @ManyToMany
    @JoinTable(
            name = "tags_users",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public Tag() {
    }

    private Tag(String name, boolean isPublished, User user) {
        this.name = name;
        this.isPublished = isPublished;
        this.users.add(user);
    }

    public static Tag create(String name, User user) {
        var isPublished = user.isAdmin();
        return new Tag(name, isPublished, user);
    }

    public void addTagToUser(User user) {
        users.add(user);
    }

    public void removeTagFromUser(User user) {
        users.remove(user);
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

    public Set<User> getUsers() {
        return users;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void publish() {
        this.isPublished = true;
    }

    public void unpublish() {
        this.isPublished = false;
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
