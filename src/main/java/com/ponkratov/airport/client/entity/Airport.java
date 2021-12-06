package com.ponkratov.airport.client.entity;

public class Airport implements Entity {
    private String iatacode;
    private String country;
    private String city;

    public Airport() {
    }

    public Airport(String iatacode, String country, String city) {
        this.iatacode = iatacode;
        this.country = country;
        this.city = city;
    }

    public String getIatacode() {
        return iatacode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}