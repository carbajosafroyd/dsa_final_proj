package com.mycompany.dsa_final_proj.ui.controller;

import com.mycompany.dsa_final_proj.ui.StubServices.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.application.Platform;
import java.util.List;

public class MapController {

    @FXML private ImageView mapImageView;
    @FXML private Canvas mapCanvas;
    
    @FXML private RadioButton rbNearest;
    @FXML private RadioButton rbKNearest;
    @FXML private RadioButton rbRadius;
    @FXML private Spinner<Integer> kSpinner;
    @FXML private TextField radiusField;
    @FXML private VBox resultContainer;

    private final StubFacilityService facilityService = new StubFacilityService();
    private final StubSearchService searchService = new StubSearchService();
    
    private GraphicsContext gc;
    private ToggleGroup searchGroup;

    @FXML
    public void initialize() {
        gc = mapCanvas.getGraphicsContext2D();
        
        searchGroup = new ToggleGroup();
        rbNearest.setToggleGroup(searchGroup);
        rbKNearest.setToggleGroup(searchGroup);
        rbRadius.setToggleGroup(searchGroup);
        
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3);
        kSpinner.setValueFactory(valueFactory);

        redrawMap();
    }

    private void redrawMap() {
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        
        for (Facility f : facilityService.getAllFacilities()) {
            gc.setFill(getColorForType(f.type));
            gc.fillOval(f.x - 6, f.y - 6, 12, 12);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.5);
            gc.strokeOval(f.x - 6, f.y - 6, 12, 12);
        }
    }

    @FXML
    private void handleCanvasClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        
        if (event.getButton() == MouseButton.SECONDARY) {
            promptAddFacility(x, y);
            return;
        }
        
        redrawMap();
        
        gc.setFill(Color.web("#0b3d22"));
        gc.fillOval(x - 5, y - 5, 10, 10);
        
        resultContainer.getChildren().clear();

        if (rbNearest.isSelected()) {
            SearchResult result = searchService.findNearest(x, y);
            if (result != null) {
                drawResultLine(x, y, result.facility);
                showResultText(List.of(result));
            }
        } else if (rbKNearest.isSelected()) {
            List<SearchResult> results = searchService.findKNearest(x, y, kSpinner.getValue());
            for (SearchResult r : results) {
                drawResultLine(x, y, r.facility);
            }
            showResultText(results);
        } else if (rbRadius.isSelected()) {
            try {
                double r = Double.parseDouble(radiusField.getText());
                gc.setStroke(Color.rgb(11, 61, 34, 0.4));
                gc.setLineWidth(2);
                gc.strokeOval(x - r, y - r, r * 2, r * 2);
                gc.setFill(Color.rgb(11, 61, 34, 0.1));
                gc.fillOval(x - r, y - r, r * 2, r * 2);
                
                List<SearchResult> results = searchService.findWithinRadius(x, y, r);
                for (SearchResult res : results) {
                    drawResultLine(x, y, res.facility);
                }
                showResultText(results);
            } catch (NumberFormatException e) {
                resultContainer.getChildren().add(new Label("Invalid radius value"));
            }
        }
    }
    
    private void drawResultLine(double qx, double qy, Facility f) {
        gc.setStroke(Color.web("#27ae60"));
        gc.setLineWidth(2.5);
        gc.setLineDashes(5);
        gc.strokeLine(qx, qy, f.x, f.y);
        gc.setLineDashes(0);
        
        gc.setStroke(Color.web("#4affa0"));
        gc.setLineWidth(3);
        gc.strokeOval(f.x - 9, f.y - 9, 18, 18);
    }
    
    private void showResultText(List<SearchResult> results) {
        if (results.isEmpty()) {
            Label noRes = new Label("No facilities found.");
            noRes.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 13px;");
            resultContainer.getChildren().add(noRes);
            return;
        }
        
        for (SearchResult r : results) {
            VBox box = new VBox(2);
            box.getStyleClass().add("result-item");
            
            Label nameLbl = new Label(r.facility.name);
            nameLbl.getStyleClass().add("result-name");
            
            Label typeLbl = new Label(r.facility.type);
            typeLbl.getStyleClass().add("result-type");
            
            Label distLbl = new Label(String.format("Distance: %.1f px", r.distance));
            distLbl.getStyleClass().add("result-distance");
            
            box.getChildren().addAll(nameLbl, typeLbl, distLbl);
            resultContainer.getChildren().add(box);
        }
    }

    private Color getColorForType(String type) {
        switch (type) {
            case "ACADEMIC": return Color.web("#3498db");
            case "MEDICAL": return Color.web("#e74c3c");
            case "SPORTS": return Color.web("#e67e22");
            case "FOOD_SERVICE": return Color.web("#f1c40f");
            case "ADMINISTRATIVE": return Color.web("#1e5b3a");
            default: return Color.web("#95a5a6");
        }
    }

    private void promptAddFacility(double x, double y) {
        Dialog<Facility> dialog = new Dialog<>();
        dialog.setTitle("Plot New Facility");
        dialog.setHeaderText(String.format("Auto-generated Coordinates: X=%.1f, Y=%.1f", x, y));
        
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("root-pane");

        ButtonType saveButtonType = new ButtonType("Add Facility", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15);
        grid.setPadding(new Insets(20, 50, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Facility Name");
        nameField.setStyle("-fx-pref-width: 250;");
        
        ComboBox<String> typeCombo = new ComboBox<>(javafx.collections.FXCollections.observableArrayList(
            "ACADEMIC", "MEDICAL", "SPORTS", "FOOD_SERVICE", "ADMINISTRATIVE", "UTILITY"
        ));
        typeCombo.setValue("ACADEMIC");
        typeCombo.setStyle("-fx-pref-width: 250;");
        
        TextField descField = new TextField();
        descField.setPromptText("Description");
        descField.setStyle("-fx-pref-width: 250;");

        Label l1 = new Label("Name:"); l1.setStyle("-fx-font-weight: bold;");
        Label l2 = new Label("Type:"); l2.setStyle("-fx-font-weight: bold;");
        Label l5 = new Label("Desc:"); l5.setStyle("-fx-font-weight: bold;");

        grid.add(l1, 0, 0); grid.add(nameField, 1, 0);
        grid.add(l2, 0, 1); grid.add(typeCombo, 1, 1);
        grid.add(l5, 0, 2); grid.add(descField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> nameField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType && !nameField.getText().trim().isEmpty()) {
                return new Facility(nameField.getText(), x, y, typeCombo.getValue(), descField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(f -> {
            facilityService.addFacility(f);
            redrawMap();
        });
    }
}
