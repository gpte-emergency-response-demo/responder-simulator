package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.Map;

import static com.redhat.cajun.navy.responder.simulator.EventConfig.RES_INQUEUE;

public class ResponderConsumerVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(ResponderConsumerVerticle.class);
    private Map<String, String> config = new HashMap<>();
    KafkaConsumer<String, String> consumer = null;
    public String responderUpdatedTopic = null;


    @Override
    public void start(Future<Void> startFuture) throws Exception {

        config.put("bootstrap.servers", config().getString("kafka.connect", "localhost:9092"));
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", config().getString("kafka.group.id"));
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", config().getBoolean("kafka.autocommit", true).toString());

        responderUpdatedTopic = config().getString("kafka.sub");

        consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.CREATE_ENTRY.getActionType());

            vertx.eventBus().send(RES_INQUEUE, record.value(), options, reply -> {
                logger.info(record.value());
                if (reply.succeeded()) {
                    logger.debug("Incoming message accepted");
                } else {
                    logger.debug("Incoming Message not accepted "+record.topic());
                    logger.debug(record.value());
                }
            });
        });

        consumer.subscribe(responderUpdatedTopic, ar -> {
            if (ar.succeeded()) {
                logger.info(("subscribed to MissionEvents"));
            } else {
                logger.fatal("Could not subscribe " + ar.cause().getMessage());
            }
        });
    }


    @Override
    public void stop() throws Exception {
        consumer.unsubscribe(ar -> {

            if (ar.succeeded()) {
                logger.debug("Consumer unsubscribed");
            }
        });
    }
}