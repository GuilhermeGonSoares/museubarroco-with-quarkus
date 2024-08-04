package com.pibic.users.controller;

import com.pibic.users.UserServices;
import com.pibic.users.dtos.RegisterUserDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserController {
    @Inject
    UserServices userServices;
    @Context
    JsonWebToken jwt;

    @Path("/register")
    @POST
    public Response registerUser(@Valid RegisterUserRequest request) {
        var user = userServices.registerUser(
                new RegisterUserDto(
                        request.name(),
                        request.email(),
                        request.password()
                )
        );
        return Response.ok(user.getId()).status(Response.Status.CREATED).build();
    }
    @Path("/login")
    @POST
    public Response getCredential(@Valid LoginUserRequest request) {
        var token = userServices.getToken(request.email(), request.password());
        return Response.ok(token).build();
    }

    @GET()
    @RolesAllowed({"admin"})
    public Response getUsers() {
        Long userId = Long.valueOf(jwt.getClaim("id").toString());
        var x = jwt.getClaim("groups");
        var y = jwt.getExpirationTime();
        String userName = jwt.getClaim("name");
        String userEmail = jwt.getClaim("upn");

        return Response.ok(userServices.listAllUsers()).build();
    }
}
