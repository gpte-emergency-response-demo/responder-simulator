package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import rx.Observable;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class HttpApplication extends AbstractVerticle {



    @Override
    public void start(Future<Void> future) {
        // Create a router object.
        Router router = Router.router(vertx);

        router.get("/api/greeting").handler(this::greeting);
        router.get("/*").handler(StaticHandler.create());

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(
                        // Retrieve the port from the configuration, default to 8080.
                        config().getInteger("http.port", 8080), ar -> {
                            if (ar.succeeded()) {
                                System.out.println("Server started on port " + ar.result().actualPort());
                            }
                            future.handle(ar.mapEmpty());
                        });
    }

    private void greeting(RoutingContext rc) {

        String name = rc.request().getParam("name");

        JsonObject response = new JsonObject()
                .put("content", String.format("-----", name));

        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());
    }
}
