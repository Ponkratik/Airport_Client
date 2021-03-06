package com.ponkratov.airport.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponkratov.airport.client.entity.Role;
import com.ponkratov.airport.client.entity.User;
import com.ponkratov.airport.client.tcpconnection.*;
import com.ponkratov.airport.client.util.PieChartGenerator;
import com.ponkratov.airport.client.util.TextReportGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserManagementController {

    @FXML
    public Label messageLabel;

    @FXML
    public ComboBox<String> searchComboBox;

    @FXML
    public TextField searchField;

    @FXML
    public Label searchLabel;

    @FXML
    public Button searchButton;

    @FXML
    public Button restorePasswordButton;

    @FXML
    public Button reportButton;

    @FXML
    public Button chartButton;

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
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("userID"));
        contentTableView.getColumns().add(idColumn);
        TableColumn<User, String> loginColumn = new TableColumn<>("Login");
        loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        contentTableView.getColumns().add(loginColumn);
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        contentTableView.getColumns().add(emailColumn);
        TableColumn<User, String> lastNameColumn = new TableColumn<>("??????????????");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        contentTableView.getColumns().add(lastNameColumn);
        TableColumn<User, String> firstNameColumn = new TableColumn<>("??????");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        contentTableView.getColumns().add(firstNameColumn);
        TableColumn<User, String> surNameColumn = new TableColumn<>("????????????????");
        surNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("surName"));
        contentTableView.getColumns().add(surNameColumn);
        TableColumn<User, Boolean> blockedColumn = new TableColumn<>("????????????????????????");
        blockedColumn.setCellValueFactory(new PropertyValueFactory<User, Boolean>("isBlocked"));
        contentTableView.getColumns().add(blockedColumn);
        TableColumn<User, Integer> roleIDColumn = new TableColumn<>("ID ????????");
        roleIDColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("roleID"));
        contentTableView.getColumns().add(roleIDColumn);

        fillTable();

        roleComboBox.setItems(findRoles());

        selectionModel = contentTableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, user, t1) -> {
            if (t1 != null && menuHeaderLabel.getText().equals("????????????????????????????")) {
                try {
                    messageLabel.setText("");
                    fillFields(t1);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        ObservableList<String> searchConditions = FXCollections.observableArrayList(
                "??????????",
                "????.??????????",
                "????????",
                "??????"
        );
        searchComboBox.setItems(searchConditions);
        searchComboBox.getSelectionModel().selectFirst();

        if (contentTableView.getItems().size() != 0) {
            onEditButton(null);
        } else {
            onAddButton(null);
        }
    }

    public void onAddButton(ActionEvent event) {
        menuHeaderLabel.setText("??????????????????????");
        contentTableView.setDisable(true);
        blockCheckBox.setVisible(false);
        searchComboBox.setDisable(true);
        searchField.setDisable(true);
        clearFields();
        roleComboBox.getSelectionModel().selectFirst();
        editButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        addButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("????????????????????????????????");
    }

    public void onEditButton(ActionEvent event) throws IOException, ClassNotFoundException {
        menuHeaderLabel.setText("????????????????????????????");
        contentTableView.setDisable(false);
        blockCheckBox.setVisible(true);
        searchComboBox.setDisable(false);
        searchField.setDisable(false);
        addButton.setStyle("-fx-text-fill: #122A3A; -fx-background-color: transparent");
        editButton.setStyle("-fx-background-color: #584694; -fx-text-fill: #FFFFFF");
        actionButton.setText("??????????????????");
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

    public void disableFields(boolean disable) {
        loginField.setDisable(disable);
        emailField.setDisable(disable);
        lastNameField.setDisable(disable);
        firstNameField.setDisable(disable);
        surNameField.setDisable(disable);
        blockCheckBox.setDisable(disable);
        roleComboBox.setDisable(disable);
        actionButton.setDisable(disable);
        addButton.setDisable(disable);
        editButton.setDisable(disable);
    }

    public void fillFields(User user) throws IOException, ClassNotFoundException {
        loginField.setText(user.getLogin());
        emailField.setText(user.getEmail());
        lastNameField.setText(user.getLastName());
        firstNameField.setText(user.getFirstName());
        surNameField.setText(user.getSurName());
        blockCheckBox.setSelected(user.isBlocked());
        roleComboBox.setValue(getRoleName(user.getRoleID()));
    }

    public void fillTable() throws IOException, ClassNotFoundException {
        ObservableList<User> users = findUsers();
        contentTableView.setItems(users);
    }

    public void selectFirst() throws IOException, ClassNotFoundException {
        if (contentTableView.getItems().size() == 0) {
            disableFields(true);
            addButton.setDisable(true);
            editButton.setDisable(true);
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

    public ObservableList<User> findUsers() throws IOException, ClassNotFoundException {
        ObservableList<User> users= FXCollections.observableArrayList();

        Request request = new Request();
        request.setRequestCommand(CommandType.FINDALLUSERS);
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
        ObservableList<String> roles = FXCollections.observableArrayList();

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
        if (menuHeaderLabel.getText().equals("??????????????????????")) {
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
        fillTable();
        onEditButton(null);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
    }

    public void onSearchButton(ActionEvent event) throws IOException, ClassNotFoundException {
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        switch (searchComboBox.getSelectionModel().getSelectedIndex()) {
            case 0 -> {
                request.setRequestCommand(CommandType.FINDUSERSBYLOGINREGEXP);
            }
            case 1 -> {
                request.setRequestCommand(CommandType.FINDUSERBYEMAIL);
            }
            case 2 -> {
                request.setRequestCommand(CommandType.FINDUSERSBYROLE);
            }
            case 3 -> {
                request.setRequestCommand(CommandType.FINDUSERSBYNAMEREGEXP);
            }
        }

        if (searchField.getText().equals("")) {
            request.setRequestCommand(CommandType.FINDALLUSERS);
        }

        params.put(RequestAttribute.SEARCHCONDITION, searchField.getText());
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));

        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        if (response.getResponseStatus().equals(ResponseStatus.OK)) {
            messageLabel.setText("");
            ObservableList<User> users = FXCollections.observableArrayList();
            User.UserBuilder[] temp = new ObjectMapper().readValue(response.getResponseData(), User.UserBuilder[].class);
            for (User.UserBuilder userBuilder : temp) {
                users.add(userBuilder.createUser());
            }
            contentTableView.setItems(users);
            clearFields();
            selectFirst();
        } else {
            messageLabel.setText(response.getResponseMessage());
        }
        onEditButton(null);
    }

    public void onRestorePasswordButton(ActionEvent event) throws IOException, ClassNotFoundException {
        Request request = new Request();
        request.setRequestCommand(CommandType.RESTOREPASSWORD);
        Map<String, String> params = new HashMap<>();
        params.put(RequestAttribute.USERID, String.valueOf(contentTableView.getSelectionModel().getSelectedItem().getUserID()));
        request.setRequestParams(params);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        messageLabel.setText(response.getResponseMessage());
    }

    public void onReportButton(ActionEvent actionEvent) {
        TextReportGenerator<User> reportGenerator= new TextReportGenerator<>();
        boolean isGenerated = reportGenerator.generateReport(reportButton.getScene().getWindow(), contentTableView.getItems());
        if (!isGenerated) {
            messageLabel.setText("???? ?????????????? ?????????????????????????? ??????????");
        }
    }

    public void onChartButton(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        PieChartWindow window = new PieChartWindow();
        window.stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/airport.png"))));
        PieChartGenerator.setMenuHeaderLabel("?????????????????????????? ?????????????????????? ???? ????????????????????");
        Map<String, Integer> data;
        Request request = new Request();
        request.setRequestCommand(CommandType.COUNTUSERSFOREACHROLE);
        ClientSocket.getOos().writeObject(new ObjectMapper().writeValueAsString(request));
        CommandResult response = new ObjectMapper().readValue((String) ClientSocket.getOis().readObject(), CommandResult.class);
        messageLabel.setText(response.getResponseMessage());
        data = (Map<String, Integer>) new ObjectMapper().readValue(response.getResponseData(), Map.class);
        PieChartGenerator.setPieChartData(data);
        window.showWindow();
    }
}
