package com.scuilion.confluence.macro;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.EntityLinkService;
import com.atlassian.applinks.api.application.stash.StashApplicationType;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.ResponseException;
import com.atlassian.sal.api.net.Response;
import com.atlassian.sal.api.net.ResponseStatusException;
import com.google.common.collect.Iterables;
import com.atlassian.sal.api.net.ResponseHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.StringWriter;

public class StashConnectImpl implements StashConnect {

    private static final Logger log = LoggerFactory.getLogger(StashConnectImpl.class);

    private final ApplicationLinkService applicationLinkService;
    private final EntityLinkService entityLinkService;
    private final I18nResolver i18n;

    public StashConnectImpl(ApplicationLinkService applicationLinkService, EntityLinkService entityLinkService, I18nResolver i18n) {
        this.applicationLinkService = applicationLinkService;
        this.entityLinkService = entityLinkService;
        this.i18n = i18n;
    }

    public String callStashRequest(String url) throws ResponseException {
        return executeRequest(url).getResponse();
    }

    public StashResponseHandler executeRequest(String requestUrl) throws ResponseException {

        log.error("trying a request");
        logApps();
        ApplicationLink stashApplicationLink = getStashApplicationLink();
        ApplicationLinkRequestFactory requestFactory = stashApplicationLink.createAuthenticatedRequestFactory();
        try {
            ApplicationLinkRequest request = requestFactory.createRequest(Request.MethodType.GET, requestUrl);
            StashResponseHandler stashResponseHandler = new StashConnectImpl.StashResponseHandler(SupportedFileEncoding.UTF_8);
            request.execute(stashResponseHandler);
            return stashResponseHandler;
        } catch (CredentialsRequiredException e) {
            throw new ResponseException(e);
        } catch (ResponseException e) {
            log.error("kevin has an error ", e);
            if (((e instanceof ResponseStatusException)) && (isUnauthorized((ResponseStatusException) e))) {
                throw new ResponseException(new CredentialsRequiredException(requestFactory, "authentication necessary"));
            }
            throw e;
        }
    }
    private void logApps(){
        for(ApplicationLink t : this.applicationLinkService.getApplicationLinks(StashApplicationType.class)){
            log.error("-------------");
            log.error(t.getName());
            log.error(t.getDisplayUrl().toString());
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
    private static class StashResponseHandler
        implements ResponseHandler<Response>
    {
        private final SupportedFileEncoding fileEncoding;
        private String response;
        private String stashUser;

        public StashResponseHandler(SupportedFileEncoding fileEncoding)
        {
            this.fileEncoding = fileEncoding;
        }

        public void handle(Response response)
            throws ResponseException
        {
            log.error("does it get here");
            if(response !=null && response.getResponseBodyAsStream() !=null) {
                log.error("XXX:" + response.getResponseBodyAsString());
                log.error(String.valueOf(response.getHeaders()));
            }
            if (!response.isSuccessful()) {
                throw new ResponseStatusException(response.getStatusText(), response);
            }
            try
            {
                StringWriter writer = new StringWriter();
                IOUtils.copy(response.getResponseBodyAsStream(), writer, this.fileEncoding.getCharset().name());
                this.response = writer.toString();
            }
            catch (IOException e)
            {
                throw new ResponseException(e);
            }
            this.stashUser = response.getHeader("X-AUSERNAME");
        }

        public String getStashUser()
        {
            return this.stashUser;
        }

        public String getResponse()
        {
            return this.response;
        }
    }

    private boolean isUnauthorized(ResponseStatusException e) {
        return e.getResponse().getStatusCode() == 401;
    }
}