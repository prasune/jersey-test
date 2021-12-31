package com.test.resource;

import com.test.service.GreetingService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/greetings")
public class GreetingResource {

    @Inject
    private GreetingService greetingService;

    @GET
    @Path("/{greetingId}")
    @Produces({ "application/json" })
    public Response getHiGreeting(@PathParam("greetingId") String greetingId,
                                  @Context HttpServletRequest httpServletRequest) {
        Response response = greetingService.doSomeProcessing(greetingId, "hdeg", httpServletRequest);
        return response;
    }
}
