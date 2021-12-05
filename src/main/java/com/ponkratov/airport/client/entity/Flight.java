package com.ponkratov.airport.client.entity;

import java.sql.Timestamp;

public class Flight implements Entity {
    private int flightID;
    private Timestamp depTime;
    private Timestamp arrTime;
    private String IATACode;
    private boolean isArrival;
    private int planeID;
    private int flightStatusID;

    private Flight(int flightID, Timestamp depTime, Timestamp arrTime, String IATACode, boolean isArrival, int planeID, int flightStatusID) {
        this.flightID = flightID;
        this.depTime = depTime;
        this.arrTime = arrTime;
        this.IATACode = IATACode;
        this.isArrival = isArrival;
        this.planeID = planeID;
        this.flightStatusID = flightStatusID;
    }

    public int getFlightID() {
        return flightID;
    }

    public Timestamp getDepTime() {
        return depTime;
    }

    public Timestamp getArrTime() {
        return arrTime;
    }

    public String getIATACode() {
        return IATACode;
    }

    public boolean isArrival() {
        return isArrival;
    }

    public int getPlaneID() {
        return planeID;
    }

    public int getFlightStatusID() {
        return flightStatusID;
    }

    public static class FlightBuilder {
        private int flightID;
        private Timestamp depTime;
        private Timestamp arrTime;
        private String IATACode;
        private boolean isArrival;
        private int planeID;
        private int flightStatusID;

        public FlightBuilder setFlightID(int flightID) {
            this.flightID = flightID;
            return this;
        }

        public FlightBuilder setDepTime(Timestamp depTime) {
            this.depTime = depTime;
            return this;
        }

        public FlightBuilder setArrTime(Timestamp arrTime) {
            this.arrTime = arrTime;
            return this;
        }

        public FlightBuilder setIATACode(String IATACode) {
            this.IATACode = IATACode;
            return this;
        }

        public FlightBuilder setArrival(boolean arrival) {
            isArrival = arrival;
            return this;
        }

        public FlightBuilder setPlaneID(int planeID) {
            this.planeID = planeID;
            return this;
        }

        public FlightBuilder setFlightStatusID(int flightStatusID) {
            this.flightStatusID = flightStatusID;
            return this;
        }

        public Flight createFlight() {
            return new Flight(flightID, depTime, arrTime, IATACode, isArrival, planeID, flightStatusID);
        }
    }
}