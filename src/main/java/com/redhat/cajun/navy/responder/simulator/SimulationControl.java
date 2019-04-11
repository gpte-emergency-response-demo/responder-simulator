package com.redhat.cajun.navy.responder.simulator;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;


public class SimulationControl extends ResponderVerticle {

    private ActiveResponder responders = new ActiveResponder();
    private int defaultTime = 2000;


    @Override
    public void start(Future<Void> startFuture) throws Exception {
        // subscribe to Eventbus for incoming messages
        vertx.eventBus().consumer(config().getString(RES_INQUEUE, RES_INQUEUE), this::onMessage);

        long timerID = vertx.setPeriodic(defaultTime, id -> {
                responders.getActiveResponders().forEach(responder -> {
                    if(responder.isEmpty()) {
                        responders.removeResponder(responder);
                    }
                    else {
                        if(responder.isContinue())
                            createMessage((responder));
                    }

                });
        });

    }

    protected void createMessage(Responder responder){
        //move the responders location

        if(responder.isEmpty()) {
            System.out.println("Removing responder " + responder);
            responders.removeResponder(responder);
            responder.setContinue(false);
            responder.setStatus(Responder.Status.DROPPED);
        }
        else {
            responder.nextLocation();
            if (responder.isHuman() && responder.getCurrentLocation().isWayPoint()) {
                responder.setContinue(false);
                responder.setStatus(Responder.Status.PICKEDUP);
            }
            else{
                responder.setStatus(Responder.Status.MOVING);
            }
        }

            DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.PUBLISH_UPDATE.getActionType());
            vertx.eventBus().send(RES_OUTQUEUE, responder.toString(), options, reply -> {
                if (reply.succeeded()) {
                    System.out.println("Responder update message accepted");
                } else {
                    System.out.println("Responder update message not accepted");
                }
            });
    }

    protected Responder getResponderFromStringJson(String json) throws Exception{
        Responder r = new Responder();
        JsonNode body = getNode("body", json);
        r.setResponderId(body.get("responderId").asText());
        r.setMissionId((body.get("id").asText()));
        r.setIncidentId(body.get("incidentId").asText());

        JsonNode route = getNode("route", body.toString());
        JsonNode steps = getNode("steps", route.toString());

        steps.elements().forEachRemaining(jsonNode -> {
            Location l = Json.decodeValue(String.valueOf(jsonNode.get("loc")),Location.class);
            l.setDestination(jsonNode.get("destination").asBoolean());
            l.setWayPoint(jsonNode.get("wayPoint").asBoolean());
            r.addNextLocation(l);
        });
        return r;
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
                    Responder r = getResponderFromStringJson(String.valueOf(message.body()));
                    if(!responders.getActiveResponders().contains(r))
                        responders.addResponder(r);
                }catch(Exception e) {
                    e.printStackTrace();
                    message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Responder not parsable " + e.getMessage());
                }
                break;
            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);
        }

    }
}

