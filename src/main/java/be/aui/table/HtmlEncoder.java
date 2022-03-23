package be.aui.table;

import org.owasp.esapi.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlEncoder extends UserInputHandler {
    private static Logger LOG = LoggerFactory.getLogger(HtmlEncoder.class);

    public HtmlEncoder(Encoder encoder) {
        super(encoder);
    }

    public void handleImpl(UserInputHandlerContext input) {
        LOG.debug("Encoding macro input");
        String inputValue = input.getInputValue();
        input.setInputValue(getEncoder().encodeForHTML(inputValue));
    }
}