package com.ponkratov.airport.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsController {

    @FXML
    public Button logoutButton;

    @FXML
    private Button actionButton;

    @FXML
    private Label menuHeaderLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField reNewPasswordField;

    public void initialize() {
        messageLabel.setText("");
    }

    public void onActionButton(ActionEvent event) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.CHANGEPASSWORD);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.USERPASS, oldPasswordField.getText());
        params.put(RequestAttribute.USERPASSNEW, newPasswordField.getText());
        params.put(RequestAttribute.USERPASSRE, reNewPasswordField.getText());
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        messageLabel.setText(response.getResponseMessage());
    }

    public void onLogoutButton(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.LOGOUT);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        messageLabel.setText(response.getResponseMessage());

        MainWindow baseMenu = new MainWindow();
        baseMenu.showWindow();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }
}
