package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.kafka.client.consumer.KafkaConsumer;

public class ResponderConsumerVerticle extends ResponderMessageVerticle {


    @Override
    public void init(Future<Void> startFuture) throws Exception{

        consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.CREATE_ENTRY.getActionType());

            vertx.eventBus().send(RES_INQUEUE, record.value(), options, reply -> {
                if (reply.succeeded()) {
                    //System.out.println("Incoming Message accepted");


                } else {
                    System.err.println("Incoming Message not accepted "+record.topic());
                    System.err.println(record.value());
                }
            });
        });

        consumer.subscribe(responderUpdatedTopic, ar -> {
            if (ar.succeeded()) {
                System.out.println("subscribed to MissionEvents");
            } else {
                System.out.println("Could not subscribe " + ar.cause().getMessage());
            }
        });
    }


    @Override
    public void stop() throws Exception {
        consumer.unsubscribe(ar -> {

            if (ar.succeeded()) {
                System.out.println("Consumer unsubscribed");
            }
        });
    }
}