package com.redhat.cajun.navy.responder.simulator;

import com.redhat.cajun.navy.responder.simulator.data.Location;
import io.vertx.core.json.Json;


public class Responder {

    private String responderId = null;

    private String missionId = null;

    private String incidentId = null;

    private Location location = null;

    private boolean isHuman = false;

    private boolean isContinue = true;

    private String status;


    public Responder(String responderId, String missionId, String incidentId, Location location, boolean isHuman, boolean isContinue, String status) {
        this.responderId = responderId;
        this.missionId = missionId;
        this.incidentId = incidentId;
        this.location = location;
        this.isHuman = isHuman;
        this.isContinue = isContinue;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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




    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }


    @Override
    public String toString() {
        return Json.encode(this);
    }
}

