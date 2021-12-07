package com.ponkratov.airport.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.entity.*;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
    public Label stewardListLabel;

    @FXML
    public ListView<String> stewardsListView;

    @FXML
    public Button addEditActionButton;

    @FXML
    public AnchorPane addEditPane;

    @FXML
    public Label messageLabel;

    @FXML
    public ComboBox<String> flightStatusComboBox;

    @FXML
    public Spinner<Integer> depHSpinner;

    @FXML
    public Spinner<Integer> depMSpinner;

    @FXML
    public Spinner<Integer> arrHSpinner;

    @FXML
    public Spinner<Integer> arrMSpinner;

    private TableView.TableViewSelectionModel<Flight> selectionModel;

    public void initialize() throws IOException, ClassNotFoundException {
        messageLabel.setText("");
        editButton.setDisable(true);
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

        fillTable();

        selectionModel = contentTableView.getSelectionModel();

        airportComboBox.setItems(findAirports());
        planeComboBox.setItems(findPlanes());
        flightStatusComboBox.setItems(findFlightStatuses());

        depHSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 1));
        depMSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 1));
        arrHSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 1));
        arrMSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 1));

        pilotsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        stewardsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        pilotsListView.setItems(findAllUsersByRole(4));
        stewardsListView.setItems(findAllUsersByRole(5));

        onViewButton(null);
    }

    public void onAddButton(ActionEvent actionEvent) {
        viewPane.setVisible(false);
        addEditPane.setVisible(true);
        addHeaderLabel.setText("Добавление");
        addEditActionButton.setText("Создать");
        editButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        viewButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        addButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        flightStatusComboBox.setVisible(false);
        clearFields();
    }

    public void onEditButton(ActionEvent actionEvent) {
        viewPane.setVisible(false);
        addEditPane.setVisible(true);
        addHeaderLabel.setText("Редактирование");
        addEditActionButton.setText("Сохранить");
        addButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        viewButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        editButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        flightStatusComboBox.setVisible(true);
    }

    public void onViewButton(ActionEvent actionEvent) {
        viewPane.setVisible(true);
        addEditPane.setVisible(false);
        addButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        editButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        viewButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
    }

    public void onEditNavigationButton(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        onEditButton(null);
        fillFields(selectionModel.getSelectedItem());
    }

    public void onAddEditActionButton(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        if (addHeaderLabel.getText().equals("Добавление")) {
            request.setRequestCommand(CommandType.CREATEFLIGHT);
        } else {
            request.setRequestCommand(CommandType.UPDATEFLIGHT);
            params.put(RequestAttribute.FLIGHTID, String.valueOf(selectionModel.getSelectedItem().getFlightID()));
        }

        params.put(RequestAttribute.DEPTIME, depTimePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + depHSpinner.getValue() + ":" + depMSpinner.getValue() + ":00");
        params.put(RequestAttribute.ARRTIME, arrTimePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + arrHSpinner.getValue() + ":" + arrMSpinner.getValue() + ":00");
        params.put(RequestAttribute.IATACODE, airportComboBox.getValue());
        params.put(RequestAttribute.ISARRIVAL, String.valueOf(arrivalCheckBox.isSelected()));
        params.put(RequestAttribute.PLANEID, String.valueOf(getPlaneID(planeComboBox.getValue())));
        params.put(RequestAttribute.FLIGHTSTATUSID, String.valueOf(getFlightStatusID(flightStatusComboBox.getValue())));
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);

        fillTable();
        onViewButton(null);

        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
    }

    public void fillTable() throws IOException, ClassNotFoundException {
        ObservableList<Flight> flights = findAllFlights();
        contentTableView.setItems(flights);
    }

    public void fillFields(Flight flight) throws IOException, ClassNotFoundException {
        depTimePicker.setValue(LocalDate.from(flight.getDepTime().toLocalDateTime()));
        depMSpinner.getValueFactory().setValue(flight.getDepTime().toLocalDateTime().getMinute());
        depHSpinner.getValueFactory().setValue(flight.getDepTime().toLocalDateTime().getHour());
        arrTimePicker.setValue(LocalDate.from(flight.getArrTime().toLocalDateTime()));
        arrHSpinner.getValueFactory().setValue(flight.getArrTime().toLocalDateTime().getHour());
        arrMSpinner.getValueFactory().setValue(flight.getArrTime().toLocalDateTime().getMinute());
        airportComboBox.getSelectionModel().select(flight.getIATACode());
        arrivalCheckBox.setSelected(flight.getIsArrival());
        planeComboBox.getSelectionModel().select(getPlaneNumber(flight.getPlaneID()));
        flightStatusComboBox.getSelectionModel().select(getflightStatusName(flight.getFlightStatusID()));
        //заполнение листвью пилотав и стюардов
    }

    public void clearFields() {
        depTimePicker.setValue(LocalDate.now());
        arrTimePicker.setValue(LocalDate.now());
        depHSpinner.getValueFactory().setValue(1);
        depMSpinner.getValueFactory().setValue(1);
        arrHSpinner.getValueFactory().setValue(1);
        arrMSpinner.getValueFactory().setValue(1);
        airportComboBox.getSelectionModel().selectFirst();
        arrivalCheckBox.setSelected(false);
        planeComboBox.getSelectionModel().selectFirst();
        flightStatusComboBox.getSelectionModel().selectFirst();
        pilotsListView.getSelectionModel().selectFirst();
        stewardsListView.getSelectionModel().selectFirst();
    }

    public ObservableList<Flight> findAllFlights() throws IOException, ClassNotFoundException {
        ObservableList<Flight> flights= FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDALLFLIGHTS);
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

    public ObservableList<String> findAirports() throws IOException, ClassNotFoundException {
        ObservableList<String> airports = FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDALLAIRPORTS);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            Airport[] temp1 = new ObjectMapper().readValue(response.getResponseData(), Airport[].class);
            for (Airport airport: temp1) {
                airports.add(airport.getIatacode());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        return airports;
    }

    public ObservableList<String> findPlanes() throws IOException, ClassNotFoundException {
        ObservableList<String> planes = FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDALLPLANES);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            Plane.PlaneBuilder[] temp = new ObjectMapper().readValue(response.getResponseData(), Plane.PlaneBuilder[].class);
            for (Plane.PlaneBuilder planeBuilder: temp) {
                planes.add(planeBuilder.createPlane().getPlaneNumber());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }

        return planes;
    }

    public ObservableList<String> findFlightStatuses() throws IOException, ClassNotFoundException {
        ObservableList<String> flightStatuses = FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDALLFLIGHTSTATUSES);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            FlightStatus[] temp1 = new ObjectMapper().readValue(response.getResponseData(), FlightStatus[].class);
            for (FlightStatus status: temp1) {
                flightStatuses.add(status.getStatusName());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        return flightStatuses;
    }

    public ObservableList<String> findAllUsersByRole(int roleID) throws IOException, ClassNotFoundException {
        ObservableList<String> users = FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDUSERSBYROLE);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.SEARCHCONDITION, String.valueOf(roleID));
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            User.UserBuilder[] temp1 = new ObjectMapper().readValue(response.getResponseData(), User.UserBuilder[].class);
            for (User.UserBuilder userBuilder: temp1) {
                User user = userBuilder.createUser();
                users.add(user.getLogin());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        return users;
    }

    public int getFlightStatusID(String statusName) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.FINDFLIGHTSTATUSBYNAME);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.STATUSNAME, statusName);
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
            return new ObjectMapper().readValue(response.getResponseData(), FlightStatus.class).getFlightStatusID();
        } else {
            messageLabel.setText(response.getResponseMessage());
            return -1;
        }
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

    public int getPlaneID(String planeNumber) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.FINDPLANEBYNUMBER);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.SEARCHCONDITION, planeNumber);
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            Plane.PlaneBuilder[] temp1 = new ObjectMapper().readValue(response.getResponseData(), Plane.PlaneBuilder[].class);
            Plane plane;
            plane = temp1[0].createPlane();

            return plane.getPlaneID();
        } else {
            messageLabel.setText(response.getResponseMessage());
            return -1;
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
