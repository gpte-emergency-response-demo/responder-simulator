package com.redhat.cajun.navy.responder.simulator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.Json;


public class Responder {

    private String responderId = null;

    private String missionId = null;

    private String incidentId = null;

    private boolean isHuman = false;

    private boolean isContinue = true;

    private String status;
    @JsonSerialize(using = DoubleContextualSerializer.class)
    @Precision(precision = 4)
    private double lat;

    @JsonSerialize(using = DoubleContextualSerializer.class)
    @Precision(precision = 4)
    private double lon;


    public Responder(String responderId, String missionId, String incidentId, double lat, double lon, boolean isHuman, boolean isContinue, String status) {
        this.responderId = responderId;
        this.missionId = missionId;
        this.incidentId = incidentId;
        this.lat = lat;
        this.lon = lon;

        this.isHuman = isHuman;
        this.isContinue = isContinue;
        this.status = status;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double input) {
        this.lat = input;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double input) {
        this.lon = input;
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

