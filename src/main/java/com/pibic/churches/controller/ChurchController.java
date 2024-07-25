package com.pibic.churches.controller;

import com.pibic.churches.ChurchService;
import com.pibic.churches.dtos.ChurchImageDto;
import com.pibic.churches.dtos.CreateChurchDto;
import com.pibic.churches.dtos.UpdateChurchDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/churches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ChurchController {
    @Inject
    private ChurchService churchService;
    private final Long userId = 1L;

    @GET
    public Response getChurches() {
        var churches = churchService.getChurches();
        return Response.ok(churches).build();
    }

    @GET
    @Path("/{id}")
    public Response getChurch(@PathParam("id") Long id) {
        var church = churchService.getChurch(id);
        return Response.ok(church).build();
    }

    @POST
    public Response createChurch(@Valid CreateChurchRequest createChurchRequest) {
        var createChurchDto = new CreateChurchDto(
                createChurchRequest.name(),
                createChurchRequest.description(),
                String.join(";", createChurchRequest.bibliographyReference()),
                createChurchRequest.street(),
                createChurchRequest.city(),
                createChurchRequest.state(),
                userId,
                createChurchRequest.images()
                                    .stream()
                                    .map(image -> new ChurchImageDto(image.base64Image(), image.photographer())).toList()
        );
        var churchId = churchService.createChurch(createChurchDto);
        return Response.ok(churchId).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateChurch(@PathParam("id") Long id, @Valid UpdateChurchRequest updateChurchRequest) {
        var updateChurchDto = new UpdateChurchDto(
                id,
                updateChurchRequest.name(),
                updateChurchRequest.description(),
                updateChurchRequest.bibliographyReference(),
                updateChurchRequest.street(),
                updateChurchRequest.city(),
                updateChurchRequest.state(),
                updateChurchRequest.imageUrlsToBeRemoved(),
                updateChurchRequest.images()
                                    .stream()
                                    .map(image -> new ChurchImageDto(image.base64Image(), image.photographer())).toList(),
                userId
        );
        churchService.updateChurch(updateChurchDto);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteChurch(@PathParam("id") Long id) {
        churchService.deleteChurch(id, userId);
        return Response.noContent().build();
    }
}
