package be.aui.table;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.inject.Named;

@Component
public abstract class ContentFormattingMacro implements Macro{

    protected static final String PREVIEW = "preview";

    @Autowired
    public ContentFormattingMacro() {
    }

    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        return renderMacro(map, s, conversionContext);
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.RICH_TEXT;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }

    protected boolean isPreviewMode(ConversionContext conversionContext) {
        return conversionContext.getOutputType().equals("preview");
    }

    protected abstract String renderMacro(Map<String, String> paramMap, String paramString, ConversionContext paramConversionContext) throws MacroExecutionException;
}
