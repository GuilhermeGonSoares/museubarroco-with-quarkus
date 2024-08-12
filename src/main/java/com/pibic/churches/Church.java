package com.pibic.churches;

import com.pibic.paintings.Painting;
import com.pibic.shared.images.Image;
import com.pibic.users.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "churches")
public class Church {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Address address;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String bibliographyReferences;
    @Column(columnDefinition = "TEXT")
    private String bibliographySource;
    @Column(nullable = false)
    private boolean isPublished;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_by")
    private User registeredBy;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "church_images",
            joinColumns = @JoinColumn(name = "church_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images = new ArrayList<>();
    @OneToMany(mappedBy = "church")
    private List<Painting> paintings = new ArrayList<>();

    public Church() {
    }

    private Church (String name,
                    Address address,
                    String description,
                    String bibliographyReferences,
                    String bibliographySource,
                    boolean isPublished,
                    User registeredBy,
                    List<Image> images) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.bibliographyReferences = bibliographyReferences;
        this.bibliographySource = bibliographySource;
        this.isPublished = isPublished;
        this.registeredBy = registeredBy;
        this.images.addAll(images);
    }

    public static Church create(String name,
                                 Address address,
                                 String description,
                                 String bibliographyReferences,
                                 String bibliographySource,
                                 User registeredBy,
                                 List<Image> images
    ) {
        var isPublished = registeredBy.isAdmin();
        if (images.isEmpty()) {
            throw new IllegalArgumentException("Church must have at least one image");
        }
        return new Church(name, address, description, bibliographyReferences, bibliographySource, isPublished, registeredBy, images);
    }

    public void update(
            String name,
            Address address,
            String description,
            String bibliographyReferences,
            String bibliographySource,
            List<String> imageUrlsToRemove,
            List<Image> imagesToBeAdded,
            User user
    ) {
        if (isPublished && !user.isAdmin()) {
            throw new IllegalStateException("Only admins can update published churches");
        }
        if (!user.isAdmin() && !user.getId().equals(registeredBy.getId())) {
            throw new IllegalStateException("Only the user who registered the church can update it");
        }
        if (imageUrlsToRemove != null) {
            this.images.removeIf(image -> imageUrlsToRemove.contains(image.getUrl()));
        }
        if (this.images.size() + imagesToBeAdded.size() == 0) {
            throw new IllegalArgumentException("Church must have at least one image");
        }
        this.name = name;
        this.address = address;
        this.description = description;
        this.bibliographyReferences = bibliographyReferences;
        this.bibliographySource = bibliographySource;
        this.images.addAll(imagesToBeAdded);
    }

    @PrePersist
    public void prePersist() {
        for (var image : images) {
            image.setType("church");
        }
    }

    @PreUpdate
    public void preUpdate() {
        for (var image : images) {
            image.setType("church");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getBibliographyReferences() {
        return bibliographyReferences;
    }

    public String getBibliographySource() {
        return bibliographySource;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void publish() {
        isPublished = true;
    }

    public User getRegisteredBy() {
        return registeredBy;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Painting> getPaintings() {
        return paintings;
    }

    public void setPaintings(List<Painting> paintings) {
        this.paintings = paintings;
    }
}
