package org.problemchimp.vectorclock.http;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.problemchimp.http.Endpoint;
import org.problemchimp.http.HelloEndpoint;
import org.problemchimp.vectorclock.handler.VectorClock;
import org.problemchimp.vectorclock.handler.VectorClockMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * HTTP endpoint for the vector clock.
 */
public class ClockEndpoint extends Endpoint<VectorClockMessage> {

    @Autowired VectorClock vectorClock;

    /**
     * Jersey annotations aren't inherited, but you can still import them by doing
     * this. With thanks to
     * https://stackoverflow.com/questions/33694296/will-jax-rs-jersey-resource-paths-honor-inheritance
     */
    @Path("/")
    public HelloEndpoint getBaseResource() {
	return new HelloEndpoint();
    }

    @GET
    @Path("/clock")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clock() {
	return Response.ok().entity(vectorClock).build();
    }
}
