package be.aui.table;

import org.owasp.esapi.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringCanonicalizer extends UserInputHandler {
    private static Logger LOG = LoggerFactory.getLogger(StringCanonicalizer.class);

    public StringCanonicalizer(Encoder encoder) {
        super(encoder);
    }

    public void handleImpl(UserInputHandlerContext input) {
        LOG.debug("Canonicalizing user input");
        String inputValue = input.getInputValue();
        input.setInputValue(getEncoder().canonicalize(inputValue));
    }
}