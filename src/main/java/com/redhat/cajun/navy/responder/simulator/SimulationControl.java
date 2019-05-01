package com.redhat.cajun.navy.responder.simulator;

import com.redhat.cajun.navy.responder.simulator.data.Mission;
import com.redhat.cajun.navy.responder.simulator.data.MissionCommand;
import com.redhat.cajun.navy.responder.simulator.data.Responder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.*;

import static com.redhat.cajun.navy.responder.simulator.EventConfig.RES_INQUEUE;
import static com.redhat.cajun.navy.responder.simulator.EventConfig.RES_OUTQUEUE;


public class SimulationControl extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(SimulationControl.class);

    Set<Responder> responders = null;

    private int defaultTime = 5000;


    public enum MessageType {
        MissionStartedEvent("MissionStartedEvent"),
        MissionPickedUpEvent("MissionPickedUpEvent"),
        MissionCompletedEvent("MissionCompletedEvent");

        private String messageType;

        MessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMessageType() {
            return messageType;
        }

    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        responders = new HashSet<>(150);

        // subscribe to Eventbus for incoming messages
        vertx.eventBus().consumer(config().getString(RES_INQUEUE, RES_INQUEUE), this::onMessage);

        defaultTime = config().getInteger("interval", 10000);

        long timerID = vertx.setPeriodic(defaultTime, id -> {

            vertx.<String>executeBlocking(fut->{


                List<Responder> toRemove = new ArrayList<>();
                responders.forEach(responder -> {
                    if(responder.isEmpty()) {
                        toRemove.add(responder);
                    }
                    else {
                        if(responder.isContinue()){
                            createMessage((responder));
                            responder.nextLocation();
                        }
                    }

                });
                responders.removeAll(toRemove);

            }, res -> {
                if (res.succeeded()) {
                    logger.debug("executed");

                } else {
                    logger.fatal("error while excute blocking");
                    startFuture.fail(res.cause());
                }
            });
        });

    }

    protected void createMessage(Responder r){
            if (r.peek().isWayPoint())
                r.setStatus(Responder.Status.PICKEDUP);

            else if (r.peek().isDestination())
                r.setStatus(Responder.Status.DROPPED);

            else
                r.setStatus(Responder.Status.MOVING);

            sendMessage(r);

    }

    private void sendMessage(Responder r){

        com.redhat.cajun.navy.responder.simulator.Responder responder =
                new com.redhat.cajun.navy.responder.simulator.Responder(
                        r.getResponderId(),
                        r.getMissionId(),
                        r.getIncidentId(),
                        r.getLocation().getLoc(),
                        r.isHuman(),
                        r.isContinue(), r.getStatus().getActionType());

        DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.PUBLISH_UPDATE.getActionType()).addHeader("key",r.getIncidentId()+r.getResponderId());
        vertx.eventBus().send(RES_OUTQUEUE, responder.toString(), options,
                reply -> {
                    if (!reply.succeeded()) {
                        logger.error("EventBus: Responder update message not accepted "+r);
                    }
                });

    }

    public void onMessage(Message<JsonObject> message) {

        if (!message.headers().contains("action")) {
            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
            return;
        }
        String action = message.headers().get("action");
        switch (action) {
            case "CREATE_ENTRY":
                try {
                    Responder r = getResponder(String.valueOf(message.body()), MessageType.MissionStartedEvent);
                        if (!responders.contains(r))
                            responders.add(r);
                        message.reply(r.toString());
               }
                catch(UnWantedResponderEvent re){
                    message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Responder not parsable " + re.getMessage());
                }
                break;

            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);
        }

    }


    protected Responder getResponder(String json, MessageType messageType) throws UnWantedResponderEvent{
        MissionCommand mc = Json.decodeValue(json, MissionCommand.class);

        Mission m = mc.getBody();

        if(
                m.getResponderStartLat() == 0
                        || m.getResponderStartLong() == 0
                        || m.getIncidentLat() == 0
                        || m.getIncidentLong() == 0
                        || m.getDestinationLat() == 0
                        || m.getDestinationLong() == 0
        )
            throw new UnWantedResponderEvent("Unwanted MessageType: "+messageType.getMessageType());

        else if(MessageType.valueOf(mc.getMessageType()).equals(messageType)){
            return mc.getBody().getResponder();
        }

        else throw new UnWantedResponderEvent("Unwanted MessageType: "+messageType.getMessageType());
    }


}

