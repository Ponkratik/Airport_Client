package com.ponkratov.airport.client.util;

import com.ponkratov.airport.client.entity.Entity;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;

public class TextReportGenerator<T extends Entity> {
    public boolean generateReport(Window window, ObservableList<T> observableList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение текстового отчёта");
        fileChooser.setInitialFileName("report");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Текстовый файл", "*.txt"));

        try {
            File file = fileChooser.showSaveDialog(window);
            FileWriter fileWriter = new FileWriter(file);
            for (T member : observableList) {
                fileWriter.write(member.toReport());
                fileWriter.write("\n\n");
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
