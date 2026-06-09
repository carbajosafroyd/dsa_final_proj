package com.mycompany.dsa_final_proj.ui.controller;

import com.mycompany.dsa_final_proj.ui.StubServices.StubFacilityService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Label facilityCountLabel;
    @FXML private PieChart facilityTypesChart;

    private final StubFacilityService facilityService = new StubFacilityService();

    @FXML
    public void initialize() {
        facilityCountLabel.setText(String.valueOf(facilityService.getSize()));
        
        // Group facilities by type
        Map<String, Long> typeCounts = facilityService.getAllFacilities().stream()
            .collect(Collectors.groupingBy(f -> f.type, Collectors.counting()));
            
        // Populate Pie Chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Long> entry : typeCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        facilityTypesChart.setData(pieChartData);
    }
}
