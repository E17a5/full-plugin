package be.aui.table;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.setup.BootstrapManager;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.renderer.v2.SubRenderer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


public abstract class VelocityMacro extends ContentFormattingMacro{

    @ComponentImport
    protected SubRenderer subRenderer;

    @ComponentImport
    protected BootstrapManager bootstrapManager;




    public VelocityMacro(SubRenderer subrenderer, BootstrapManager bootstrapManager) {
        super();
        this.subRenderer = subrenderer;
        this.bootstrapManager = bootstrapManager;
    }

    public String getDefaultImagePath() {
        return null;
    }

    public abstract String getVelocityFilename();

    public void preExecute(Map parameters, String body, ConversionContext conversionContext, Map velocityContext) {}

    public String postExecute(Map parameters, String body, ConversionContext conversionContext, Map velocityContext, String result) {
        return result;
    }

    public String getRelativeBasePath() {
        return this.bootstrapManager.getWebAppContextPath();
    }

    public Map newVelocityContext(Map parameters, String body, ConversionContext conversionContext) {
        return newBaseVelocityContext(parameters, body, conversionContext);
    }

    public Map newBaseVelocityContext(Map parameters, String body, ConversionContext conversionContext) {
        Map<String, Object> velocityContext = MacroUtils.defaultVelocityContext();
        if (isMarkMacro(conversionContext)) {
            body = body.replaceAll("(<p>)|(</p>)", "");
            body = body.replaceAll("\\u00A0", "");
        }

        velocityContext.put("macro", this);
        velocityContext.put("body", body);
        velocityContext.put("subRenderer", this.subRenderer);
        velocityContext.put("renderContext", conversionContext);
        String imgPath = getDefaultImagePath();

        if (imgPath != null)
            velocityContext.put("imgPath", getRelativeBasePath() + imgPath);
        velocityContext.put("defaultParam", (VelocityMacro)parameters.get("0"));
        return velocityContext;
    }

    protected String renderMacro(Map<String, String> parameters, String body, ConversionContext conversionContext) throws MacroExecutionException {
        body = Util.stripFirstParagraphTags(body);
        String classname = Util.getHtmlSafeParameter(parameters, "class");
        if (classname == null)
            classname = "";
        Map<String, String> velocityContext = newVelocityContext(parameters, body, conversionContext);
        velocityContext.put("classname", classname);
        preExecute(parameters, body, conversionContext, velocityContext);
        String result = VelocityUtils.getRenderedTemplate(getVelocityFilename(), velocityContext);
        result = postExecute(parameters, body, conversionContext, velocityContext, result);
        if (result.matches(".*\\n$"))
            result = result.substring(0, result.length() - 1);
        return result;
    }

    private boolean isMarkMacro(ConversionContext conversionContext) {
        return (StringUtils.equals(conversionContext.getPropertyAsString("macroName"), "reg-tm") ||
                StringUtils.equals(conversionContext.getPropertyAsString("macroName"), "sm") ||
                StringUtils.equals(conversionContext.getPropertyAsString("macroName"), "tm"));
    }
}
