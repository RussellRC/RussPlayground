package org.russellrc.longpoll.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(allowGetters=true, ignoreUnknown = true)
public class SumResultModel {

    private String cameraId;
    private Integer answer;

    @JsonProperty("camera_id")
    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(final String cameraId) {
        this.cameraId = cameraId;
    }

    @JsonProperty
    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(final Integer answer) {
        this.answer = answer;
    }
}
