package org.russellrc.longpoll.controller;

import org.springframework.stereotype.Component;
import org.russellrc.longpoll.domain.OperationRequest;
import org.russellrc.longpoll.domain.OperationResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Path("/jaxrs")
@Component
public class JaxRsController {

    static Map<String, OperationRequest> REQUESTS = new ConcurrentHashMap<>();
    static Map<String, OperationResponse> RESPONSES = new ConcurrentHashMap<>();

    // TODO: Could use @Inject, but also @ManagedAsync (see TODO below)
    private static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    /**
     * https://openliberty.io/blog/2019/01/24/async-rest-jaxrs-microprofile.html
     * https://blog.allegro.tech/2014/10/async-rest.html
     * https://mincong.io/2020/03/15/jaxrs-async-processing/
     */
    @POST
    @Path("/accept")
    @Consumes({ "application/json"})
    @Produces({ "application/json" })
    // TODO: instead of manually executing task with Executor you can use @ManagedAsync annotation and Resource.asyncGet() method will be executed by an internal Jersey executor service.
    public void accept(@QueryParam("cameraId") final String cameraId, final @Context ContainerRequestContext context, @Suspended AsyncResponse response) {
        final Runnable command = () -> {
            while (true) {
                try {
                    if (REQUESTS.get(cameraId) != null) {
                        // TODO: this should be a model object
                        final Map<String, Integer> body = Map.of("a", REQUESTS.get(cameraId).getA(), "b", REQUESTS.get(cameraId).getB());
                        // TODO: Resume with a Response object?
                        response.resume(body);
                        break;
                    }
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // restore interrupt
                    response.resume(e);
                }
            }
        };
        EXECUTOR_SERVICE.execute(command);
    }

    @POST
    @Path("/accept2")
    @Consumes({ "application/json"})
    @Produces({ "application/json" })
    // TODO: CompletionStage of a Response??
    public CompletionStage<Object> accept2(@QueryParam("cameraId") final String cameraId, final @Context ContainerRequestContext context) {
        final Supplier<Object> command = () -> {
            while (true) {
                try {
                    if (REQUESTS.get(cameraId) != null) {
                        return Map.of("a", REQUESTS.get(cameraId).getA(), "b", REQUESTS.get(cameraId).getB());
                    }
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // restore interrupt
                    throw new RuntimeException(e);
                }
            }
        };
        final CompletableFuture<Object> future = CompletableFuture.supplyAsync(command, EXECUTOR_SERVICE);
        return future;
    }
}
