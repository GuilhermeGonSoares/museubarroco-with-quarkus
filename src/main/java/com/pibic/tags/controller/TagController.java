package com.pibic.tags.controller;

import com.pibic.tags.TagServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TagController {
    @Inject
    TagServices tagServices;

    private final Long userId = 1L;

    @GET
    public Response getAllTags() {
        var tags = tagServices.getAllTags();
        return Response.ok(tags).build();
    }

    @POST
    public Response createTag(CreateTagRequest createTagRequest) {
        var tag = tagServices.createTag(createTagRequest.name(), userId);
        return Response.ok(tag.id()).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTag(@PathParam("id") Long id, UpdateTagRequest updateTagRequest) {
        var tag = tagServices.updateTag(id, updateTagRequest.name(), userId);
        return Response.ok(tag).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") Long id) {
        tagServices.deleteTag(id, userId);
        return Response.noContent().build();
    }
}
