package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;

import java.util.HashMap;
import java.util.Map;

import static com.redhat.cajun.navy.responder.simulator.EventConfig.RES_OUTQUEUE;

public class ResponderProducerVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(ResponderProducerVerticle.class);
    private Map<String, String> config = new HashMap<>();
    KafkaProducer<String,String> producer = null;
    public String responderMovedTopic = null;


    @Override
    public void start(Future<Void> startFuture) throws Exception {

        config.put("bootstrap.servers", config().getString("kafka.connect", "localhost:9092"));
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        responderMovedTopic = config().getString("kafka.pub");

        producer = KafkaProducer.create(vertx,config);
        vertx.eventBus().consumer(config().getString(RES_OUTQUEUE, RES_OUTQUEUE), this::onMessage);
    }


    public void onMessage(Message<JsonObject> message) {

        if (!message.headers().contains("action")) {
            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
            return;
        }


        String action = message.headers().get("action");
        String key = message.headers().get("key");

        switch (action) {
            case "PUBLISH_UPDATE":

                KafkaProducerRecord<String, String> record =
                        KafkaProducerRecord.create(responderMovedTopic, key, String.valueOf(message.body()));

                producer.write(record, done -> {

                    if (done.succeeded()) {

                        RecordMetadata recordMetadata = done.result();
                        logger.info("Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
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
