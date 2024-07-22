package com.pibic.churches;

import com.pibic.churches.dtos.ChurchDto;
import com.pibic.churches.dtos.CreateChurchDto;
import com.pibic.shared.Image;
import com.pibic.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ChurchService {
    @Inject
    ChurchRepository churchRepository;
    @Inject
    UserRepository userRepository;

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
                    createChurchDto.images().stream()
                            .map(imageDto -> new Image(imageDto.url(), imageDto.photographer()))
                            .toList());
        churchRepository.persist(church);
        return church.getId();
    }

    public ChurchDto getChurch(Long id) {
        var church = churchRepository.findById(id);
        if (church == null) {
           throw new NotFoundException("Church not found");
        }
        return new ChurchDto(church.getName(), church.getAddress().street(),
                church.getAddress().city(), church.getAddress().state());
    }
}
