module com.ponkratov.airport.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.ponkratov.airport.client to javafx.fxml;
    exports com.ponkratov.airport.client;

    opens com.ponkratov.airport.client.entity to com.fasterxml.jackson.databind;
    exports com.ponkratov.airport.client.entity;

    opens com.ponkratov.airport.client.tcpconnection to com.fasterxml.jackson.databind;
    exports com.ponkratov.airport.client.tcpconnection;
}