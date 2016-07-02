package com.scuilion.confluence.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Map;

public class StashMacro implements Macro {

    private static final Logger log = LoggerFactory.getLogger(StashMacro.class);

    private final XhtmlContent xhtmlUtils;
    private final StashConnectImpl stashConnectImpl;

    public StashMacro(@Nonnull StashConnectImpl stashConnectImpl, SpaceManager spaceManger, @Nonnull XhtmlContent xhtmlUtils) {
        this.stashConnectImpl = stashConnectImpl;
        this.xhtmlUtils = xhtmlUtils;
    }

    @Override
    public String execute(Map<String, String> parameters, String bodyContent, ConversionContext conversionContext) throws MacroExecutionException
    {
        Map velocityContext = MacroUtils.defaultVelocityContext();
        velocityContext.put("a_value", parameters.get("project"));
        velocityContext.put("b_value", parameters.get("repo"));
        velocityContext.put("c_value", parameters.get("filter"));
        return VelocityUtils.getRenderedTemplate("templates/branches-with-filters.vm", velocityContext);
    }
    
    @Override
    public BodyType getBodyType()
    {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType()
    {
        return OutputType.BLOCK;
    }
}