package com.ponkratov.airport.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.entity.Role;
import com.ponkratov.airport.client.entity.User;
import com.ponkratov.airport.client.tcpconnection.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class UserManagementController {

    @FXML
    private Button actionButton;

    @FXML
    private Button addButton;

    @FXML
    private CheckBox blockCheckBox;

    @FXML
    private TableView<User> contentTableView;

    @FXML
    private Button editButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField loginField;

    @FXML
    private Label menuHeaderLabel;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField surNameField;

    public void initialize() throws IOException, ClassNotFoundException {
        Request request = new Request();
        String command = CommandType.FINDALLUSERS;
        request.setRequestCommand(command);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            User.UserBuilder[] temp = new ObjectMapper().readValue(response.getResponseData(), User.UserBuilder[].class);
            ObservableList<User> users= FXCollections.observableArrayList();
            for (User.UserBuilder userBuilder : temp) {
                users.add(userBuilder.createUser());
            }
            TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("userID"));
            contentTableView.getColumns().add(idColumn);
            TableColumn<User, String> loginColumn = new TableColumn<>("Login");
            loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
            contentTableView.getColumns().add(loginColumn);
            TableColumn<User, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
            contentTableView.getColumns().add(emailColumn);
            TableColumn<User, String> lastNameColumn = new TableColumn<>("Last name");
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
            contentTableView.getColumns().add(lastNameColumn);
            TableColumn<User, String> firstNameColumn = new TableColumn<>("First name");
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
            contentTableView.getColumns().add(firstNameColumn);
            TableColumn<User, String> surNameColumn = new TableColumn<>("Sur name");
            surNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("surName"));
            contentTableView.getColumns().add(surNameColumn);
            TableColumn<User, Boolean> blockedColumn = new TableColumn<>("Is blocked");
            blockedColumn.setCellValueFactory(new PropertyValueFactory<User, Boolean>("isBlocked"));
            contentTableView.getColumns().add(blockedColumn);
            TableColumn<User, Integer> roleIDColumn = new TableColumn<>("Role ID");
            roleIDColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("roleID"));
            contentTableView.getColumns().add(roleIDColumn);
            contentTableView.setItems(users);

            command = CommandType.FINDALLROLES;
            request.setRequestCommand(command);
            ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
            CommandResult response1 = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
            if (response1.getResponseStatus().equals(ResponseStatus.OK)) {
                Role[] temp1 = new ObjectMapper().readValue(response1.getResponseData(), Role[].class);
                ObservableList<String> roles= FXCollections.observableArrayList();
                for (Role role: temp1) {
                    roles.add(role.getRoleName());
                }
                roleComboBox.setItems(roles);
            }


            onEditButton(null);
        }
    }

    public void onAddButton(ActionEvent event) {
        menuHeaderLabel.setText("Добавление");
        contentTableView.setDisable(true);
        clearFields();
        editButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        addButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("Добавить");
    }

    public void onEditButton(ActionEvent event) {
        menuHeaderLabel.setText("Редактирование");
        contentTableView.setDisable(false);
        addButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        editButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("Сохранить");
    }

    public void clearFields() {
        loginField.clear();
        emailField.clear();
        lastNameField.clear();
        firstNameField.clear();
        surNameField.clear();
        blockCheckBox.setSelected(false);
    }

    public void fillFields(User user) {
        loginField.setText(user.getLogin());
        emailField.setText(user.getEmail());
        lastNameField.setText(user.getLastName());
        firstNameField.setText(user.getFirstName());
        surNameField.setText(user.getSurName());
        blockCheckBox.setSelected(user.getIsBlocked());
    }
}
