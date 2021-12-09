package com.ponkratov.airport.client;

import com.ponkratov.airport.client.util.PieChartGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Map;

public class PieChartWindowController {

    @FXML
    private Button closeButton;

    @FXML
    private Label menuHeaderLabel;

    @FXML
    private PieChart customPieChart;

    @FXML
    private Label messageLabel;

    public void initialize() {
        messageLabel.setText("");
        menuHeaderLabel.setText(PieChartGenerator.getMenuHeaderLabel());
        customPieChart.getData().clear();
        PieChartGenerator.getPieChartData().forEach((k, v) -> customPieChart.getData().add(new PieChart.Data(k, v)));
    }

    public void setCustomPieChartData(Map<String, Integer> pieChartData) {
        customPieChart.getData().clear();
        pieChartData.forEach((k, v) -> customPieChart.getData().add(new PieChart.Data(k, v)));
    }

    public void onCloseButton(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
