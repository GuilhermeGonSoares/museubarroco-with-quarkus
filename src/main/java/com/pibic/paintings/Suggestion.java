package com.pibic.paintings;

import com.pibic.shared.Image;
import com.pibic.users.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suggestions")
public class Suggestion {
    private enum Status {
        PENDING,
        ANSWERED
    }
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String reason;
    private String response;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "suggestion_images",
            joinColumns = @JoinColumn(name = "suggestion_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images = new ArrayList<>();

    public Suggestion() {
    }

    public Suggestion(String reason, User user, List<Image> images) {
        this.reason = reason;
        this.status = Status.PENDING;
        this.user = user;
        this.images.addAll(images);
    }

    @PrePersist
    public void prePersist(){
        for (var image : images){
            image.setType("suggestion");
        }
    }

    public void Answer(String message){
        if (status == Status.PENDING){
            response = message;
            images.clear();
            status = Status.ANSWERED;
        }
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public String getResponse() {
        return response;
    }

    public String getStatus() {
        return status.name();
    }

    public List<Image> getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
