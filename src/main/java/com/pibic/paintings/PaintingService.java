package com.pibic.paintings;

import com.pibic.churches.ChurchRepository;
import com.pibic.paintings.dtos.*;
import com.pibic.shared.Image;
import com.pibic.tags.TagRepository;
import com.pibic.tags.dtos.TagDto;
import com.pibic.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class PaintingService {
    @Inject
    PaintingRepository paintingRepository;
    @Inject
    ChurchRepository churchRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    TagRepository tagRepository;

    public PaintingResponse getPaintingById(Long id) {
        Painting painting = paintingRepository
                .find("id = ?1 and isPublished = true", id)
                .firstResult();
        if (painting == null) {
            throw new NotFoundException("Painting not found");
        }
        return mappingToResponse(painting);
    }

    public List<PaintingResponse> getAllPaintings() {
        return paintingRepository
                .find("isPublished = true")
                .stream()
                .map(this::mappingToResponse)
                .toList();
    }

    @Transactional
    public Long createPainting(CreatePaintingDto createPaintingDto){
        var user = userRepository.findById(createPaintingDto.registeredById());
        if (user == null)
            throw new NotFoundException("User not found");
        var church = churchRepository.findById(createPaintingDto.churchId());
        if (church == null)
            throw new NotFoundException("Church not found");
        var tags = tagRepository.find("id in ?1", createPaintingDto.tagsIds()).list();
        var painting = Painting.create(
                createPaintingDto.title(),
                createPaintingDto.description(),
                createPaintingDto.artisan(),
                createPaintingDto.dateOfCreation(),
                createPaintingDto.bibliographySource(),
                createPaintingDto.bibliographyReference(),
                createPaintingDto.placement(),
                church,
                user,
                createPaintingDto.images().stream().map(imageDto -> new Image(imageDto.url(), imageDto.photographer())).toList(),
                createPaintingDto.engravings().stream().map(engravingDto -> new Engraving(engravingDto.name(), engravingDto.url(), engravingDto.createdBy())).toList(),
                tags
        );
        paintingRepository.persist(painting);
        return painting.getId();
    }

    private PaintingResponse mappingToResponse(Painting painting) {
        return new PaintingResponse(
                painting.getId(),
                painting.getTitle(),
                painting.getDescription(),
                painting.getArtisan(),
                painting.getDateOfCreation(),
                painting.getBibliographySource(),
                painting.getBibliographyReference(),
                painting.getPlacement(),
                painting.getRegisteredBy().getName(),
                painting.getImages().stream().map(image -> new ImageDto(image.getUrl(), image.getPhotographer())).toList(),
                painting.getEngravings().stream().map(engraving -> new EngravingDto(engraving.getName(), engraving.getUrl(), engraving.getCreatedBy())).toList(),
                new ChurchDto(
                        painting.getChurch().getId(),
                        painting.getChurch().getName(),
                        painting.getChurch().getAddress().city(),
                        painting.getChurch().getAddress().state(),
                        painting.getChurch().getAddress().street(),
                        painting.getChurch().getImages().stream().map(image -> new ImageDto(image.getUrl(), image.getPhotographer())).toList()
                ),
                painting.getTags().stream().map(tag -> new TagDto(tag.getId(), tag.getName())).toList()
        );
    }
}
