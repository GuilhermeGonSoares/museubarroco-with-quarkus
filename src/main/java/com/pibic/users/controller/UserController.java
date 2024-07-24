package com.pibic.users.controller;

import com.pibic.users.UserServices;
import com.pibic.users.dtos.CreateUserDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserController {
    @Inject
    UserServices userServices;

    @POST
    public Response createUser(CreateUserRequest request) {
        var user = userServices.createUser(
                new CreateUserDto(
                        request.name(),
                        request.email(),
                        request.password(),
                        request.admin()
                )
        );
        return Response.ok(user.getId()).build();
    }

    @GET()
    public Response getUsers() {
        return Response.ok(userServices.listAllUsers()).build();
    }
}
