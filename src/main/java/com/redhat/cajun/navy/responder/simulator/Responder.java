package com.redhat.cajun.navy.responder.simulator;

import java.util.Objects;
import java.util.Stack;

public class Responder {

    private String responderId = null;

    private String missionId = null;

    private Stack<Location> responderLocation = null;

    private boolean isHuman = false;

    public Responder() {
        responderLocation = new Stack<Location>();
    }

    public int getLocationCount(){
        return responderLocation.size();
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

    public Location getCurrentLocation(){
        return responderLocation.peek();
    }

    public boolean isEmpty(){
        return responderLocation.isEmpty();
    }


    public Location nextLocation(){
        responderLocation.pop();
        return responderLocation.peek();
    }

    public void addNextLocation(Location location){
        if(responderLocation!=null){
            responderLocation.push(location);
        }
    }

    public void setLocation(Location location){
        addNextLocation(location);
    }

    public Location getLocation(){
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
        return Objects.hash(responderId);
    }

    public String messageString() {
        return "Responder{" +
                "responderId='" + responderId + '\'' +
                ", missionId='" + missionId + '\'' +
                ", responderLocation=" + getCurrentLocation() +
                ", isHuman=" + isHuman +
                '}';
    }


    @Override
    public String toString() {
        return "Responder{" +
                "responderId='" + responderId + '\'' +
                ", missionId='" + missionId + '\'' +
                ", responderLocation=" + responderLocation +
                ", isHuman=" + isHuman +
                '}';
    }
}
