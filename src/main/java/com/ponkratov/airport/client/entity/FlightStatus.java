package com.ponkratov.airport.client.entity;

public class FlightStatus implements Entity {
    private int flightStatusID;
    private String statusName;

    public FlightStatus() {
    }

    public FlightStatus(int flightStatusID, String statusName) {
        this.flightStatusID = flightStatusID;
        this.statusName = statusName;
    }

    public int getFlightStatusID() {
        return flightStatusID;
    }

    public String getStatusName() {
        return statusName;
    }

    @Override
    public String toReport() {
        return null;
    }
}
