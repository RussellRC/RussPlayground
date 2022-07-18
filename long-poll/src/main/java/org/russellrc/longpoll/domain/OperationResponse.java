package org.russellrc.longpoll.domain;

public class OperationResponse {
    final String cameraId;
    final int result;

    public OperationResponse(final String cameraId, final int result) {
        this.cameraId = cameraId;
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
