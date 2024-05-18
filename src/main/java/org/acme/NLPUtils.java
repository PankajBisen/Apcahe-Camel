package org.acme;

public class NLPUtils {

    public static UserMessages createUserMessages(final CombinedUserMessage event) {
        return UserMessages.builder()
            .emitter(event.getEmitter())
            .text(String.join(". ", event.getText()))
            .build();
    }
}
