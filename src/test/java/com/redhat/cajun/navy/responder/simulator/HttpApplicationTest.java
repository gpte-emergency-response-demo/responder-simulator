package com.redhat.cajun.navy.responder.simulator;

import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)

public class HttpApplicationTest {

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new HttpApplication(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    @DisplayName("Should start a Web Server on port 8080")
    @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
    void start_http_server(Vertx vertx, VertxTestContext testContext) throws Throwable {
        vertx.createHttpClient().getNow(8080, "localhost", "/", response -> testContext.verify(() -> {
            response.handler(body -> {
                testContext.completeNow();
            });
        }));
    }


}