package org.russellrc.longpoll.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.russellrc.longpoll.api.model.SumResultModel;
import org.russellrc.longpoll.domain.OperationResponse;
import org.russellrc.longpoll.domain.OperationRequest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("spring")
public class SpringController {

    static Map<String, OperationRequest> REQUESTS = new ConcurrentHashMap<>();
    static Map<String, OperationResponse> RESPONSES = new ConcurrentHashMap<>();
    private static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    @GetMapping(value = "/accept", produces = "application/json")
    @ResponseBody
    public Map<String, Integer> accept(@RequestParam String cameraId) {
        while (true) {
            try {
                if (REQUESTS.get(cameraId) != null) {
                    break;
                }
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Map.of("a", REQUESTS.get(cameraId).getA(), "b", REQUESTS.get(cameraId).getB());
    }

    @PostMapping(value = "/answer", produces = "application/json")
    @ResponseBody
    public SumResultModel executeSum(@RequestBody SumResultModel model) {
        RESPONSES.put(model.getCameraId(), new OperationResponse(model.getCameraId(), model.getAnswer()));
        return model;
    }

    @GetMapping(value = "/doSum", produces = "application/json")
    public DeferredResult<Integer> doSum(@RequestParam String cameraId, @RequestParam int a, @RequestParam int b) {
        final OperationRequest operationRequest = new OperationRequest(cameraId, a, b);
        final DeferredResult<Integer> response = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> waitForResult(response, operationRequest), EXECUTOR_SERVICE);
        return response;
    }


    private static OperationResponse waitForResult(final DeferredResult<Integer> deferredResult, final OperationRequest operationRequest) {
        REQUESTS.put(operationRequest.getCameraId(), operationRequest);
        while (true) {
            try {
                if (RESPONSES.get(operationRequest.getCameraId()) != null) {
                    break;
                }
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        final OperationResponse operationResponse = RESPONSES.get(operationRequest.getCameraId());
        deferredResult.setResult(operationResponse.getResult());
        return operationResponse;
    }

}