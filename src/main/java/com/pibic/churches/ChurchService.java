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
                    createChurchDto.bibliographySource(),
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
                LEFT JOIN FETCH p.tags t
                WHERE p.church = ?1
                AND p.isPublished = true AND t.isPublished = true
                """, church);
        church.setPaintings(paintings);
        return ChurchResponse.fromChurch(church);
    }

    public AuthorizedChurchResponse getAuthorizedChurch(Long id, Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var sql = new StringBuilder("""
                SELECT c FROM Church c
                LEFT JOIN FETCH c.images i
                WHERE c.id = ?1
                """);
        if (!user.isAdmin()) {
            sql.append("AND c.registeredBy.id = ").append(userId);
        }
        var church = churchRepository.find(sql.toString(), id).firstResult();
        if (church == null) {
            throw new NotFoundException("Church not found");
        }
        return AuthorizedChurchResponse.fromChurch(church);
    }

    public List<ChurchesResponse> getChurches(String state) {
        var sql = new StringBuilder("""
                    SELECT c FROM Church c
                    LEFT JOIN FETCH c.images
                    WHERE c.isPublished = true
                   """);
        if (state != null) {
            sql.append(" AND c.address.state = '").append(state).append("'");
        }
        return churchRepository.list(sql.toString())
                .stream()
                .map(ChurchesResponse::fromChurch)
                .toList();
    }

    public List<AuthorizedChurchResponse> getAuthorizedChurches(Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var sql = new StringBuilder("""
                SELECT c FROM Church c
                LEFT JOIN FETCH c.images i
                WHERE 1 = 1
                """);
        if (!user.isAdmin()) {
            sql.append("AND c.registeredBy.id = ").append(userId);
        }
        return churchRepository.list(sql.toString())
                .stream()
                .map(AuthorizedChurchResponse::fromChurch)
                .toList();
    }

    public List<AvailableChurchResponse> getAvailableChurches(Long userId) {
        var user = userRepository.findByIdOptional(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (user.isAdmin()){
            return churchRepository.list("isPublished = ?1", true)
                    .stream()
                    .map(AvailableChurchResponse::fromChurch)
                    .toList();
        }
        return churchRepository.list("isPublished = ?1 or registeredBy.id = ?2", true, userId)
                .stream()
                .map(AvailableChurchResponse::fromChurch)
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
                updateChurchDto.bibliographySource(),
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

    @Transactional
    public void publishChurch(Long id, Long userId) {
        var church = churchRepository.findById(id);
        if (church == null) {
            throw new NotFoundException("Church not found");
        }
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        church.publish();
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
