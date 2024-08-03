package com.pibic.paintings;

import com.pibic.churches.ChurchRepository;
import com.pibic.paintings.dtos.*;
import com.pibic.shared.Image;
import com.pibic.shared.ImageHelper;
import com.pibic.shared.abstraction.IStorageService;
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
    private static final String BLOB_CONTAINER_PAINTING = "paintings";
    private static final String BLOB_CONTAINER_ENGRAVING = "engravings";
    @Inject
    PaintingRepository paintingRepository;
    @Inject
    ChurchRepository churchRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    TagRepository tagRepository;
    @Inject
    IStorageService storageService;

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
                .list()
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
                getImageWithUrls(createPaintingDto.title(), createPaintingDto.images()),
                getEngravingWithUrls(createPaintingDto.title(), createPaintingDto.engravings()),
                tags
        );
        paintingRepository.persist(painting);
        return painting.getId();
    }

    @Transactional
    public void updatePainting(UpdatePaintingDto updatePaintingDto){
        var painting = paintingRepository.findById(updatePaintingDto.id());
        if (painting == null)
            throw new NotFoundException("Painting not found");
        var user = userRepository.findById(updatePaintingDto.userId());
        if (user == null)
            throw new NotFoundException("User not found");
        var church = churchRepository.findById(updatePaintingDto.churchId());
        if (church == null)
            throw new NotFoundException("Church not found");
        var tags = tagRepository.find("id in ?1", updatePaintingDto.tags()).list();
        painting.update(
                updatePaintingDto.title(),
                updatePaintingDto.description(),
                updatePaintingDto.artisan(),
                updatePaintingDto.dateOfCreation(),
                updatePaintingDto.bibliographySource(),
                updatePaintingDto.bibliographyReference(),
                updatePaintingDto.placement(),
                updatePaintingDto.imagesUrlsToRemove(),
                getImageWithUrls(updatePaintingDto.title(), updatePaintingDto.images()),
                updatePaintingDto.engravingsUrlsToRemove(),
                getEngravingWithUrls(updatePaintingDto.title(), updatePaintingDto.engravings()),
                tags,
                church,
                user
        );
        if (!updatePaintingDto.imagesUrlsToRemove().isEmpty())
            updatePaintingDto.imagesUrlsToRemove().forEach(imageUrl -> storageService.deleteFile(BLOB_CONTAINER_PAINTING, imageUrl));
        if (!updatePaintingDto.engravingsUrlsToRemove().isEmpty())
            updatePaintingDto.engravingsUrlsToRemove().forEach(engravingUrl -> storageService.deleteFile(BLOB_CONTAINER_ENGRAVING, engravingUrl));
    }

    @Transactional
    public void deletePainting(Long id, Long userId) {
        var painting = paintingRepository.findById(id);
        if (painting == null) {
            throw new NotFoundException("Painting not found");
        }
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (painting.isPublished() && !user.isAdmin()) {
            throw new IllegalStateException("Only admins can delete published paintings");
        }
        if (!user.isAdmin() && !painting.getRegisteredBy().getId().equals(userId)) {
            throw new IllegalStateException("Only the user who registered the painting can delete it");
        }
        paintingRepository.delete(painting);
        painting.getImages().forEach(image -> storageService.deleteFile(BLOB_CONTAINER_PAINTING, image.getUrl()));
        painting.getEngravings().forEach(engraving -> storageService.deleteFile(BLOB_CONTAINER_ENGRAVING, engraving.getUrl()));
    }

    @Transactional
    public void addSuggestion(Long id, Long userId, String reason, List<ImageDto> images){
        var painting = paintingRepository.findById(id);
        if (painting == null)
            throw new NotFoundException("Painting not found");
        var user = userRepository.findById(userId);
        if (user == null)
            throw new NotFoundException("User not found");
        var suggestion = new Suggestion(reason, user, getImageWithUrls(painting.getTitle(), images));
        painting.addSuggestion(suggestion);
    }

    @Transactional
    public void addAnswerToSuggestion(Long id, Long suggestionId, String message){
        var painting = paintingRepository.findById(id);
        if (painting == null)
            throw new NotFoundException("Painting not found");
        painting.addAnswerToSuggestion(suggestionId, message);
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
    private List<Image> getImageWithUrls(String paintingName, List<ImageDto> images){
        return images
                .stream()
                .map(imageDto -> new Image(
                        storageService.uploadFile(
                                BLOB_CONTAINER_PAINTING,
                                ImageHelper.getImageName(paintingName, imageDto.base64Image()),
                                ImageHelper.getBase64ContentStream(imageDto.base64Image())),
                        imageDto.photographer()
                ))
                .toList();
    }
    private List<Engraving> getEngravingWithUrls(String paintingName, List<EngravingDto> engravings){
        return engravings
                .stream()
                .map(engravingDto -> new Engraving(
                        engravingDto.name(),
                        engravingDto.createdBy(),
                        storageService.uploadFile(
                                BLOB_CONTAINER_ENGRAVING,
                                ImageHelper.getImageName(paintingName, engravingDto.base64Image()),
                                ImageHelper.getBase64ContentStream(engravingDto.base64Image())
                        )
                ))
                .toList();
    }
}
