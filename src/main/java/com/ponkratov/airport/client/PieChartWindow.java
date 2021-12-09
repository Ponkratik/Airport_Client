package com.ponkratov.airport.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PieChartWindow extends Application {
    Stage stage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("pie-chart.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 540);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setTitle("Airport Client");
        stage.setScene(scene);
        stage.show();
    }

    public void showWindow() throws IOException {
        start(stage);
    }
}
