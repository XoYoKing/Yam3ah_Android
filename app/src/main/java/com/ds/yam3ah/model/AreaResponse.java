package com.ds.yam3ah.model;

/**
 * Created by Shivangi on 6/4/2015.
 */
public class AreaResponse {

    private Status status;
    private ResponseData responseData;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }
}
