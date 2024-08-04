package com.pibic.tags.controller;

import com.pibic.tags.TagServices;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TagController {
    @Inject
    TagServices tagServices;
    @Context
    JsonWebToken jwt;

    @GET
    public Response getAllTags() {
        var tags = tagServices.getAllTags();
        return Response.ok(tags).build();
    }

    @RolesAllowed({"admin", "user"})
    @POST
    public Response createTag(@Valid CreateTagRequest createTagRequest) {
        var userId = Long.parseLong(jwt.getClaim("id").toString());
        var tag = tagServices.createTag(createTagRequest.name(), userId);
        return Response.ok(tag.id()).status(Response.Status.CREATED).build();
    }

    @RolesAllowed({"admin", "user"})
    @PUT
    @Path("/{id}")
    public Response updateTag(@PathParam("id") Long id, @Valid UpdateTagRequest updateTagRequest) {
        var userId = Long.parseLong(jwt.getClaim("id").toString());
        var tag = tagServices.updateTag(id, updateTagRequest.name(), userId);
        return Response.ok(tag).build();
    }

    @RolesAllowed({"admin", "user"})
    @DELETE
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") Long id) {
        var userId = Long.parseLong(jwt.getClaim("id").toString());
        tagServices.deleteTag(id, userId);
        return Response.noContent().build();
    }
}
