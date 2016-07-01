package com.scuilion.confluence.macro.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.sal.api.net.ResponseException;
import com.scuilion.confluence.macro.StashConnectImpl;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/stash")
public class StashResource {

  private final StashConnectImpl stashConnectImpl;

  StashResource(@Nonnull StashConnectImpl stashConnectImpl) {
    this.stashConnectImpl = stashConnectImpl;
  }
    //http://kevino-thinkpad-t450s:1990/confluence/rest/stash/1.0/projects

  @GET
  @AnonymousAllowed
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response getMessage(@QueryParam("key") String key) throws ResponseException {
    return Response.ok(stashConnectImpl.callStashRequest("rest/api/1.0/projects")).build();
  }

}
