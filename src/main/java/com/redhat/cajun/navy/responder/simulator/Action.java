package com.redhat.cajun.navy.responder.simulator;

public enum Action {
    CREATE_ENTRY ("CREATE_ENTRY"),
    PUBLISH_UPDATE ("PUBLISH_UPDATE"),
    RESPONDER_MSG ("RESPONDER_MSG"),
    RESPONDERS_CLEAR ("RESPONDERS_CLEAR");

    private String actionType;

    Action(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }

}

