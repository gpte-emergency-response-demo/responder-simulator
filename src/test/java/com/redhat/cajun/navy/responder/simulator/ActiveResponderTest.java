package com.redhat.cajun.navy.responder.simulator;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import rx.Observable;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;

public class ActiveResponderTest {

    ActiveResponder responders = new ActiveResponder();



    @Test
    public void addResponder() {
        responders.addResponder(getResponder());
        assertEquals(responders.getActiveResponders().size(), 1);
    }

    private Responder getResponder(){

        Responder r = new Responder();
        r.setIncidentId("IncidentId");
        r.setStatus(Responder.Status.READY);
        r.setContinue(true);
        r.setMissionId("MissionId");
        r.setResponderId("ResponderId");
        r.setLocation(new Location(33, -77));
        r.setHuman(false);
        return r;

    }


    @Test
    public void removeResponder() {
        responders.removeResponder(getResponder());
        assertEquals(responders.getActiveResponders().size(), 0);

    }

    @Test
    public void hasResponder() {
        responders.addResponder(getResponder());
        Responder r = new Responder();
        r.setIncidentId("IncidentId");
        r.setStatus(Responder.Status.READY);
        r.setContinue(true);
        r.setMissionId("MissionId");
        r.setResponderId("ResponderId");
        r.setLocation(new Location(33, -77));
        r.setHuman(false);
        assertEquals(r.equals(getResponder()), true);
        assertEquals(responders.hasResponder(r),true);
    }


    @Test
    public void bulkAdd() throws Exception{

        Type RESPONDER_TYPE = new TypeToken<List<Responder>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("src/test/resources/Responders.json"));
        List<Responder> responderList = gson.fromJson(reader, RESPONDER_TYPE);
        Observable.from(responderList).flatMap(r->{
            responders.addResponder(r);
            return Observable.just(r);
        }).doOnError(System.out::println)
                .subscribe();
        assertEquals(responders.getActiveResponders().size(), 150);

    }




}