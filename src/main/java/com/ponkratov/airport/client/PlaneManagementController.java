package com.ponkratov.airport.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.entity.Plane;
import com.ponkratov.airport.client.entity.User;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlaneManagementController {

    @FXML
    public TableView<Plane> contentTableView;
    private TableView.TableViewSelectionModel<Plane> selectionModel;

    @FXML
    public Label messageLabel;

    @FXML
    public ComboBox<String> searchComboBox;

    @FXML
    public Label searchLabel;

    @FXML
    public TextField searchField;

    @FXML
    public Button searchButton;

    @FXML
    public Button addButton;

    @FXML
    public Button editButton;

    @FXML
    public Label menuHeaderLabel;

    @FXML
    public TextField planeModelField;

    @FXML
    public TextField planeNumberField;

    @FXML
    public TextField seatsQuantityField;

    @FXML
    public Button actionButton;

    public void initialize() throws IOException, ClassNotFoundException {
        messageLabel.setText("");

        TableColumn<Plane, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<Plane, Integer>("planeID"));
        contentTableView.getColumns().add(idColumn);
        TableColumn<Plane, String> planeModelColumn = new TableColumn<>("Модель");
        planeModelColumn.setCellValueFactory(new PropertyValueFactory<Plane, String>("planeModel"));
        contentTableView.getColumns().add(planeModelColumn);
        TableColumn<Plane, String> planeNumberColumn = new TableColumn<>("Рег.номер");
        planeNumberColumn.setCellValueFactory(new PropertyValueFactory<Plane, String>("planeNumber"));
        contentTableView.getColumns().add(planeNumberColumn);
        TableColumn<Plane, Integer> seatsQuantityColumn = new TableColumn<>("Кол-во мест");
        seatsQuantityColumn.setCellValueFactory(new PropertyValueFactory<Plane, Integer>("seatsQuantity"));
        contentTableView.getColumns().add(seatsQuantityColumn);

        fillTable();

        ObservableList<String> searchConditions = FXCollections.observableArrayList(
                "Модель",
                "Рег.номер"
        );
        searchComboBox.setItems(searchConditions);
        searchComboBox.getSelectionModel().selectFirst();

        selectionModel = contentTableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, user, t1) -> {
            if (t1 != null && menuHeaderLabel.getText().equals("Редактирование")) {
                fillFields(t1);
            }
        });

        /*if (contentTableView.getItems().size() != 0) {
            onEditButton(null);
        } else {
            onAddButton(null);
        }*/

        onEditButton(null);
    }

    public void onSearchButton(ActionEvent event) throws IOException, ClassNotFoundException {
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        switch (searchComboBox.getSelectionModel().getSelectedIndex()) {
            case 0 -> {
                request.setRequestCommand(CommandType.FINDPLANESBYMODELREGEXP);
            }
            case 1 -> {
                request.setRequestCommand(CommandType.FINDPLANEBYNUMBER);
            }
        }

        if (searchField.getText().equals("")) {
            request.setRequestCommand(CommandType.FINDALLPLANES);
        }

        params.put(RequestAttribute.SEARCHCONDITION, searchField.getText());
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));

        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
            ObservableList<Plane> planes = FXCollections.observableArrayList();
            Plane.PlaneBuilder[] temp = new ObjectMapper().readValue(response.getResponseData(), Plane.PlaneBuilder[].class);
            for (Plane.PlaneBuilder planeBuilder : temp) {
                planes.add(planeBuilder.createPlane());
            }
            contentTableView.setItems(planes);
            selectFirst();
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
    }

    public void onAddButton(ActionEvent event) {
        menuHeaderLabel.setText("Добавление");
        contentTableView.setDisable(true);
        /*searchComboBox.setDisable(true);
        searchField.setDisable(true);*/
        clearFields();
        editButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        addButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("Добавить");
    }

    public void onEditButton(ActionEvent event) {
        menuHeaderLabel.setText("Редактирование");
        contentTableView.setDisable(false);
        /*searchComboBox.setDisable(false);
        searchField.setDisable(false);*/
        clearFields();
        addButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        editButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("Редактирование");
        selectFirst();
    }

    public void onActionButton(ActionEvent event) throws IOException, ClassNotFoundException {
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        if (menuHeaderLabel.getText().equals("Добавление")) {
            request.setRequestCommand(CommandType.CREATEPLANE);
        } else {
            request.setRequestCommand(CommandType.UPDATEPLANE);
            params.put(RequestAttribute.PLANEID, String.valueOf(selectionModel.getSelectedItem().getPlaneID()));
        }
        params.put(RequestAttribute.PLANEMODEL, planeModelField.getText());
        params.put(RequestAttribute.PLANENUMBER, planeNumberField.getText());
        params.put(RequestAttribute.SEATSQUANTITY, seatsQuantityField.getText());
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        fillTable();
        onEditButton(null);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
    }

    public void clearFields() {
        planeModelField.clear();
        planeNumberField.clear();
        seatsQuantityField.clear();
    }

    public void disableFields(boolean disable) {
        planeModelField.setDisable(disable);
        planeNumberField.setDisable(disable);
        seatsQuantityField.setDisable(disable);
        actionButton.setDisable(disable);
    }

    public void fillFields(Plane plane) {
        planeModelField.setText(plane.getPlaneModel());
        planeNumberField.setText(plane.getPlaneNumber());
        seatsQuantityField.setText(String.valueOf(plane.getSeatsQuantity()));
    }

    public void selectFirst() {
        if (contentTableView.getItems().size() == 0) {
            disableFields(true);
            addButton.setDisable(false);
            editButton.setDisable(true);
            onAddButton(null);
        } else {
            disableFields(false);
            addButton.setDisable(false);
            editButton.setDisable(false);
            contentTableView.requestFocus();
            contentTableView.getFocusModel().focus(0);
            selectionModel.selectFirst();
            fillFields(selectionModel.getSelectedItem());
        }
    }

    public void fillTable() throws IOException, ClassNotFoundException {
        contentTableView.setItems(findAll());
    }

    public ObservableList<Plane> findAll() throws IOException, ClassNotFoundException {
        ObservableList<Plane> planes = FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDALLPLANES);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            Plane.PlaneBuilder[] temp = new ObjectMapper().readValue(response.getResponseData(), Plane.PlaneBuilder[].class);
            for (Plane.PlaneBuilder planeBuilder : temp) {
                planes.add(planeBuilder.createPlane());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        return planes;
    }
}
