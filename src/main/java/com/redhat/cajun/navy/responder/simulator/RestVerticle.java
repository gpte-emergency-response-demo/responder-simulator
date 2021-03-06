package com.redhat.cajun.navy.responder.simulator;

import com.redhat.cajun.navy.responder.simulator.data.MissionCommand;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.micrometer.PrometheusScrapingHandler;

import java.util.ArrayList;

import static com.redhat.cajun.navy.responder.simulator.EventConfig.REST_EP;
import static com.redhat.cajun.navy.responder.simulator.EventConfig.RES_INQUEUE;

public class RestVerticle extends AbstractVerticle {


    private Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    private ArrayList<JsonObject> recievedMissionCommands = new ArrayList<>(150);
    private ArrayList<JsonObject> respondersInSim = new ArrayList<>(150);

    @Override
    public void start(Future<Void> fut) {

        // subscribe to Eventbus for incoming messages
        vertx.eventBus().consumer(config().getString(REST_EP, REST_EP), this::onMessage);

        int port = config().getInteger("http.port", 8080);
        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("Responder Sim");
        });

        router.route("/api/responders*").handler(BodyHandler.create());
        router.post("/api/responders").handler(this::putHuman);
        router.route("/metrics").handler(PrometheusScrapingHandler.create());
        router.get("/stats/r").handler(this::getRespondersInSim);
        router.get("/stats/mc").handler(this::getMissionCommandsRecieved);



        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(port,
                        result -> {
                            if (result.succeeded()) {
                                logger.info("Http Server listening on port "+port);
                                fut.complete();
                            } else {
                                logger.error("Http Server didnt start "+result.cause());
                                fut.fail(result.cause());
                            }
                        }
                );
    }


    private void getRespondersInSim(RoutingContext routingContext){
        routingContext.response()
                .setStatusCode(204)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(respondersInSim));

    }

    private void getMissionCommandsRecieved(RoutingContext routingContext){
        routingContext.response()
                .setStatusCode(204)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(recievedMissionCommands));

    }

    private void putHuman(RoutingContext routingContext) {
        DeliveryOptions options = new DeliveryOptions().addHeader("action", Action.RESPONDER_MSG.getActionType());
        vertx.eventBus().send(RES_INQUEUE, routingContext.getBodyAsString(), options, reply -> {
            if (reply.succeeded()) {
                logger.debug("A Person added to Responders");
                routingContext.response()
                        .setStatusCode(204)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end();
            } else {
                logger.error("Adding Person failed "+routingContext.getBodyAsString());
                routingContext.response()
                        .setStatusCode(400)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end();


            }
        });


    }



    public void onMessage(Message<JsonObject> message) {

        if (!message.headers().contains("action")) {
            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
            return;
        }
        String action = message.headers().get("action");
        switch (action) {
            case "CREATE_ENTRY":
                recievedMissionCommands.add(message.body());
                message.reply("received");
                break;
            case "RESPONDER_MSG":
                respondersInSim.add(message.body());
                message.reply("received");
                break;
            default:
                message.reply("ignoring");
        }
    }


}
