package com.ponkratov.airport.client.tcpconnection;

public class CommandResult {
    private String responseStatus;
    private String responseMessage;
    private String responseData;

    public String getResponseStatus() {
        return responseStatus;
    }

    public String getResponseData() {
        return responseData;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public CommandResult() {}

    public CommandResult(String responseStatus, String responseMessage, String responseData) {
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
        this.responseData = responseData;
    }
}
