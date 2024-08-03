package com.pibic.paintings.controller;

import com.pibic.paintings.PaintingService;
import com.pibic.paintings.dtos.CreatePaintingDto;
import com.pibic.paintings.dtos.EngravingDto;
import com.pibic.paintings.dtos.ImageDto;
import com.pibic.paintings.dtos.UpdatePaintingDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/paintings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PaintingController {
    @Inject
    PaintingService paintingService;
    private static final Long userId = 1L;

    @GET
    public Response getPaintings() {
        return Response.ok(paintingService.getAllPaintings()).build();
    }

    @GET
    @Path("{id}")
    public Response getPaintingById(@PathParam("id") Long id){
        return Response.ok(paintingService.getPaintingById(id)).build();
    }

    @POST
    public Response createPainting(@Valid CreatePaintingRequest createPaintingRequest){
        var paintingDto = new CreatePaintingDto(
                createPaintingRequest.title(),
                createPaintingRequest.description(),
                createPaintingRequest.dateOfCreation(),
                createPaintingRequest.bibliographySource(),
                String.join(";", createPaintingRequest.bibliographyReference()),
                createPaintingRequest.placement(),
                createPaintingRequest.artisan(),
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

    @PUT
    @Path("{id}")
    public Response updatePainting(@PathParam("id") Long id, @Valid UpdatePaintingRequest updatePaintingRequest){
        var paintingDto = new UpdatePaintingDto(
                id,
                updatePaintingRequest.title(),
                updatePaintingRequest.description(),
                updatePaintingRequest.artisan(),
                updatePaintingRequest.dateOfCreation(),
                updatePaintingRequest.bibliographySource(),
                String.join(";", updatePaintingRequest.bibliographyReference()),
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

    @DELETE
    @Path("{id}")
    public Response deletePainting(@PathParam("id") Long id){
        paintingService.deletePainting(id, userId);
        return Response.noContent().build();
    }

    @PATCH
    @Path("{id}/add-suggestion")
    public Response addSuggestion(@PathParam("id") Long id, @Valid AddSuggestionRequest addSuggestionRequest){
        paintingService.addSuggestion(
                id,
                51L,
                addSuggestionRequest.reason(),
                addSuggestionRequest.images()
                        .stream()
                        .map(i -> new ImageDto(i.base64Image(), i.photographer())).toList()
        );
        return Response.ok().build();
    }

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
}
