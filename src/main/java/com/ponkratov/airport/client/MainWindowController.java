package com.ponkratov.airport.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.entity.User;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainWindowController {

    @FXML
    public Button closeButton;

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
    private PasswordField passwordField;

    @FXML
    public void initialize() throws IOException {
        try {
            ClientSocket.getInstance();
            messageLabel.setText("");
        } catch (ConnectException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/airport.png"))));
            alert.setHeaderText("Невозможно подключиться к серверу");
            alert.setContentText("Вероятно, сервер не запущен");
            alert.showAndWait();
            System.exit(0);
        }
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
            User user = new ObjectMapper().readValue(response.getResponseData(), User.UserBuilder.class).createUser();
            ClientSocket.setCurrnetUser(user);
            BaseMenu baseMenu = new BaseMenu();
            baseMenu.showWindow();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        }
    }

    public void onCloseButton(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}