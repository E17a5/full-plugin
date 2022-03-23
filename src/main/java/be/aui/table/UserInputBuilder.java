package be.aui.table;

import java.util.ArrayList;
import java.util.List;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

public class UserInputBuilder {

    // List of inputHandler class
    private List<UserInputHandler> handlers = new ArrayList<>();

    private Encoder encoder;

    // return new UserInputBuilder instance
    public static UserInputBuilder builder() {
        return new UserInputBuilder();
    }

    public UserInputBuilder setOwaspEncoder(Encoder encoder) {
        this.encoder = encoder;

        return this;
    }

    public UserInputBuilder withCanonicalizationOfInputString() {
        if (this.encoder == null)
            throw new IllegalStateException("Canonicalization requires an encoder!");
        this.handlers.add(new StringCanonicalizer(this.encoder));
        return this;
    }

    public UserInputBuilder withHtmlEncodingOfInputString() {
        if (this.encoder == null)
            throw new IllegalStateException("An encoder must be specified");
        this.handlers.add(new HtmlEncoder(this.encoder));
        return this;
    }

    public UserInputBuilder withSecurityConfiguration(String securityConfigurationName) {
        ESAPI.initialize(securityConfigurationName);
        return this;
    }


    public UserInputHandler build() {

        // check if handlers is Empty
        //Handlers --> list of inputHandler class

        if (this.handlers.isEmpty())
            throw new IllegalStateException("No user input handlers have been set!");

        // create a InputHandler variable to .?.
        UserInputHandler previous = null;


        for (UserInputHandler handler : this.handlers) {

            if (previous != null)
                previous.setNextInChain(handler);
            previous = handler;
        }
        return this.handlers.get(0);
    }
}

