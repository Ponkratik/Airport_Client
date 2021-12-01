package com.ponkratov.airport.client;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.HashMap;
import java.util.Map;

public class UserManagementController {

    @FXML
    public Label messageLabel;

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

    private TableView.TableViewSelectionModel<User> selectionModel;

    public void initialize() throws IOException, ClassNotFoundException {
        messageLabel.setText("");

        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("userID"));
        contentTableView.getColumns().add(idColumn);
        TableColumn<User, String> loginColumn = new TableColumn<>("Login");
        loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        contentTableView.getColumns().add(loginColumn);
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        contentTableView.getColumns().add(emailColumn);
        TableColumn<User, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        contentTableView.getColumns().add(lastNameColumn);
        TableColumn<User, String> firstNameColumn = new TableColumn<>("Имя");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        contentTableView.getColumns().add(firstNameColumn);
        TableColumn<User, String> surNameColumn = new TableColumn<>("Отчество");
        surNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("surName"));
        contentTableView.getColumns().add(surNameColumn);
        TableColumn<User, Boolean> blockedColumn = new TableColumn<>("Заблокирован");
        blockedColumn.setCellValueFactory(new PropertyValueFactory<User, Boolean>("isBlocked"));
        contentTableView.getColumns().add(blockedColumn);
        TableColumn<User, Integer> roleIDColumn = new TableColumn<>("ID роли");
        roleIDColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("roleID"));
        contentTableView.getColumns().add(roleIDColumn);

        fillTable();

        ObservableList<String> roles = findRoles();
        roleComboBox.setItems(roles);

        selectionModel = contentTableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, user, t1) -> {
            if (t1 != null && menuHeaderLabel.getText().equals("Редактирование")) {
                try {
                    fillFields(t1);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        onEditButton(null);
    }

    public void onAddButton(ActionEvent event) {
        menuHeaderLabel.setText("Регистрация");
        contentTableView.setDisable(true);
        blockCheckBox.setVisible(false);
        clearFields();
        editButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        addButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("Зарегистрировать");
    }

    public void onEditButton(ActionEvent event) throws IOException, ClassNotFoundException {
        menuHeaderLabel.setText("Редактирование");
        contentTableView.setDisable(false);
        blockCheckBox.setVisible(true);
        addButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        editButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("Сохранить");
        selectFirst();
    }

    public void clearFields() {
        loginField.clear();
        emailField.clear();
        lastNameField.clear();
        firstNameField.clear();
        surNameField.clear();
        blockCheckBox.setSelected(false);
        roleComboBox.setValue("");
    }

    public void fillFields(User user) throws IOException, ClassNotFoundException {
        loginField.setText(user.getLogin());
        emailField.setText(user.getEmail());
        lastNameField.setText(user.getLastName());
        firstNameField.setText(user.getFirstName());
        surNameField.setText(user.getSurName());
        blockCheckBox.setSelected(user.getIsBlocked());
        roleComboBox.setValue(getRoleName(user.getRoleID()));
    }

    public void fillTable() throws IOException, ClassNotFoundException {
        ObservableList<User> users = findUsers();
        contentTableView.setItems(users);
    }

    public void selectFirst() throws IOException, ClassNotFoundException {
        contentTableView.requestFocus();
        contentTableView.getFocusModel().focus(0);
        selectionModel.selectFirst();
        fillFields(selectionModel.getSelectedItem());
    }

    public ObservableList<User> findUsers() throws IOException, ClassNotFoundException {
        ObservableList<User> users= FXCollections.observableArrayList();

        Request request = new Request();
        String command = CommandType.FINDALLUSERS;
        request.setRequestCommand(command);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            User.UserBuilder[] temp = new ObjectMapper().readValue(response.getResponseData(), User.UserBuilder[].class);
            for (User.UserBuilder userBuilder : temp) {
                users.add(userBuilder.createUser());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        return users;
    }

    public ObservableList<String> findRoles() throws IOException, ClassNotFoundException {
        ObservableList<String> roles= FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDALLROLES);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");

            Role[] temp1 = new ObjectMapper().readValue(response.getResponseData(), Role[].class);
            for (Role role: temp1) {
                roles.add(role.getRoleName());
            }
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        return roles;
    }

    public int getRoleID(String roleName) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.FINDROLEBYNAME);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.ROLENAME, roleName);
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
            return new ObjectMapper().readValue(response.getResponseData(), Role.class).getRoleID();
        } else {
            messageLabel.setText(response.getResponseMessage());
            return -1;
        }
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
            messageLabel.setText("");
            return new ObjectMapper().readValue(response.getResponseData(), Role.class).getRoleName();
        } else {
            messageLabel.setText(response.getResponseMessage());
            return "";
        }
    }

    public void onActionButton(ActionEvent event) throws IOException, ClassNotFoundException {
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        if (menuHeaderLabel.getText().equals("Регистрация")) {
            request.setRequestCommand(CommandType.REGISTER);
        } else {
            request.setRequestCommand(CommandType.UPDATEUSER);
            params.put(RequestAttribute.USERID, String.valueOf(selectionModel.getSelectedItem().getUserID()));
            params.put(RequestAttribute.USERCURRENTBLOCK, String.valueOf(blockCheckBox.isSelected()));
        }
        params.put(RequestAttribute.USERLOGIN, loginField.getText());
        params.put(RequestAttribute.USEREMAIL, emailField.getText());
        params.put(RequestAttribute.USERLASTNAME, lastNameField.getText());
        params.put(RequestAttribute.USERFIRSTNAME, firstNameField.getText());
        params.put(RequestAttribute.USERSURNAME, surNameField.getText());
        params.put(RequestAttribute.USERROLEID, String.valueOf(getRoleID(roleComboBox.getValue())));
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
        } else {
            messageLabel.setText(response.getResponseMessage());
        }

        fillTable();
        onEditButton(null);
    }
}
