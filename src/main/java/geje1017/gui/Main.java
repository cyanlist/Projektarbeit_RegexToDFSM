package geje1017.gui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLightLaf: " + e.getMessage());
        }
        SwingUtilities.invokeLater(Frame::new);
    }

}
