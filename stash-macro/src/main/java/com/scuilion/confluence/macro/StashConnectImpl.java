package com.scuilion.confluence.macro;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.application.stash.StashApplicationType;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.Response;
import com.atlassian.sal.api.net.ResponseException;
import com.atlassian.sal.api.net.ResponseHandler;
import com.atlassian.sal.api.net.ResponseStatusException;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

public class StashConnectImpl implements StashConnect {

    private static final Logger log = LoggerFactory.getLogger(StashConnectImpl.class);

    private final ApplicationLinkService applicationLinkService;

    public StashConnectImpl(ApplicationLinkService applicationLinkService) {
        this.applicationLinkService = applicationLinkService;
    }

    public String makeStashRequest(String url) throws ResponseException {
        return executeRequest(url).getResponse();
    }

    public StashResponseHandler executeRequest(String requestUrl) throws ResponseException {
        ApplicationLink stashApplicationLink = getStashApplicationLink();
        ApplicationLinkRequestFactory requestFactory = stashApplicationLink.createAuthenticatedRequestFactory();
        try {
            ApplicationLinkRequest request = requestFactory.createRequest(Request.MethodType.GET, requestUrl);
            StashResponseHandler stashResponseHandler = new StashConnectImpl.StashResponseHandler();
            request.execute(stashResponseHandler);
            return stashResponseHandler;
        } catch (CredentialsRequiredException e) {
            throw new ResponseException(e);
        } catch (ResponseException e) {
            log.error("Error getting Application Links", e);
            if (((e instanceof ResponseStatusException)) && (isUnauthorized((ResponseStatusException) e))) {
                throw new ResponseException(new CredentialsRequiredException(requestFactory, "authentication necessary"));
            }
            throw e;
        }
    }

    public ApplicationLink getStashApplicationLink() throws ResponseException {
        ApplicationLink applicationLink = this.applicationLinkService.getPrimaryApplicationLink(StashApplicationType.class);
        if (applicationLink == null) {
            applicationLink = (ApplicationLink) Iterables.getFirst(this.applicationLinkService.getApplicationLinks(StashApplicationType.class), null);
        }
        if (applicationLink == null) {
            throw new RuntimeException("Stash application link does not exist!");
        }
        return applicationLink;
    }

    private static class StashResponseHandler implements ResponseHandler<Response> {
        private String response;

        public void handle(Response response) throws ResponseException {
            if (!response.isSuccessful()) {
                throw new ResponseStatusException(response.getStatusText(), response);
            }
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(response.getResponseBodyAsStream(), writer, "UTF-8");
                this.response = writer.toString();
            } catch (IOException e) {
                throw new ResponseException(e);
            }
        }

        public String getResponse() {
            return this.response;
        }
    }

    private boolean isUnauthorized(ResponseStatusException e) {
        return e.getResponse().getStatusCode() == 401;
    }
}