package be.aui.table;

import org.owasp.esapi.Encoder;

public abstract class UserInputHandler {
    private Encoder encoder;

    private UserInputHandler nextInChain;

    public UserInputHandler(Encoder encoder) {
        this.encoder = encoder;
    }

    public final void setNextInChain(UserInputHandler nextInChain) {
        this.nextInChain = nextInChain;
    }

    public Encoder getEncoder() {
        return this.encoder;
    }

    public void handle(UserInputHandlerContext input) {
        handleImpl(input);
        if (this.nextInChain != null)
            this.nextInChain.handleImpl(input);
    }

    public abstract void handleImpl(UserInputHandlerContext paramUserInputHandlerContext);
}
