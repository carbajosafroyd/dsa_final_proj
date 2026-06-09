/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 *
 * Launcher class — this is the application entry point.
 *
 * IMPORTANT DESIGN NOTE:
 * This class does NOT extend javafx.application.Application.
 * This is intentional. When JavaFX is on the classpath (not module path),
 * Java requires the main class to be a plain class that delegates to
 * Application.launch(). This is the standard workaround used in production
 * JavaFX applications with Maven.
 */

package com.mycompany.dsa_final_proj;

import javafx.application.Application;
import com.mycompany.dsa_final_proj.ui.MainApp;

/**
 * Application launcher.
 * Delegates to {@link MainApp} which contains the actual JavaFX setup.
 *
 * @author DNSC IT221 Team
 */
public class Dsa_final_proj {

    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
