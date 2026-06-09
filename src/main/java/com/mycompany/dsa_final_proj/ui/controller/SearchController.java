package com.mycompany.dsa_final_proj.ui.controller;

import com.mycompany.dsa_final_proj.ui.StubServices.StubFacilityService;
import com.mycompany.dsa_final_proj.ui.StubServices.Facility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.List;
import java.util.stream.Collectors;

public class SearchController {

    @FXML private TextField searchField;
    @FXML private HBox filterBox;
    @FXML private ListView<Facility> resultsList;
    @FXML private Label resultsCountLabel;

    private final StubFacilityService facilityService = new StubFacilityService();
    private final ObservableList<Facility> allFacilities = FXCollections.observableArrayList();
    private final ObservableList<Facility> filteredFacilities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        allFacilities.addAll(facilityService.getAllFacilities());
        filteredFacilities.addAll(allFacilities);
        
        resultsList.setItems(filteredFacilities);
        
        // Custom Cell Factory to render facilities beautifully in the list
        resultsList.setCellFactory(listView -> new ListCell<Facility>() {
            @Override
            protected void updateItem(Facility item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(5);
                    
                    Label nameLbl = new Label(item.name);
                    nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #1e5b3a;");
                    
                    Label typeLbl = new Label("Type: " + item.type + " | Coordinates: X=" + item.x + ", Y=" + item.y);
                    typeLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #8a9990; -fx-font-weight: bold;");
                    
                    Label descLbl = new Label(item.description);
                    descLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #333333;");
                    
                    box.getChildren().addAll(nameLbl, typeLbl, descLbl);
                    setGraphic(box);
                }
            }
        });

        // Initialize Filter Radio Buttons
        String[] types = {"ALL", "ACADEMIC", "MEDICAL", "SPORTS", "FOOD_SERVICE", "ADMINISTRATIVE", "UTILITY"};
        ToggleGroup group = new ToggleGroup();
        
        for (String type : types) {
            RadioButton rb = new RadioButton(type);
            rb.setToggleGroup(group);
            rb.setStyle("-fx-text-fill: #333333; -fx-cursor: hand;");
            if (type.equals("ALL")) rb.setSelected(true);
            
            // Auto-search when filter changes
            rb.selectedProperty().addListener((obs, old, isSelected) -> {
                if (isSelected) handleSearch();
            });
            filterBox.getChildren().add(rb);
        }
        
        // Live search as you type
        searchField.textProperty().addListener((obs, old, newVal) -> handleSearch());
        
        updateResultsCount();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        
        String selectedType = "ALL";
        for (javafx.scene.Node n : filterBox.getChildren()) {
            RadioButton rb = (RadioButton) n;
            if (rb.isSelected()) {
                selectedType = rb.getText();
                break;
            }
        }
        
        final String typeFilter = selectedType;
        
        List<Facility> results = allFacilities.stream()
            .filter(f -> typeFilter.equals("ALL") || f.type.equals(typeFilter))
            .filter(f -> query.isEmpty() || 
                         f.name.toLowerCase().contains(query) || 
                         f.description.toLowerCase().contains(query))
            .collect(Collectors.toList());
            
        filteredFacilities.setAll(results);
        updateResultsCount();
    }
    
    private void updateResultsCount() {
        resultsCountLabel.setText("Search Results (" + filteredFacilities.size() + " found)");
    }
}
