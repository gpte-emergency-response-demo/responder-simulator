package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;

import java.util.HashMap;
import java.util.Map;

public abstract class ResponderVerticle extends AbstractVerticle {

    KafkaConsumer<String, String> consumer = null;
    KafkaProducer<String,String> producer = null;

    public String responderMovedTopic = null;

    public String responderUpdatedTopic = null;

    public String RES_INQUEUE = "responder.inqueue";

    public String RES_OUTQUEUE = "responder.outqueue";

    Map<String, String> config = new HashMap<>();

    public enum ErrorCodes {
        NO_ACTION_SPECIFIED,
        BAD_ACTION
    }

}
