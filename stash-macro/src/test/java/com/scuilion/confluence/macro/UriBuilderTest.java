package com.scuilion.confluence.macro;

import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;

public class UriBuilderTest {

    @Test
    public void eh() throws MalformedURLException {
        String project = "project";
        String repo = "repo";
        String filter = "filter";

        UriBuilder uriBuilder = UriBuilder.fromPath("")
            .path("rest/api/1.0/projects/")
            .path(project)
            .path("repos")
            .path(repo)
            .path("branches")
            .queryParam("filterText", filter);

        System.out.println(uriBuilder.build().toString());
    }
}
