package com.ponkratov.airport.client.tcpconnection;

import java.util.Map;

public class Request {
    private String requestCommand;
    private Map<String, String> requestParams;

    public Request(String requestCommand, Map<String, String> requestParams) {
        this.requestCommand = requestCommand;
        this.requestParams = requestParams;
    }

    public Request() {
    }

    public String getRequestCommand() {
        return requestCommand;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestCommand(String requestCommand) {
        this.requestCommand = requestCommand;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }
}
