package org.acme;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CombinedUserMessagesAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            UserMessage body = newExchange.getIn().getBody(UserMessage.class);
            CombinedUserMessage newEventBody = CombinedUserMessage.builder()
                    .emitter(body.getEmitter()).text(List.of(body.getText())).build();
            newExchange.getIn().setBody(newEventBody);
            return newExchange;
        }
        UserMessage newUserMessage = newExchange.getIn().getBody(UserMessage.class);
        CombinedUserMessage oldCombinedUserMessage = oldExchange.getIn()
                .getBody(CombinedUserMessage.class);

        List<String> newTest = new ArrayList<>(oldCombinedUserMessage.getText());
        newTest.add(newUserMessage.getText());

        CombinedUserMessage newCombinedUserMessage = CombinedUserMessage.builder()
                .emitter(newUserMessage.getEmitter()).text(newTest).build();

        oldExchange.getIn().setBody(newCombinedUserMessage);
        return oldExchange;
    }
}