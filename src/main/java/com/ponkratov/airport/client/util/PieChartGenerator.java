package com.ponkratov.airport.client.util;

import java.util.HashMap;
import java.util.Map;

public class PieChartGenerator {
    private static String menuHeaderLabel;
    private static Map<String, Integer> pieChartData;

    public static String getMenuHeaderLabel() {
        return menuHeaderLabel;
    }

    public static void setMenuHeaderLabel(String menuHeaderLabel) {
        PieChartGenerator.menuHeaderLabel = menuHeaderLabel;
    }

    public static Map<String, Integer> getPieChartData() {
        return pieChartData;
    }

    public static void setPieChartData(Map<String, Integer> pieChartData) {
        PieChartGenerator.pieChartData = pieChartData;
    }
}
