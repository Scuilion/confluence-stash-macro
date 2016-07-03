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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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

    public StashMacro(@Nonnull StashConnectImpl stashConnectImpl) {
        this.stashConnectImpl = stashConnectImpl;
    }

    @Override
    public String execute(Map<String, String> parameters, String bodyContent, ConversionContext conversionContext) throws MacroExecutionException {
        String project = parameters.get("project");
        String repo = parameters.get("repo");
        String filter = parameters.get("filter");
        List<String> branches = retrieveBranches(project, repo, filter);
        Map velocityContext = MacroUtils.defaultVelocityContext();
        velocityContext.put("repo", repo);
        velocityContext.put("branches", branches);
        return VelocityUtils.getRenderedTemplate("templates/branches-with-filters.vm", velocityContext);
    }

    private List<String> retrieveBranches(String project, String repo, String filter) {
        UriBuilder uriBuilder = UriBuilder.fromPath("")
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
            String response = stashConnectImpl.makeStashRequest(uriBuilder.build().toString());
            ObjectMapper objectMapper = new ObjectMapper();
            branches = objectMapper.readValue(response, Branches.class);
        } catch (ResponseException re) {
            log.error("Failure to find branches.", re);
        } catch (JsonParseException jpe) {
            log.error("Failure to parse branches.", jpe);
        } catch (JsonMappingException jme) {
            log.error("Failure to parse branches.", jme);
        } catch (IOException ioe) {
            log.error("Failure to parse branches.", ioe);
        }
        List<String> branchNames = new ArrayList<>();
        for (Branch b : branches.getValues()) {
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