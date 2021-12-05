package com.ponkratov.airport.client;

import com.ponkratov.airport.client.entity.Flight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class FlightManagementController {

    @FXML
    public Button addButton;

    @FXML
    public Button editButton;

    @FXML
    public Button viewButton;

    @FXML
    public TableView<Flight> contentTableView;

    @FXML
    public Button editNavigationButton;

    @FXML
    public AnchorPane viewPane;

    @FXML
    public AnchorPane addPane;

    @FXML
    public Label addHeaderLabel;

    @FXML
    public DatePicker depTimePicker;

    @FXML
    public ComboBox<String> airportComboBox;

    @FXML
    public DatePicker arrTimePicker;

    @FXML
    public CheckBox arrivalCheckBox;

    @FXML
    public ComboBox<String> planeComboBox;

    @FXML
    public Label pilotListLabel;

    @FXML
    public ListView<String> pilotsListView;

    @FXML
    public Button addActionButton;

    @FXML
    public Label stewardListLabel;

    @FXML
    public ListView<String> stewardsListView;

    public void initialize() {

    }

    public void onAddButton(ActionEvent actionEvent) {

    }

    public void onEditButton(ActionEvent actionEvent) {

    }

    public void onViewButton(ActionEvent actionEvent) {

    }

    public void onEditNavigationButton(ActionEvent actionEvent) {

    }

    public void onAddActionButton(ActionEvent actionEvent) {

    }
}
