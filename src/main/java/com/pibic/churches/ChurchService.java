package com.pibic.churches;

import com.pibic.churches.dtos.*;
import com.pibic.paintings.PaintingRepository;
import com.pibic.shared.images.Image;
import com.pibic.shared.images.ImageHelper;
import com.pibic.shared.abstraction.IStorageService;
import com.pibic.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class ChurchService {
    private static final String BLOB_CONTAINER = "churches";
    @Inject
    ChurchRepository churchRepository;
    @Inject
    PaintingRepository paintingRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    IStorageService storageService;

    @Transactional
    public Long createChurch(CreateChurchDto createChurchDto) {
        var user = userRepository.findById(createChurchDto.registeredBy());
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var church = Church.create(
                    createChurchDto.name(),
                    new Address(createChurchDto.street(), createChurchDto.city(), createChurchDto.state()),
                    createChurchDto.description(),
                    createChurchDto.bibliographyReferences(),
                    user,
                    getImageUrls(createChurchDto.name(), createChurchDto.images())
        );
        churchRepository.persist(church);
        return church.getId();
    }

    public ChurchResponse getChurch(Long id) {
        var church = churchRepository.find("""
                SELECT c FROM Church c
                LEFT JOIN FETCH c.images i
                WHERE c.id = ?1 AND c.isPublished = true
                """, id).firstResult();
        if (church == null) {
           throw new NotFoundException("Church not found");
        }
        var paintings = paintingRepository.list("""
                SELECT p FROM Painting p
                LEFT JOIN FETCH p.images
                LEFT JOIN FETCH p.tags
                WHERE p.church = ?1
                """, church);
        church.setPaintings(paintings);
        return ChurchResponse.fromChurch(church);
    }

    public List<ChurchesResponse> getChurches() {
        return churchRepository.list("""
                        SELECT c FROM Church c
                        LEFT JOIN FETCH c.images
                        WHERE c.isPublished = true
                        """)
                .stream()
                .map(ChurchesResponse::fromChurch)
                .toList();
    }

    @Transactional
    public void updateChurch(UpdateChurchDto updateChurchDto) {
        var church = churchRepository.findById(updateChurchDto.id());
        if (church == null) {
            throw new NotFoundException("Church not found");
        }
        var user = userRepository.findById(updateChurchDto.updatedBy());
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        church.update(
                updateChurchDto.name(),
                new Address(updateChurchDto.street(), updateChurchDto.city(), updateChurchDto.state()),
                updateChurchDto.description(),
                updateChurchDto.bibliographyReferences(),
                updateChurchDto.imageUrlsToBeRemoved(),
                getImageUrls(updateChurchDto.name(), updateChurchDto.images()),
                user
        );
        if (!updateChurchDto.imageUrlsToBeRemoved().isEmpty())
            updateChurchDto.imageUrlsToBeRemoved().forEach(imageUrl -> storageService.deleteFile(BLOB_CONTAINER, imageUrl));
    }

    @Transactional
    public Long deleteChurch(Long id, Long userId) {
        var church = churchRepository.findById(id);
        if (church == null) {
            throw new NotFoundException("Church not found");
        }
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (church.isPublished() && !user.isAdmin()) {
            throw new IllegalStateException("Only admins can delete published churches");
        }
        if (!user.isAdmin() && !church.getRegisteredBy().getId().equals(userId)) {
            throw new IllegalStateException("Only the user who registered the church can delete it");
        }
        churchRepository.delete(church);
        church.getImages().forEach(image -> storageService.deleteFile(BLOB_CONTAINER, image.getUrl()));
        return id;
    }

    private List<Image> getImageUrls(String churchName, List<ChurchImageDto> images) {
        return images.stream()
                .map(imageDto -> new Image(
                        storageService.uploadFile(
                                BLOB_CONTAINER,
                                ImageHelper.getImageName(churchName, imageDto.base64Image()),
                                ImageHelper.getBase64ContentStream(imageDto.base64Image())),
                        imageDto.photographer())
                )
                .toList();
    }

}
