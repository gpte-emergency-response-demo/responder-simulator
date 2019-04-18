package com.redhat.cajun.navy.responder.simulator;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.*;


public class SimulationControl extends ResponderVerticle {

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
        responders = new HashSet<Responder>(150);

        // subscribe to Eventbus for incoming messages
        vertx.eventBus().consumer(config().getString(RES_INQUEUE, RES_INQUEUE), this::onMessage);

        defaultTime = config().getInteger("interval", 10000);

        long timerID = vertx.setPeriodic(defaultTime, id -> {

            List<Responder> toRemove = new ArrayList<>();
            vertx.<String>executeBlocking(future -> {

                responders.forEach(responder -> {
                    if(responder.isEmpty()) {
                      toRemove.add(responder);
                    }
                    else {
                        if(responder.isContinue()){
                            createMessage((responder));
                        }
                    }

                });
                        String result = "";
                        responders.removeAll(toRemove);
                        future.complete(result);

                    },res -> {

                if (res.succeeded()) {

                    res.result();

                } else {
                    res.cause().printStackTrace();
                }
            });
        });

    }

    protected void createMessage(Responder r){
        System.out.println(r);
        if(r.size() > 1) {
            if (r.getCurrentLocation().isWayPoint()) {
                r.setStatus(Responder.Status.PICKEDUP);
            }
            else {
                r.setStatus(Responder.Status.MOVING);
            }
            sendMessage(r);
            r.nextLocation();

        }

        if (r.size()==1) {
            r.setContinue(false);
            r.setStatus(Responder.Status.DROPPED);
            sendMessage(r);
            r.nextLocation();

        }

    }

    private void sendMessage(Responder r){
        DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.PUBLISH_UPDATE.getActionType());
        vertx.eventBus().send(RES_OUTQUEUE, r.toString(), options,
                reply -> {
                    if (reply.succeeded()) {
                        logger.debug("EventBus: Responder update message accepted");
                    } else {
                        logger.error("EventBus: Responder update message not accepted");
                    }
                });

    }

    protected Responder getResponderFromStringJson(String json, MessageType messageType) throws UnWantedResponderEvent, Exception{
        Responder r = new Responder();
        JsonNode type = getNode("messageType",json);
        JsonNode body = getNode("body", json);

        if (type.asText().equalsIgnoreCase(messageType.getMessageType())) {
            r.setResponderId(body.get("responderId").asText());
            r.setMissionId((body.get("id").asText()));
            r.setIncidentId(body.get("incidentId").asText());

            JsonNode route = getNode("route", body.toString());
            JsonNode steps = getNode("steps", route.toString());

            steps.elements().forEachRemaining(jsonNode -> {
                Location l = Json.decodeValue(String.valueOf(jsonNode.get("loc")), Location.class);
                l.setDestination(jsonNode.get("destination").asBoolean());
                l.setWayPoint(jsonNode.get("wayPoint").asBoolean());
                r.addNextLocation(l);
            });
            return r;
        }
        else throw new UnWantedResponderEvent("Unwanted MessageType: "+messageType.getMessageType());
    }


    protected JsonNode getNode(String tag, String stream) throws Exception{
        return Json.mapper.readTree(stream).get(tag);
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
                    Responder r = getResponderFromStringJson(String.valueOf(message.body()), MessageType.MissionStartedEvent);
                        if (!responders.contains(r))
                            responders.add(r);
                }
                catch(UnWantedResponderEvent re){
                    message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Responder not parsable " + re.getMessage());
                }
                catch(Exception e) {
                    message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Responder not parsable " + e.getMessage());
                }
                break;

            case "RESPONDER_MSG":
                    Responder responder = Json.decodeValue(String.valueOf(message.body()), Responder.class);
                    createMessage((responder));
                    break;
            case "RESPONDERS_CLEAR":
                    responders =Collections.synchronizedSet(new HashSet<Responder>(150));
                    break;
            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);
        }

    }
}

