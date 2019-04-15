package com.redhat.cajun.navy.responder.simulator;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.Json;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponderTest {

    @Test
    public void testJsonParsing() throws Exception {

        Responder r = new Responder();
        JsonNode body = getNode("body", StaticData.testJson);
        r.setResponderId(body.get("responderId").asText());
        r.setMissionId((body.get("id").asText()));

        JsonNode route = getNode("route", body.toString());
        JsonNode steps = getNode("steps", route.toString());

        steps.elements().forEachRemaining(jsonNode -> {
            r.addNextLocation(Json.decodeValue(String.valueOf(jsonNode.get("loc")),Location.class));
        });

        Location l = new Location();
        l.setWayPoint(false);
        l.setDestination(false);
        l.setLat(29.788405);
        l.setLong(-95.633189);
        assertEquals(29.788405, l.getLat());
        assertEquals(-95.633189, l.getLong());
//        if(responder.isEmpty()) {
//            //System.out.println("Removing responder " + responder);
//            responders.removeResponder(responder);
//            responder.setContinue(false);
//            responder.setStatus(Responder.Status.DROPPED);
//        }
//        else {
//            responder.nextLocation();
//            if (responder.isHuman() && responder.getCurrentLocation().isWayPoint()) {
//                responder.setContinue(false);
//                responder.setStatus(Responder.Status.PICKEDUP);
//            }
//            else{
//                responder.setStatus(Responder.Status.MOVING);
//            }
//        }

    }


    @Test
    public void testEvents() throws Exception {

        Responder r = new Responder();
        JsonNode body = getNode("body", StaticData.testJson);
        r.setResponderId(body.get("responderId").asText());
        r.setMissionId((body.get("id").asText()));
        r.setIncidentId(body.get("incidentId").asText());

        JsonNode route = getNode("route", body.toString());
        JsonNode steps = getNode("steps", route.toString());

        steps.elements().forEachRemaining(jsonNode -> {
            Location l = Json.decodeValue(String.valueOf(jsonNode.get("loc")),Location.class);
            l.setDestination(jsonNode.get("destination").asBoolean());
            l.setWayPoint(jsonNode.get("wayPoint").asBoolean());
            if(l!=null)
                r.addNextLocation(l);
            System.out.println(l);
        });

        System.out.println(r.getResponderLocation().size());

        for (int i = r.getResponderLocation().size()-1; i >= 0; i--) {
            //System.out.println(i);
            if(!r.isEmpty()) {
                if (r.getCurrentLocation().isWayPoint())
                    r.setStatus(Responder.Status.PICKEDUP);
                else
                    r.setStatus(Responder.Status.MOVING);
                r.nextLocation();
            }
            if (r.isEmpty()) {
                r.setContinue(false);
                r.setStatus(Responder.Status.DROPPED);
            }
            System.out.println(r.getResponderLocation().size()+" "+i +" "+ r);
            Responder r1 = Json.decodeValue(r.toString(), Responder.class);
        }


    }


    private JsonNode getNode(String tag, String stream) throws Exception{

        return Json.mapper.readTree(stream).get(tag);

    }

}