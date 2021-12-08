package com.ponkratov.airport.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.entity.Flight;
import com.ponkratov.airport.client.entity.FlightStatus;
import com.ponkratov.airport.client.entity.Plane;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class OwnFlightsViewController {

    @FXML
    public TableView<Flight> contentTableView;
    public TableView.TableViewSelectionModel<Flight> selectionModel;

    @FXML
    public Label menuHeaderLabel;

    @FXML
    public Label planeNumberLabel;

    @FXML
    public TextField planeNumberField;

    @FXML
    public Label flightStatusLabel;

    @FXML
    public TextField flightStatusNameField;

    @FXML
    public DatePicker depTimePicker;

    @FXML
    public Label messageLabel;

    public void initialize() {
        messageLabel.setText("");

        TableColumn<Flight, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("flightID"));
        contentTableView.getColumns().add(idColumn);
        TableColumn<Flight, Timestamp> depTimeColumn = new TableColumn<>("Время вылета");
        depTimeColumn.setCellValueFactory(new PropertyValueFactory<Flight, Timestamp>("depTime"));
        contentTableView.getColumns().add(depTimeColumn);
        TableColumn<Flight, Timestamp> arrTimeColumn = new TableColumn<>("Время прилёта");
        arrTimeColumn.setCellValueFactory(new PropertyValueFactory<Flight, Timestamp>("arrTime"));
        contentTableView.getColumns().add(arrTimeColumn);
        TableColumn<Flight, String> IATACodeColumn = new TableColumn<>("Аэропорт");
        IATACodeColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("IATACode"));
        contentTableView.getColumns().add(IATACodeColumn);
        TableColumn<Flight, Boolean> arrivalColumn = new TableColumn<>("Прибывающий");
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<Flight, Boolean>("isArrival"));
        contentTableView.getColumns().add(arrivalColumn);
        TableColumn<Flight, Integer> planeIDColumn = new TableColumn<>("Самолёт");
        planeIDColumn.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("planeID"));
        contentTableView.getColumns().add(planeIDColumn);
        TableColumn<Flight, Integer> flightStatusIDColumn = new TableColumn<>("Статус");
        flightStatusIDColumn.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("flightStatusID"));
        contentTableView.getColumns().add(flightStatusIDColumn);

        selectionModel = contentTableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, flight, t1) -> {
            if (t1 != null) {
                try {
                    fillFields(t1);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        depTimePicker.getEditor().textProperty().addListener((observableValue, date, t1) -> {
            if (t1 != null) {
                try {
                    contentTableView.setItems(findDepArrFlights(depTimePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                    selectionModel.selectFirst();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        depTimePicker.setValue(LocalDate.now());
    }

    public ObservableList<Flight> findDepArrFlights(String date) throws IOException, ClassNotFoundException {
        ObservableList<Flight> flights= FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDOWNFLIGHTS);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.DEPTIME, date);
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            Flight.FlightBuilder[] temp = new ObjectMapper().readValue(response.getResponseData(), Flight.FlightBuilder[].class);
            for (Flight.FlightBuilder flightBuilder : temp) {
                flights.add(flightBuilder.createFlight());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        return flights;
    }

    public void fillFields(Flight flight) throws IOException, ClassNotFoundException {
        planeNumberField.setText(getPlaneNumber(flight.getPlaneID()));
        flightStatusNameField.setText(getflightStatusName(flight.getFlightStatusID()));
    }

    public String getflightStatusName(int flightStatusID) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.FINDFLIGHTSTATUSBYID);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.FLIGHTSTATUSID, String.valueOf(flightStatusID));
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
            return new ObjectMapper().readValue(response.getResponseData(), FlightStatus.class).getStatusName();
        } else {
            messageLabel.setText(response.getResponseMessage());
            return "";
        }
    }

    public String getPlaneNumber(int planeID) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.FINDPLANEBYID);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.PLANEID, String.valueOf(planeID));
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            Plane.PlaneBuilder temp1 = new ObjectMapper().readValue(response.getResponseData(), Plane.PlaneBuilder.class);
            Plane plane;
            plane = temp1.createPlane();

            return plane.getPlaneNumber();
        } else {
            messageLabel.setText(response.getResponseMessage());
            return "";
        }
    }
}
