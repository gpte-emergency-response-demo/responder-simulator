package com.redhat.cajun.navy.responder.simulator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class ActiveResponder {

    private Set<Responder> activeResponders = null;

    public ActiveResponder(){
        activeResponders = Collections.synchronizedSet(new HashSet<Responder>(150));
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

    public Set<Responder> getActiveResponders(){
        return activeResponders;
    }

}
