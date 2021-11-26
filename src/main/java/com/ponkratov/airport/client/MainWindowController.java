package com.ponkratov.airport.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainWindowController {

    @FXML
    private Label headerLabel;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane loginContainer;

    @FXML
    private AnchorPane loginDataContainer;

    @FXML
    private TextField loginField;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField passwordField;

    @FXML
    public void initialize() throws IOException {
        ClientSocket.getInstance();
        messageLabel.setText("");
    }

    @FXML
    void loginButtonClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        messageLabel.setText("");
        String login = loginField.getText();
        String password = passwordField.getText();
        Request request = new Request();
        request.setRequestCommand(CommandType.AUTHENTICATE);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.USERLOGIN, login);
        params.put(RequestAttribute.USERPASS, password);
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        messageLabel.setText(response.getResponseMessage());
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            System.out.println("Login successful");
        }
    }

}