package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.micrometer.PrometheusScrapingHandler;

import static com.redhat.cajun.navy.responder.simulator.EventConfig.RES_INQUEUE;

public class RestVerticle extends AbstractVerticle {


    private Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    private static final String EP = "responders";

//    @Override
//    public void start(Future<Void> future) {
//        int port = config().getInteger("http.port", 8887);
//
//        // Create a router object.
//        Router router = Router.router(vertx);
//        router.get("/").handler(rc -> {
//            rc.response().putHeader("content-type", "text/html")
//                    .end("Responder Sim");
//        });
//
//        router.route().handler(BodyHandler.create());
//        router.post(EP + "/").handler(this::putHuman);
//        router.route("/metrics").handler(PrometheusScrapingHandler.create());
//
//        vertx.createHttpServer()
//                .requestHandler(router)
//                .listen(port, ar -> {
//                    if (ar.succeeded()) {
//                        logger.info("Http Server Listening on: "+port);
//                        future.complete();
//                    } else {
//                        logger.error("Http Server not started: "+ar.cause());
//                        future.fail(ar.cause());
//
//                    }
//                });
//    }

    @Override
    public void start(Future<Void> fut) {
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


}
