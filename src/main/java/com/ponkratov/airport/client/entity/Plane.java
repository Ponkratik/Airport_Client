package com.ponkratov.airport.client.entity;

import java.util.Objects;

public class Plane implements Entity {
    private int planeID;
    private String planeModel;
    private String planeNumber;
    private int seatsQuantity;

    private Plane(int planeID, String planeModel, String planeNumber, int seatsQuantity) {
        this.planeID = planeID;
        this.planeModel = planeModel;
        this.planeNumber = planeNumber;
        this.seatsQuantity = seatsQuantity;
    }

    public int getPlaneID() {
        return planeID;
    }

    public String getPlaneModel() {
        return planeModel;
    }

    public String getPlaneNumber() {
        return planeNumber;
    }

    public int getSeatsQuantity() {
        return seatsQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return planeID == plane.planeID
                && seatsQuantity == plane.seatsQuantity
                && Objects.equals(planeModel, plane.planeModel)
                && Objects.equals(planeNumber, plane.planeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planeID, planeModel, planeNumber, seatsQuantity);
    }

    public String toReport() {
        return "ID самолёта: " + planeID
                + "\nМодель: " + planeModel
                + "\nРег.номер: " + planeNumber
                + "\nКол-во мест: " + seatsQuantity;
    }

    public static class PlaneBuilder {
        private int planeID;
        private String planeModel;
        private String planeNumber;
        private int seatsQuantity;

        public PlaneBuilder setPlaneID(int planeID) {
            this.planeID = planeID;
            return this;
        }

        public PlaneBuilder setPlaneModel(String planeModel) {
            this.planeModel = planeModel;
            return this;
        }

        public PlaneBuilder setPlaneNumber(String planeNumber) {
            this.planeNumber = planeNumber;
            return this;
        }

        public PlaneBuilder setSeatsQuantity(int seatsQuantity) {
            this.seatsQuantity = seatsQuantity;
            return this;
        }

        public Plane createPlane() {
            return new Plane(planeID, planeModel, planeNumber, seatsQuantity);
        }
    }
}
