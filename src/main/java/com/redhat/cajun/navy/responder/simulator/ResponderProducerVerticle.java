package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;

public class ResponderProducerVerticle extends ResponderMessageVerticle{

    Logger logger = LoggerFactory.getLogger(ResponderProducerVerticle.class);

    @Override
    public void init(Future<Void> startFuture) throws Exception {
        producer = KafkaProducer.create(vertx,config);
        vertx.eventBus().consumer(config().getString(RES_OUTQUEUE, RES_OUTQUEUE), this::onMessage);
    }


    public void onMessage(Message<JsonObject> message) {

        if (!message.headers().contains("action")) {
            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
            return;
        }


        String action = message.headers().get("action");

        switch (action) {
            case "PUBLISH_UPDATE":

                KafkaProducerRecord<String, String> record =
                        KafkaProducerRecord.create(responderMovedTopic, String.valueOf(message.body()));

                producer.write(record, done -> {

                    if (done.succeeded()) {

                        RecordMetadata recordMetadata = done.result();
                        logger.debug("Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
                                ", partition=" + recordMetadata.getPartition() +
                                ", offset=" + recordMetadata.getOffset());

                        message.reply("Message delivered to topic"+responderMovedTopic);
                    }

                });

                break;

            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);

        }
    }


}
