package com.redhat.cajun.navy.responder.simulator;

public class Location {

    private double lat;
    private double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    private boolean isWayPoint = false;
    private boolean isDestination = false;

    public Location() {
    }


    public boolean isWayPoint() {
        return isWayPoint;
    }

    public void setWayPoint(boolean wayPoint) {
        isWayPoint = wayPoint;
    }

    public boolean isDestination() {
        return isDestination;
    }

    public void setDestination(boolean destination) {
        isDestination = destination;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double input) {
        this.lat = input;
    }

    public double getLong() {
        return lon;
    }

    public void setLong(double input) {
        this.lon = input;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}