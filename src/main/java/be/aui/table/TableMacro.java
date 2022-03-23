package be.aui.table;


import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.setup.BootstrapManager;
import com.atlassian.json.jsonorg.JSONArray;
import com.atlassian.json.jsonorg.JSONObject;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.renderer.v2.SubRenderer;
import org.owasp.esapi.Encoder;

import java.util.*;

import org.owasp.esapi.reference.DefaultEncoder;
import org.owasp.esapi.reference.DefaultSecurityConfiguration;

import java.util.Map;

public class TableMacro extends VelocityMacro {
//    private UserInputHandler inputHandler = UserInputBuilder.builder()
//            .withSecurityConfiguration(DefaultSecurityConfiguration.class.getName())
//            .setOwaspEncoder((Encoder)new DefaultEncoder(new ArrayList(Arrays.asList((Object[])new String[] { "HTMLEntityCodec" })))).withCanonicalizationOfInputString()
//            .withHtmlEncodingOfInputString()
//            .build();

    public TableMacro(SubRenderer subrenderer, BootstrapManager bootstrapManager) {
        super(subrenderer, bootstrapManager);
    }

    public Map newVelocityContext(Map parameters, String body, ConversionContext conversionContext) {
        Map<String, String> velocityContext = super.newVelocityContext(parameters, body, conversionContext);
        String restURL = (String)parameters.get("restURL");
        int counter = Integer.parseInt(parameters.get("counter").toString());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < counter; i++) {
//            String header = (parameters.get(i + "_label") == null) ? "" : parameters.get(i + "_label").toString();
//            UserInputHandlerContext context = new UserInputHandlerContext(header);
////            this.inputHandler.handle(context);
////            String safeHeader = context.getInputValue();
////            String id = parameters.get(i + "_value").toString();
            jsonArray.put((new JSONObject()).put("header", "safeHeader").put("id", "id"));
        }
        velocityContext.put("restURL", restURL);
        velocityContext.put("values", jsonArray.toString());
        return velocityContext;
    }

    public Macro.BodyType getBodyType() {
        return Macro.BodyType.NONE;
    }

    public String getVelocityFilename() {
        return "vm/rest-table-macro.vm";
    }
}
