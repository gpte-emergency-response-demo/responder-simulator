package com.redhat.cajun.navy.responder.simulator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Responder {

    private String responderId = null;

    private String missionId = null;

    private String incidentId = null;

    @JsonIgnore
    public Queue<Location> queue = null;

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
        queue = new LinkedList<>();
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

    public Queue<Location> getQueue() {
        return queue;
    }

    public void setQueue(Queue<Location> queue) {
        this.queue = queue;
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



    public Location nextLocation() {
        return queue.poll();
    }

    public void addNextLocation(Location location) {
        if (queue != null) {
            queue.add(location);
        }
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
        Location l = queue.peek();
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
