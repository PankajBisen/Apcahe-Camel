package org.acme;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

public class CamelRoutes extends EndpointRouteBuilder {


    @Counted(name = "messagesProcessed", description = "Number of messages processed")
    @Timed(name = "processTime", description = "Time taken to process chat messages")
    public void configure() {
        errorHandler(deadLetterChannel("kafka:dead_letter?brokers=localhost:9092")
                .maximumRedeliveries(3)
                .retryAttemptedLogLevel(LoggingLevel.ERROR)
                .redeliveryDelay(1000)
                .useOriginalMessage());

        onException(Exception.class).handled(true)
                .log(LoggingLevel.ERROR, "Exception occurred: ${exception.message}");

        from("kafka:input_topic?brokers=localhost:9092&groupId=group1")
                .unmarshal()
                .json(UserMessage.class)
                .log("input_topic: ${body}")
                .filter(simple("${body.type} == 'chat'"))
                .to("seda:incoming_event");

        from("seda:incoming_event?multipleConsumers=true")
                .log("incoming_event: ${body}")
                .split(simple("${body.devices}"))
                .log("body.devices: ${body}")
                .end()
                .marshal().json()
                .to("kafka:output_topic?brokers=localhost:9092");

        from("seda:incoming_event?multipleConsumers=true")
                .aggregate(simple("${body.emitter}"), new CombinedUserMessagesAggregationStrategy())
                .completionInterval(5000)
                .bean(NLPUtils.class, "createUserMessages")
                .log("aggregate: ${body}");

//        from("timer://foo?period=25000").log("timer");

        rest("/api")
                .get("/hello")
                .to("direct:hello");

        from("direct:hello")
                .log("Hello world!")
                .transform()
                .constant("Hello world!");
    }
}