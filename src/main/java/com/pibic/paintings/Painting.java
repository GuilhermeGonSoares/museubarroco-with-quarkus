package com.pibic.paintings;

import com.pibic.churches.Church;
import com.pibic.shared.Image;
import com.pibic.tags.Tag;
import com.pibic.users.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "paintings")
public class Painting {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    private boolean isPublished;
    private String artisan;
    private String dateOfCreation;
    private String bibliographySource;
    private String bibliographyReference;
    private String placement;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "painting_images",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "painting_id", referencedColumnName = "id")
    private List<Engraving> engravings = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "church_id")
    private Church church;
    @ManyToOne
    @JoinColumn(name = "registered_by")
    private User registeredBy;
    @ManyToMany
    @JoinTable(name = "painting_tags",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public Painting() {
    }

    private Painting(
            String title,
            String description,
            boolean isPublished,
            String artisan,
            String dateOfCreation,
            String bibliographySource,
            String bibliographyReference,
            String placement,
            Church church,
            User registeredBy,
            List<Image> images,
            List<Engraving> engravings,
            List<Tag> tags
    ) {
        this.title = title;
        this.description = description;
        this.isPublished = isPublished;
        this.artisan = artisan;
        this.dateOfCreation = dateOfCreation;
        this.bibliographySource = bibliographySource;
        this.bibliographyReference = bibliographyReference;
        this.placement = placement;
        this.church = church;
        this.registeredBy = registeredBy;
        this.images.addAll(images);
        this.engravings.addAll(engravings);
        this.tags.addAll(tags);
    }

    public static Painting create(
            String title,
            String description,
            String artisan,
            String dateOfCreation,
            String bibliographySource,
            String bibliographyReference,
            String placement,
            Church church,
            User registeredBy,
            List<Image> images,
            List<Engraving> engravings,
            List<Tag> tags
    ) {
        if (images.size() == 0) {
            throw new IllegalArgumentException("Painting must have at least one image");
        }
        var isPublished = registeredBy.isAdmin();
        return new Painting(
                title,
                description,
                isPublished,
                artisan,
                dateOfCreation,
                bibliographySource,
                bibliographyReference,
                placement,
                church,
                registeredBy,
                images,
                engravings,
                tags
        );
    }

    public void update(
            String title,
            String description,
            String artisan,
            String dateOfCreation,
            String bibliographySource,
            String bibliographyReference,
            String placement,
            List<String> imageUrlsToRemove,
            List<Image> images,
            List<String> engravingUrlsToRemove,
            List<Engraving> engravings,
            List<Tag> tags,
            Church church,
            User user
    ) {
        if (isPublished && !user.isAdmin()) {
            throw new IllegalStateException("Only admins can update published paintings");
        }
        if (!user.isAdmin() && !user.getId().equals(registeredBy.getId())) {
            throw new IllegalStateException("Only the user who registered the painting can update it");
        }
        if (imageUrlsToRemove != null) {
            this.images.removeIf(image -> imageUrlsToRemove.contains(image.getUrl()));
        }
        if (this.images.size() + images.size() == 0) {
            throw new IllegalArgumentException("Painting must have at least one image");
        }
        if (engravingUrlsToRemove != null) {
            this.engravings.removeIf(engraving -> engravingUrlsToRemove.contains(engraving.getUrl()));
        }
        this.title = title;
        this.description = description;
        this.artisan = artisan;
        this.dateOfCreation = dateOfCreation;
        this.bibliographySource = bibliographySource;
        this.bibliographyReference = bibliographyReference;
        this.placement = placement;
        this.church = church;
        this.images.addAll(images);
        this.engravings.addAll(engravings);
        this.tags.addAll(tags);
    }

    public void publish() {
        isPublished = true;
    }
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public String getArtisan() {
        return artisan;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public String getBibliographySource() {
        return bibliographySource;
    }

    public String getBibliographyReference() {
        return bibliographyReference;
    }

    public String getPlacement() {
        return placement;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Engraving> getEngravings() {
        return engravings;
    }

    public Church getChurch() {
        return church;
    }

    public User getRegisteredBy() {
        return registeredBy;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
