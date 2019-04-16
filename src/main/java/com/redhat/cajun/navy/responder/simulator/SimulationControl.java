package com.redhat.cajun.navy.responder.simulator;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import rx.Observable;


public class SimulationControl extends ResponderVerticle {

    private ActiveResponder responders = new ActiveResponder();
    private int defaultTime = 10000;


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

        // subscribe to Eventbus for incoming messages
        vertx.eventBus().consumer(config().getString(RES_INQUEUE, RES_INQUEUE), this::onMessage);

        long timerID = vertx.setPeriodic(defaultTime, id -> {
            Observable.from(responders.getActiveResponders()).flatMap(responder -> {
                    if(responder.getResponderLocation().isEmpty()) {
                        responders.removeResponder(responder);
                    }
                    else {
                        if(responder.isContinue()){
                            createMessage((responder));
                            System.out.println(responder.getResponderLocation().size());
                        }
                    }

                    return Observable.just(responder);

            }).subscribe();
        });

    }

    protected void createMessage(Responder r){
        //move the responders location
        if(!r.isEmpty()) {
            if (r.getCurrentLocation().isWayPoint())
                r.setStatus(Responder.Status.PICKEDUP);
            else
                r.setStatus(Responder.Status.MOVING);
            r.nextLocation();

        if (r.isHuman() && r.getCurrentLocation().isWayPoint())
            r.setContinue(false);
        }
        if (r.isEmpty()) {
            r.setContinue(false);
            r.setStatus(Responder.Status.DROPPED);
            if(responders.hasResponder(r))
                responders.removeResponder(r);
        }

        DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.PUBLISH_UPDATE.getActionType());
        vertx.eventBus().send(RES_OUTQUEUE, r.toString(), options,
                reply -> {
                    if (reply.succeeded()) {
                        System.out.println("Responder update message accepted");
                    } else {
                        System.out.println("Responder update message not accepted");
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
                        if (!responders.getActiveResponders().contains(r))
                            responders.addResponder(r);
                }
                catch(UnWantedResponderEvent re){
                    message.reply(re.getMessage());
                }
                catch(Exception e) {
                    message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Responder not parsable " + e.getMessage());
                }
                break;

            case "RESPONDER_MSG":
                    Responder responder = Json.decodeValue(String.valueOf(message.body()), Responder.class);
                    createMessage((responder));
                    break;

            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);
        }

    }
}

