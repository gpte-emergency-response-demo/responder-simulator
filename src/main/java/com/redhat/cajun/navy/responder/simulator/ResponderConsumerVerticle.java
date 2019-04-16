package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.consumer.KafkaConsumer;

public class ResponderConsumerVerticle extends ResponderMessageVerticle {

    Logger logger = LoggerFactory.getLogger(ResponderConsumerVerticle.class);

    @Override
    public void init(Future<Void> startFuture) throws Exception{

        consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.CREATE_ENTRY.getActionType());

            vertx.eventBus().send(RES_INQUEUE, record.value(), options, reply -> {
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
                logger.debug(("subscribed to MissionEvents"));
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