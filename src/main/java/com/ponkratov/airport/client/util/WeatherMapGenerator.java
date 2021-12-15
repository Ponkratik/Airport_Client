package com.ponkratov.airport.client.util;

import com.ponkratov.airport.client.entity.Flight;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class WeatherMapGenerator {
    public boolean generateWeatherMap(Window window, Flight flight) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение карты погоды");
        fileChooser.setInitialFileName("weatherMap");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Текстовый файл", "*.txt"));

        try {
            File file = fileChooser.showSaveDialog(window);

            if (file == null) {
                return false;
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(generateWeather(flight));
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateWeather(Flight flight) {
        Timestamp datetime = flight.getArrTime();
        if (datetime.getMonth() >= 2 && datetime.getMonth() < 5) {
            if (datetime.getDay() == 1) {
                return "Ясно, +5С";
            }
            if (datetime.getDay() == 2) {
                return "Дождь, +2С";
            }
            if (datetime.getDay() == 3) {
                return "Мороз, -1С";
            }
            if (datetime.getDay() == 4) {
                return "Ветер, +2";
            }
            if (datetime.getDay() == 5) {
                return "Ясно, +6";
            }
            if (datetime.getDay() == 6) {
                return "Дождь, +5";
            }
        }   else if (datetime.getMonth() >= 5 && datetime.getMonth() < 8) {
            if (datetime.getDay() == 1) {
                return "Ясно, +25С";
            }
            if (datetime.getDay() == 2) {
                return "Дождь, +20С";
            }
            if (datetime.getDay() == 3) {
                return "Туман, +17С";
            }
            if (datetime.getDay() == 4) {
                return "Ветер, +16";
            }
            if (datetime.getDay() == 5) {
                return "Ясно, +26";
            }
            if (datetime.getDay() == 6) {
                return "Дождь, +19";
            }
        }   else if (datetime.getMonth() >= 8 && datetime.getMonth() < 11) {
            if (datetime.getDay() == 1) {
                return "Ясно, +15С";
            }
            if (datetime.getDay() == 2) {
                return "Дождь, +12С";
            }
            if (datetime.getDay() == 3) {
                return "Туман, +10С";
            }
            if (datetime.getDay() == 4) {
                return "Ветер, +12";
            }
            if (datetime.getDay() == 5) {
                return "Ясно, +20";
            }
            if (datetime.getDay() == 6) {
                return "Дождь, +13";
            }
        } else {
            if (datetime.getDay() == 1) {
                return "Ясно, -10С";
            }
            if (datetime.getDay() == 2) {
                return "Мороз, -15С";
            }
            if (datetime.getDay() == 3) {
                return "Мороз, -17С";
            }
            if (datetime.getDay() == 4) {
                return "Ветер, -15С";
            }
            if (datetime.getDay() == 5) {
                return "Ясно, 0С";
            }
            if (datetime.getDay() == 6) {
                return "Дождь, +1";
            }
        }

        return "Не опеределено";
    }
}
