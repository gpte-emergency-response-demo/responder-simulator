package com.redhat.cajun.navy.responder.simulator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.vertx.core.json.Json;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Responder {

    private String responderId = null;

    private String missionId = null;

    private String incidentId = null;

    @JsonIgnore
    private Queue<Location> responderLocation = null;

    private boolean isHuman = false;

    private boolean isContinue = true;

    private Status status = Status.RECEIVED;

    protected enum Status {
        RECEIVED("RECEIVED"),
        PREP("PREP"),
        READY("READY"),
        MOVING("MOVING"),
        STUCK("STUCK"),
        PICKEDUP("PICKEDUP"),
        DROPPED("DROPPED");

        private String actionType;

        Status(String actionType) {
            this.actionType = actionType;
        }

        public String getActionType() {
            return actionType;
        }
    }



    public Responder() {
        responderLocation = new LinkedList<>();
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }



    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public Queue<Location> getResponderLocation() {
        return responderLocation;
    }

    public void setResponderLocation(Queue<Location> responderLocation) {
        this.responderLocation = responderLocation;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    @JsonIgnore
    public Location getCurrentLocation() {
        if(!responderLocation.isEmpty())
            return responderLocation.element();
        else return new Location();
    }


    public Location nextLocation() {
        if (!responderLocation.isEmpty()) {
            return responderLocation.poll();
        } else throw new IllegalArgumentException("Stack is Empty");
    }

    public void addNextLocation(Location location) {
        if (responderLocation != null) {
            responderLocation.add(location);
        }
    }

    @JsonIgnore
    public boolean isEmpty(){
        return responderLocation.isEmpty();
    }


    public void setLocation(Location location) {
        addNextLocation(location);
    }

    public Location getLocation() {
        return nextLocation();
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Responder responder = (Responder) o;
        return Objects.equals(responderId, responder.responderId);
    }

    @Override
    public int hashCode() {
        return responderId.hashCode();
    }

    public String messageString(){
        Location l = getCurrentLocation();
        return "{\"responderId\":\""+responderId+"\"," +
                "\"missionId\":\""+missionId+"\"," +
                "\"incidentId\":\""+incidentId+"\"," +
                "\"status\":\""+getStatus()+"\"," +
                "\"continue\":"+isContinue+"," +
                "\"human\":"+isHuman+"," +
                "\"location\":{\"lat\":"+l.getLat()+",\"wayPoint\":"+l.isWayPoint()+",\"destination\":"+l.isDestination()+",\"long\":"+l.getLong()+"}}";


    }


    @Override
    public String toString() {
        return messageString();
    }
}
