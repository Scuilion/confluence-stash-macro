package com.scuilion.confluence.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.XhtmlContent;

import javax.annotation.Nonnull;
import java.util.Map;

public class StashMacro implements Macro
{
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


//        String body = conversionContext.getEntity().getBodyAsString();
//
//        final List<MacroDefinition> macros = new ArrayList<MacroDefinition>();
//
//        try
//        {
//            xhtmlUtils.handleMacroDefinitions(body, conversionContext, new MacroDefinitionHandler()
//            {
//                @Override
//                public void handle(MacroDefinition macroDefinition)
//                {
//                    macros.add(macroDefinition);
//                }
//            });
//        }
//        catch (XhtmlException e)
//        {
//            throw new MacroExecutionException(e);
//        }
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("<p>");
//        if (!macros.isEmpty())
//        {
//            builder.append("<table width=\"50%\">");
//            builder.append("<tr><th>Macro Name</th><th>Has Body?</th></tr>");
//            for (MacroDefinition defn : macros)
//            {
//                builder.append("<tr>");
//                builder.append("<td>").append(defn.getName()).append("</td><td>").append(defn.hasBody()).append("</td>");
//                builder.append("</tr>");
//            }
//            builder.append("</table>");
//            String lastLine = "init";
//            try {
//                lastLine = stashConnectImpl.getSomething("rest/api/1.0/projects");
//            } catch (ResponseException e) {
//                lastLine = e.toString();
//            }
//            return lastLine;
//        }
//        else
//        {
//            builder.append("You've done built yourself a macro! Nice work.");
//        }
//        builder.append("</p>");

//        this.stashConnectorService
        velocityContext.put("a_value", 15);
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