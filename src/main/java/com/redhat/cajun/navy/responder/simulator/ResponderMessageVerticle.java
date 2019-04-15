package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.Future;

public abstract class ResponderMessageVerticle extends ResponderVerticle {

    protected abstract void init(Future<Void> startFuture) throws Exception;


    @Override
    public void start(Future<Void> startFuture) throws Exception {

        //config.put("bootstrap.servers", "localhost:9092");
        config.put("bootstrap.servers", config().getString("kafka.connect", "localhost:9092"));
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", config().getString("kafka.group.id"));
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", config().getBoolean("kafka.autocommit", true).toString());
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        responderMovedTopic = config().getString("kafka.pub");
        responderUpdatedTopic = config().getString("kafka.sub");
        init(startFuture);

    }
}
