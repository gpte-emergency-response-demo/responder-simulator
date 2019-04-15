package com.redhat.cajun.navy.responder.simulator;

import java.util.HashSet;


public class ActiveResponder {

    private HashSet<Responder> activeResponders = null;

    public ActiveResponder(){
        activeResponders = new HashSet<Responder>(50);
    }

    public boolean addResponder(Responder r){
        if(activeResponders!=null)
            return activeResponders.add(r);
        else throw new IllegalArgumentException("ActiveResponders are null");
    }

    public boolean removeResponder(Responder r){
        if(activeResponders!=null)
            return activeResponders.remove(r);
        else throw new IllegalArgumentException("ActiveResponders are null");
    }

    public boolean hasResponder(Responder r){
        if(activeResponders!=null)
            return activeResponders.contains(r);
        else throw new IllegalArgumentException("ActiveResponders are null");
    }

    public HashSet<Responder> getActiveResponders(){
        return activeResponders;
    }

}
