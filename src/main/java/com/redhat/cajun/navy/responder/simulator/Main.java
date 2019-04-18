package com.redhat.cajun.navy.responder.simulator;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


public class Main extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(final Future<Void> future) {
        InternalLoggerFactory.setDefaultFactory(Log4JLoggerFactory.INSTANCE);
        ConfigRetriever.create(vertx, selectConfigOptions())
                .getConfig(ar -> {
                    if (ar.succeeded()) {
                        deployVerticles(ar.result(), future);
                    } else {
                        logger.debug("Failed to retrieve the configuration.");
                        future.fail(ar.cause());
                    }
                });
    }


    private ConfigRetrieverOptions selectConfigOptions(){
        ConfigRetrieverOptions options = new ConfigRetrieverOptions();

        if (System.getenv("KUBERNETES_NAMESPACE") != null) {
            ConfigStoreOptions appStore = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("properties")
                    .setConfig(new JsonObject()
                            .put("name", System.getenv("APP_CONFIGMAP_NAME"))
                            .put("key", System.getenv("APP_CONFIGMAP_KEY"))
                            .put("path", "/deployments/config/app-config.properties"));
            options.addStore(appStore);
        } else {
            ConfigStoreOptions props = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("properties")
                    .setConfig(new JsonObject().put("path", "local-app-config.properties"));
            options.addStore(props);
        }

        return options;
    }


    private void deployVerticles(JsonObject config, Future<Void> future){


        Future<String> rFuture = Future.future();
        Future<String> cFuture = Future.future();
        Future<String> pFuture = Future.future();
        Future<String> hFuture = Future.future();

        DeploymentOptions options = new DeploymentOptions();

        options.setConfig(config);
        vertx.deployVerticle(new SimulationControl(), options, rFuture.completer());
        vertx.deployVerticle(new ResponderConsumerVerticle(), options, cFuture.completer());
        vertx.deployVerticle(new ResponderProducerVerticle(), options, cFuture.completer());
        //vertx.deployVerticle(new HttpApplication(), options, hFuture.completer());


        CompositeFuture.all(rFuture, cFuture, pFuture).setHandler(ar -> {
            if (ar.succeeded()) {
                logger.info("Verticles deployed successfully.");
                future.complete();
            } else {
                logger.error("WARNINIG: Verticles NOT deployed successfully.");
                future.fail(ar.cause());
            }
        });

    }




    // Used for debugging in IDE
    public static void main(String[] args) {
        io.vertx.reactivex.core.Vertx vertx = io.vertx.reactivex.core.Vertx.vertx();

        vertx.rxDeployVerticle(Main.class.getName())
                .subscribe();
    }

}

