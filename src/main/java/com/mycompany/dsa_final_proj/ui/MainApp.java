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

        // --- Root Layout ---
        // BorderPane gives us: top (menu/toolbar), left (sidebar),
        // center (map), right (details), bottom (status bar).
        // This matches our architecture's UI layout.
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // --- Placeholder Content (will be replaced in Phase 7) ---
        VBox centerContent = new VBox(12);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getStyleClass().add("center-placeholder");

        Label titleLabel = new Label("DNSC Smart Campus Facility Finder");
        titleLabel.getStyleClass().add("app-title");

        Label subtitleLabel = new Label("Powered by KD-Tree Spatial Search");
        subtitleLabel.getStyleClass().add("app-subtitle");

        Label statusLabel = new Label("Phase 2 Complete — Project skeleton is running.");
        statusLabel.getStyleClass().add("status-label");

        centerContent.getChildren().addAll(titleLabel, subtitleLabel, statusLabel);
        root.setCenter(centerContent);

        // --- Scene Setup ---
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Load the application stylesheet
        String cssPath = getClass().getResource("/styles/application.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        // --- Stage (Window) Configuration ---
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        // Center on screen
        primaryStage.centerOnScreen();

        primaryStage.show();
    }
}
