package com.mycompany.dsa_final_proj.ui.controller;

import com.mycompany.dsa_final_proj.ui.StubServices.StubFacilityService;
import com.mycompany.dsa_final_proj.ui.StubServices.Facility;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;
import java.util.stream.Collectors;

public class FacilityFormController {

    @FXML private TableView<Facility> facilityTable;
    @FXML private TableColumn<Facility, String> colName;
    @FXML private TableColumn<Facility, String> colType;
    @FXML private TableColumn<Facility, Double> colX;
    @FXML private TableColumn<Facility, Double> colY;
    @FXML private TableColumn<Facility, String> colDesc;
    @FXML private ComboBox<String> typeFilter;

    private final StubFacilityService facilityService = new StubFacilityService();
    private ObservableList<Facility> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name));
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().type));
        colX.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().x).asObject());
        colY.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().y).asObject());
        colDesc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().description));

        masterData.addAll(facilityService.getAllFacilities());
        facilityTable.setItems(masterData);

        ObservableList<String> types = FXCollections.observableArrayList(
            "ALL", "ACADEMIC", "MEDICAL", "SPORTS", "FOOD_SERVICE", "ADMINISTRATIVE", "UTILITY"
        );
        typeFilter.setItems(types);
        typeFilter.getSelectionModel().selectFirst();
        
        typeFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("ALL")) {
                facilityTable.setItems(masterData);
            } else {
                facilityTable.setItems(FXCollections.observableArrayList(
                    masterData.stream().filter(f -> f.type.equals(newVal)).collect(Collectors.toList())
                ));
            }
        });
    }

    @FXML
    private void handleAdd() {
        showFacilityDialog(null).ifPresent(newFacility -> {
            facilityService.addFacility(newFacility);
            masterData.add(newFacility);
            facilityTable.refresh();
        });
    }

    @FXML
    private void handleEdit() {
        Facility selected = facilityTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a facility to edit.");
            return;
        }

        showFacilityDialog(selected).ifPresent(updatedFacility -> {
            facilityService.removeFacility(selected);
            facilityService.addFacility(updatedFacility);
            masterData.remove(selected);
            masterData.add(updatedFacility);
            facilityTable.refresh();
        });
    }

    @FXML
    private void handleDelete() {
        Facility selected = facilityTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a facility to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Facility");
        alert.setContentText("Are you sure you want to delete " + selected.name + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            facilityService.removeFacility(selected);
            masterData.remove(selected);
        }
    }

    /**
     * Shows a dialog to create or edit a Facility.
     */
    private Optional<Facility> showFacilityDialog(Facility facility) {
        Dialog<Facility> dialog = new Dialog<>();
        dialog.setTitle(facility == null ? "Add Facility" : "Edit Facility");
        dialog.setHeaderText("Fill the details and click on the map below to set the exact location.");

        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("root-pane");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(15);
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        TextField nameField = new TextField(facility == null ? "" : facility.name);
        nameField.setPromptText("Facility Name");
        
        ComboBox<String> typeCombo = new ComboBox<>(FXCollections.observableArrayList(
            "ACADEMIC", "MEDICAL", "SPORTS", "FOOD_SERVICE", "ADMINISTRATIVE", "UTILITY"
        ));
        typeCombo.setValue(facility == null ? "ACADEMIC" : facility.type);
        
        TextField descField = new TextField(facility == null ? "" : facility.description);
        descField.setPromptText("Description");

        Label coordLabel = new Label(facility == null ? "Not Set (Click Map below)" : String.format("X: %.1f, Y: %.1f", facility.x, facility.y));
        coordLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        Label l1 = new Label("Name:"); l1.setStyle("-fx-font-weight: bold;");
        Label l2 = new Label("Type:"); l2.setStyle("-fx-font-weight: bold;");
        Label l3 = new Label("Desc:"); l3.setStyle("-fx-font-weight: bold;");
        Label l4 = new Label("Location:"); l4.setStyle("-fx-font-weight: bold;");

        grid.add(l1, 0, 0); grid.add(nameField, 1, 0);
        grid.add(l2, 0, 1); grid.add(typeCombo, 1, 1);
        grid.add(l3, 2, 0); grid.add(descField, 3, 0);
        grid.add(l4, 2, 1); grid.add(coordLabel, 3, 1);

        javafx.scene.layout.StackPane mapStack = new javafx.scene.layout.StackPane();
        mapStack.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-background-color: #ffffff;");
        
        javafx.scene.image.ImageView mapView = new javafx.scene.image.ImageView(
            new javafx.scene.image.Image(getClass().getResourceAsStream("/images/map-v2.png"))
        );
        mapView.setFitWidth(750);
        mapView.setFitHeight(550);
        mapView.setPreserveRatio(true);

        javafx.scene.canvas.Canvas mapCanvas = new javafx.scene.canvas.Canvas(750, 550);
        javafx.scene.canvas.GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        double[] selectedCoords = new double[]{facility == null ? -1 : facility.x, facility == null ? -1 : facility.y};

        Runnable drawPin = () -> {
            gc.clearRect(0, 0, 750, 550);
            if (selectedCoords[0] != -1) {
                gc.setFill(javafx.scene.paint.Color.web("#e74c3c"));
                gc.fillOval(selectedCoords[0] - 8, selectedCoords[1] - 8, 16, 16);
                gc.setStroke(javafx.scene.paint.Color.WHITE);
                gc.setLineWidth(2);
                gc.strokeOval(selectedCoords[0] - 8, selectedCoords[1] - 8, 16, 16);
            }
        };

        drawPin.run();

        mapCanvas.setOnMouseClicked(e -> {
            selectedCoords[0] = e.getX();
            selectedCoords[1] = e.getY();
            coordLabel.setText(String.format("X: %.1f, Y: %.1f", selectedCoords[0], selectedCoords[1]));
            drawPin.run();
        });

        mapStack.getChildren().addAll(mapView, mapCanvas);
        content.getChildren().addAll(grid, mapStack);

        dialog.getDialogPane().setContent(content);

        javafx.scene.Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        Runnable validate = () -> {
            saveButton.setDisable(nameField.getText().trim().isEmpty() || selectedCoords[0] == -1);
        };
        validate.run();
        
        nameField.textProperty().addListener((obs, old, newVal) -> validate.run());
        mapCanvas.setOnMouseReleased(e -> validate.run());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType && selectedCoords[0] != -1) {
                return new Facility(
                    nameField.getText(),
                    selectedCoords[0],
                    selectedCoords[1],
                    typeCombo.getValue(),
                    descField.getText()
                );
            }
            return null;
        });

        return dialog.showAndWait();
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
