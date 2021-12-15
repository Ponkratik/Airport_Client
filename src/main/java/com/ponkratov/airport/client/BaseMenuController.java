package com.ponkratov.airport.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.entity.Role;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;

public class BaseMenuController {

    @FXML
    public Button closeButton;

    @FXML
    public Button planeManagementButton;

    @FXML
    public Button flightManagementButton;

    @FXML
    public Button ownFlightsTabloButton;

    @FXML
    private Button airportTabloButton;

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label currentUserLabel;

    @FXML
    private Label currentUserRoleLabel;

    @FXML
    private AnchorPane headerAnchorPane;

    @FXML
    private VBox mainMenu;

    @FXML
    private Button settingsButton;

    @FXML
    private Button userManagementButton;

    public void initialize() throws IOException, ClassNotFoundException {
        initClock();
        currentUserLabel.setText(ClientSocket.getCurrnetUser().getLastName() + " "
                + ClientSocket.getCurrnetUser().getFirstName() + " "
                + ClientSocket.getCurrnetUser().getSurName());
        currentUserRoleLabel.setText(getRoleName(ClientSocket.getCurrnetUser().getRoleID()));
        mainMenu.getChildren().clear();
        switch (ClientSocket.getCurrnetUser().getRoleID()) {
            case 1 -> {
                mainMenu.getChildren().add(userManagementButton);
                mainMenu.getChildren().add(planeManagementButton);
                mainMenu.getChildren().add(flightManagementButton);
            }
            case 2 -> {
                mainMenu.getChildren().add(flightManagementButton);
            }
            case 3 -> {
                mainMenu.getChildren().add(planeManagementButton);
            }
            case 4, 5 -> {
                mainMenu.getChildren().add(ownFlightsTabloButton);
            }
        }
        mainMenu.getChildren().add(airportTabloButton);
        mainMenu.getChildren().add(settingsButton);

        onAirportTabloButton(null);
    }

    public void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            currentDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            currentTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void onUserManagementButton(ActionEvent event) throws IOException {
        deselectAll();
        userManagementButton.setUnderline(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-management.fxml"));
        AnchorPane newPane = loader.load();
        contentAnchorPane.getChildren().setAll(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
    }

    public void onPlaneManagementButton(ActionEvent event) throws IOException {
        deselectAll();
        planeManagementButton.setUnderline(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plane-management.fxml"));
        AnchorPane newPane = loader.load();
        contentAnchorPane.getChildren().setAll(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
    }

    public void onFlightManagementButton(ActionEvent actionEvent) throws IOException {
        deselectAll();
        flightManagementButton.setUnderline(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("flight-management.fxml"));
        AnchorPane newPane = loader.load();
        contentAnchorPane.getChildren().setAll(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
    }

    public void onOwnFlightsTabloButton(ActionEvent actionEvent) throws IOException {
        deselectAll();
        ownFlightsTabloButton.setUnderline(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("own-flights-view.fxml"));
        AnchorPane newPane = loader.load();
        contentAnchorPane.getChildren().setAll(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
    }


    public void onAirportTabloButton(ActionEvent actionEvent) throws IOException {
        deselectAll();
        airportTabloButton.setUnderline(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("airport-tablo.fxml"));
        AnchorPane newPane = loader.load();
        contentAnchorPane.getChildren().setAll(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
    }

    public void onSettingsButton(ActionEvent event) throws IOException {
        deselectAll();
        settingsButton.setUnderline(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        AnchorPane newPane = loader.load();
        contentAnchorPane.getChildren().setAll(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
    }

    public String getRoleName(int roleID) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.FINDROLEBYID);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.ROLEID, String.valueOf(roleID));
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            return new ObjectMapper().readValue(response.getResponseData(), Role.class).getRoleName();
        } else {
            return "";
        }
    }

    public void deselectAll() {
        for(Node node : mainMenu.getChildren()) {
            Button b = (Button) node;
            b.setUnderline(false);
        }
    }

    public void onCloseButton(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
