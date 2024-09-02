package regToDEA.main.gui;

import javax.swing.*;
import java.awt.*;

public class TogglePanelWithButton extends JPanel {

    private final JButton toggleButton;
    private final JPanel contentPanel;
    private boolean isContentVisible;

    public TogglePanelWithButton(String buttonText, String hiddenPanelTitle, boolean startVisible) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Initialisiere den Inhalt
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createTitledBorder(hiddenPanelTitle));
        isContentVisible = startVisible;
        contentPanel.setVisible(isContentVisible);

        // Initialisiere den Button
        toggleButton = new JButton(buttonText);
        toggleButton.addActionListener(e -> toggleContentVisibility());

        add(toggleButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(contentPanel);
    }

    private void toggleContentVisibility() {
        isContentVisible = !isContentVisible;
        contentPanel.setVisible(isContentVisible);
        toggleButton.setText(isContentVisible ? "Hide" : "Show");
        revalidate();
        repaint();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setButtonText(String text) {
        toggleButton.setText(text);
    }

    public boolean isContentVisible() {
        return isContentVisible;
    }

    public void showContent() {
        isContentVisible = true;
        contentPanel.setVisible(true);
        toggleButton.setText("Hide");
    }

    public void hideContent() {
        isContentVisible = false;
        contentPanel.setVisible(false);
        toggleButton.setText("Show");
    }
}

