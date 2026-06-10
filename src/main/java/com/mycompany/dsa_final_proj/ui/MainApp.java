/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 *
 * MainApp — the JavaFX Application class.
 *
 * This class is responsible for:
 * - Initializing the JavaFX runtime
 * - Creating the primary stage (window)
 * - Loading the root layout
 * - Applying the application stylesheet
 *
 * It is NOT launched directly. Instead, {@link com.mycompany.dsa_final_proj.Dsa_final_proj}
 * calls Application.launch(MainApp.class, args).
 */

package com.mycompany.dsa_final_proj.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main JavaFX Application.
 * Sets up the primary window and root layout.
 *
 * @author DNSC IT221 Team
 */
public class MainApp extends Application {

    /** Application window title. */
    private static final String APP_TITLE = "DNSC Smart Campus Facility Finder";

    /** Default window dimensions. */
    private static final double WINDOW_WIDTH = 1200;
    private static final double WINDOW_HEIGHT = 750;

    /** Minimum window dimensions to prevent layout breaking. */
    private static final double MIN_WIDTH = 900;
    private static final double MIN_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        VBox sidebar = buildSidebar(root);
        root.setLeft(sidebar);

        loadScreen(root, "/views/dashboard.fxml");

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        String cssPath = getClass().getResource("/styles/application.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    private VBox buildSidebar(BorderPane root) {
        VBox sidebar = new VBox(5);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(240);

        javafx.scene.layout.HBox logoBox = new javafx.scene.layout.HBox(12);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new javafx.geometry.Insets(30, 20, 30, 25));
        
        try {
            javafx.scene.image.Image logoImage = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/logo.png"));
            javafx.scene.image.ImageView logoView = new javafx.scene.image.ImageView(logoImage);
            logoView.setFitWidth(40);
            logoView.setPreserveRatio(true);
            logoBox.getChildren().add(logoView);
        } catch (Exception e) {
            System.err.println("Could not load logo: " + e.getMessage());
        }
        
        Label brandLabel = new Label("DNSC FACILITY FINDER");
        brandLabel.getStyleClass().add("sidebar-brand");
        logoBox.getChildren().add(brandLabel);
        
        sidebar.getChildren().add(logoBox);

        Label quickLinks = new Label("Quick Links");
        quickLinks.getStyleClass().add("quick-links-header");
        sidebar.getChildren().add(quickLinks);

        String[][] navItems = {
            {"Dashboard",  "/views/dashboard.fxml", "icon-dashboard"},
            {"Map View",   "/views/map.fxml", "icon-map"},
            {"Facilities", "/views/facility_form.fxml", "icon-facilities"},
            {"Search",     "/views/search.fxml", "icon-search"},
            {"Benchmark",  "/views/benchmark.fxml", "icon-benchmark"}
        };

        for (String[] item : navItems) {
            javafx.scene.control.Button btn = new javafx.scene.control.Button(item[0]);
            btn.getStyleClass().add("nav-btn");
            btn.setMaxWidth(Double.MAX_VALUE);
            
            javafx.scene.layout.Region icon = new javafx.scene.layout.Region();
            icon.getStyleClass().addAll("nav-icon", item[2]);
            btn.setGraphic(icon);
            btn.setGraphicTextGap(15);
            
            String fxmlPath = item[1];
            btn.setOnAction(e -> loadScreen(root, fxmlPath));
            sidebar.getChildren().add(btn);
        }

        return sidebar;
    }

    private void loadScreen(BorderPane root, String fxmlPath) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource(fxmlPath)
            );
            root.setCenter(loader.load());
        } catch (Exception e) {
            Label placeholder = new Label("Screen not built yet: " + fxmlPath);
            placeholder.setStyle("-fx-text-fill: #1e5b3a; -fx-font-size: 18px;");
            javafx.scene.layout.StackPane p = new javafx.scene.layout.StackPane(placeholder);
            root.setCenter(p);
        }
    }
}
