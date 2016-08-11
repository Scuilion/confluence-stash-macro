package com.scuilion.confluence.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.sal.api.net.ResponseException;
import com.google.common.base.Strings;
import com.scuilion.confluence.macro.rest.dto.Branch;
import com.scuilion.confluence.macro.rest.dto.Branches;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StashMacro implements Macro {

    private static final Logger log = LoggerFactory.getLogger(StashMacro.class);

    private final StashConnectImpl stashConnectImpl;

    public StashMacro(@Nonnull final StashConnectImpl stashConnectImpl) {
        this.stashConnectImpl = stashConnectImpl;
    }

    @Override
    public String execute(final Map<String, String> parameters, final String bodyContent, final ConversionContext conversionContext) throws MacroExecutionException {
        final String project = parameters.get("project");
        final String repo = parameters.get("repo");
        final String repoName = parameters.get("repoName");
        final String filter = parameters.get("filter");
        final List<String> branches = retrieveBranches(project, repo, filter);
        final Map velocityContext = MacroUtils.defaultVelocityContext();
        velocityContext.put("repoName", repoName);
        velocityContext.put("branches", branches);
        velocityContext.put("linkUrl", createRepoLinkUrl(project, repo));
        return VelocityUtils.getRenderedTemplate("templates/branches-with-filters.vm", velocityContext);
    }

    private String createRepoLinkUrl(final String project, final String repo ){
//            http://kevino-ThinkPad-T450s:7990/stash
//            http://kevino-thinkpad-t450s:7990/stash/projects/PROJECT_1/repos/rep_1/browse
        String fullUrl = "";
        try {
            final UriBuilder uriBuilder = UriBuilder.fromPath(stashConnectImpl.getStashApplicationLink().getDisplayUrl().toString());
            fullUrl = uriBuilder
                .path("projects")
                .path(project)
                .path("repos")
                .path(repo)
                .path("browse").build().toString();
        } catch (final ResponseException e) {
        }
        return fullUrl;
    }

    private List<String> retrieveBranches(final String project, final String repo, final String filter) {
        final UriBuilder uriBuilder = UriBuilder.fromPath("")
            .path("rest/api/1.0/projects/")
            .path(project)
            .path("repos")
            .path(repo)
            .path("branches");

        if (!Strings.isNullOrEmpty(filter)) {
            uriBuilder.queryParam("filterText", filter);
        }
        Branches branches = new Branches();
        try {
            final String response = stashConnectImpl.makeStashRequest(uriBuilder.build().toString());
            final ObjectMapper objectMapper = new ObjectMapper();
            branches = objectMapper.readValue(response, Branches.class);
        } catch (final ResponseException re) {
            log.error("Failure to find branches.", re);
        } catch (final IOException ioe) {
            log.error("Failure to parse branches.", ioe);
        }
        final List<String> branchNames = new ArrayList<>();
        for (final Branch b : branches.getValues()) {
            branchNames.add(b.getDisplayId());
        }
        return branchNames;
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}