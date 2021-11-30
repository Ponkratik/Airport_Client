package com.ponkratov.airport.client;

import com.ponkratov.airport.client.tcpconnection.ClientSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class BaseMenuController {


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

    public void initialize() throws IOException {
        currentUserLabel.setText(ClientSocket.getCurrnetUser().getLastName() + " "
                + ClientSocket.getCurrnetUser().getFirstName() + " "
                + ClientSocket.getCurrnetUser().getSurName());
        currentUserRoleLabel.setText(String.valueOf(ClientSocket.getCurrnetUser().getRoleID()));
        mainMenu.getChildren().clear();
        switch (ClientSocket.getCurrnetUser().getRoleID()) {
            case 1 -> {
                mainMenu.getChildren().add(userManagementButton);
            }
            case 2 -> {

            }
        }
        mainMenu.getChildren().add(airportTabloButton);
        mainMenu.getChildren().add(settingsButton);

        onUserManagementButton(null);
    }

    public void onUserManagementButton(ActionEvent event) throws IOException {
        userManagementButton.setUnderline(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-management.fxml"));
        AnchorPane newPane = loader.load();
        contentAnchorPane.getChildren().setAll(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
    }

    public void onSettingsButton(ActionEvent event) {

    }
}
