package com.pibic.churches.controller;

import com.pibic.churches.ChurchService;
import com.pibic.churches.dtos.ChurchImageDto;
import com.pibic.churches.dtos.CreateChurchDto;
import com.pibic.churches.dtos.UpdateChurchDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/churches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ChurchController {
    @Inject
    private ChurchService churchService;

    @Context
    JsonWebToken jwt;

    @GET
    public Response getChurches(@QueryParam("state") String state) {
        var churches = churchService.getChurches(state);
        return Response.ok(churches).build();
    }

    @RolesAllowed({"admin", "user"})
    @GET
    @Path("/authorized")
    public Response getAuthorizedChurches() {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        var churches = churchService.getAuthorizedChurches(userId);
        return Response.ok(churches).build();
    }

    @GET
    @Path("/{id}")
    public Response getChurch(@PathParam("id") Long id) {
        var church = churchService.getChurch(id);
        return Response.ok(church).build();
    }

    @RolesAllowed({"admin", "user"})
    @GET
    @Path("/authorized/{id}")
    public Response getAuthorizedChurch(@PathParam("id") Long id) {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        var church = churchService.getAuthorizedChurch(id, userId);
        return Response.ok(church).build();
    }

    @RolesAllowed({"admin", "user"})
    @GET
    @Path("/available")
    public Response getAvailableChurch() {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        var church = churchService.getAvailableChurches(userId);
        return Response.ok(church).build();
    }

    @RolesAllowed({"admin", "user"})
    @POST
    public Response createChurch(@Valid CreateChurchRequest createChurchRequest) {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        var createChurchDto = new CreateChurchDto(
                createChurchRequest.name(),
                createChurchRequest.description(),
                String.join("\n", createChurchRequest.bibliographyReference()),
                String.join("\n", createChurchRequest.bibliographySource()),
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

    @RolesAllowed({"admin", "user"})
    @PUT
    @Path("/{id}")
    public Response updateChurch(@PathParam("id") Long id, @Valid UpdateChurchRequest updateChurchRequest) {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        var updateChurchDto = new UpdateChurchDto(
                id,
                updateChurchRequest.name(),
                updateChurchRequest.description(),
                String.join("\n", updateChurchRequest.bibliographyReference()),
                String.join("\n", updateChurchRequest.bibliographySource()),
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

    @RolesAllowed({"admin", "user"})
    @DELETE
    @Path("/{id}")
    public Response deleteChurch(@PathParam("id") Long id) {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        churchService.deleteChurch(id, userId);
        return Response.noContent().build();
    }

    @RolesAllowed({"admin"})
    @PATCH
    @Path("/{id}/publish")
    public Response publishChurch(@PathParam("id") Long id) {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        churchService.publishChurch(id, userId);
        return Response.ok().build();
    }
}
