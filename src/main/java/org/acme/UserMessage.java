package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import java.util.List;

@Data
@RegisterForReflection
public class UserMessage {

    private int id;
    private String text;
    private String emitter;
    private String type;
    private List<String> devices;

}
