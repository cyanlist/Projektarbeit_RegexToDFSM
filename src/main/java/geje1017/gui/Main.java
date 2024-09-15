package geje1017.gui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * The {@code Main} class is the entry point for the application.
 * It initializes the graphical user interface (GUI) using the FlatLightLaf look and feel
 * and starts the application by creating the main {@code Frame}.
 */
public class Main {

    /**
     * The main method that serves as the entry point to the application.
     * It sets up the FlatLightLaf look and feel and launches the {@code Frame} in a separate thread.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLightLaf: " + e.getMessage());
        }
        SwingUtilities.invokeLater(Frame::new);
    }

}
