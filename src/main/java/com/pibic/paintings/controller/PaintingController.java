package com.pibic.paintings.controller;

import com.pibic.paintings.PaintingService;
import com.pibic.paintings.dtos.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/paintings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PaintingController {
    @Inject
    PaintingService paintingService;
    @Context
    JsonWebToken jwt;


    @GET
    public Response getPaintings() {
        return Response.ok(paintingService.getPublishedPaintings()).build();
    }

    @RolesAllowed({"admin", "user"})
    @GET
    @Path("/authorized")
    public Response getAuthorizedPaintings(@QueryParam("filter") String filter) {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        List<PaintingsResponse> responses =paintingService.getAuthorizedPaintings(userId, filter);
        return Response.ok(responses).build();
    }

    @RolesAllowed({"admin", "user"})
    @GET
    @Path("/authorized/{id}")
    public Response getAuthorizedPaintingById(@PathParam("id") Long id, @QueryParam("filter") String filter) {
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        PaintingResponse painting = paintingService.getAuthorizedPaintingById(id, userId, filter);
        return Response.ok(painting).build();
    }

    @GET
    @Path("{id}")
    public Response getPaintingById(@PathParam("id") Long id){
        return Response.ok(paintingService.getPaintingById(id)).build();
    }

    @GET
    @Path("/artisans")
    public Response getArtisans(){
        return Response.ok(paintingService.getArtisans()).build();
    }

    @GET
    @Path("/tags/{tagName}")
    public Response getPaintingsByTag(@PathParam("tagName") String tagName){
        return Response.ok(paintingService.getPaintingsByTag(tagName)).build();
    }

    @RolesAllowed({"admin", "user"})
    @POST
    public Response createPainting(@Valid CreatePaintingRequest createPaintingRequest){
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        var paintingDto = new CreatePaintingDto(
                createPaintingRequest.title(),
                createPaintingRequest.description(),
                createPaintingRequest.artisan(),
                createPaintingRequest.dateOfCreation(),
                String.join("\n", createPaintingRequest.bibliographySource()),
                String.join("\n", createPaintingRequest.bibliographyReference()),
                createPaintingRequest.placement(),
                createPaintingRequest.churchId(),
                userId,
                createPaintingRequest.images()
                                        .stream()
                                        .map(i -> new ImageDto(i.base64Image(), i.photographer())).toList(),
                createPaintingRequest.engravingRequests()
                        .stream()
                        .map(e -> new EngravingDto(e.name(), e.base64Image(), e.createdBy())).toList(),
                createPaintingRequest.tagIds()
        );
        return Response
                    .ok(paintingService.createPainting(paintingDto))
                    .status(Response.Status.CREATED)
                    .build();
    }

    @RolesAllowed({"admin", "user"})
    @PUT
    @Path("{id}")
    public Response updatePainting(@PathParam("id") Long id, @Valid UpdatePaintingRequest updatePaintingRequest){
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        var paintingDto = new UpdatePaintingDto(
                id,
                updatePaintingRequest.title(),
                updatePaintingRequest.description(),
                updatePaintingRequest.artisan(),
                updatePaintingRequest.dateOfCreation(),
                String.join("\n", updatePaintingRequest.bibliographySource()),
                String.join("\n", updatePaintingRequest.bibliographyReference()),
                updatePaintingRequest.placement(),
                updatePaintingRequest.urlImagesToRemove(),
                updatePaintingRequest.images()
                        .stream()
                        .map(i -> new ImageDto(i.base64Image(), i.photographer())).toList(),
                updatePaintingRequest.urlEngravingsToRemove(),
                updatePaintingRequest.engravings()
                        .stream()
                        .map(e -> new EngravingDto(e.name(), e.base64Image(), e.createdBy())).toList(),
                updatePaintingRequest.tagIds(),
                updatePaintingRequest.churchId(),
                userId
        );
        paintingService.updatePainting(paintingDto);
        return Response.ok().build();
    }

    @RolesAllowed({"admin", "user"})
    @DELETE
    @Path("{id}")
    public Response deletePainting(@PathParam("id") Long id){
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        paintingService.deletePainting(id, userId);
        return Response.noContent().build();
    }

    @RolesAllowed({"user"})
    @PATCH
    @Path("{id}/add-suggestion")
    public Response addSuggestion(@PathParam("id") Long id, @Valid AddSuggestionRequest addSuggestionRequest){
        Long userId = Long.parseLong(jwt.getClaim("id").toString());
        paintingService.addSuggestion(
                id,
                userId,
                addSuggestionRequest.reason(),
                addSuggestionRequest.images()
                        .stream()
                        .map(i -> new ImageDto(i.base64Image(), i.photographer())).toList()
        );
        return Response.ok().build();
    }

    @RolesAllowed({"admin"})
    @PATCH
    @Path("{id}/add-answer-to-suggestion/{suggestionId}")
    public Response addAnswerToSuggestion(
            @PathParam("id") Long id,
            @PathParam("suggestionId") Long suggestionId,
            @Valid AddAnswer answer
    ){
        paintingService.addAnswerToSuggestion(id, suggestionId, answer.response());
        return Response.ok().build();
    }

    @RolesAllowed({"admin"})
    @PATCH
    @Path("{id}/publish")
    public Response publishPainting(@PathParam("id") Long id){
        paintingService.publishPainting(id);
        return Response.ok().build();
    }
}
