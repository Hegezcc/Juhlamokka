package juhlamokka.frontend.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;    
import juhlamokka.database.User;

@Path("user/{id}")
public class UserController {

    /**
     * Call: <code>/api/v1/user/23</code>
     * @param id
     * @return HTTP Response
     */
    @GET
    @Produces("application/json")
    public Response getResource(@PathParam("id") Integer id) {
        User user = new User(id, WebAPI.db);
        return Response.status(Status.OK).entity(user).build();
    }
}