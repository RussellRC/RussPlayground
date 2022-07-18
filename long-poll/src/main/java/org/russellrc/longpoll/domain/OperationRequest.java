package org.russellrc.longpoll.domain;

public class OperationRequest {
    final String cameraId;
    final int a;
    final int b;

    public OperationRequest(final String cameraId, final int a, final int b) {
        this.cameraId = cameraId;
        this.a = a;
        this.b = b;
    }

    public String getCameraId() {
        return cameraId;
    }

    public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}
}