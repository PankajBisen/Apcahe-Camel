package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RegisterForReflection
public class CombinedUserMessage {
    private String emitter;
    private List<String> text;
}
