package geje1017.gui.customGuiElements;

import geje1017.gui.FSMGroup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Represents an abstract panel used to display information about finite state machines (FSMs).
 * This class provides common setup methods and layout management for subclasses that visualize FSMs.
 * Subclasses must implement the {@code addMinimizedView()} and {@code addExpandedView()} methods
 * to define specific UI components for different views.
 */
public abstract class AbstractFSMPanel extends JPanel {

    protected FSMGroup fsmGroup;
    protected JPanel detailsPanel;

    protected static int step = 0;
    protected boolean isCollapsed = true;

    /**
     * Constructs a new AbstractFSMPanel with the specified FSMGroup and initializes the layout.
     *
     * @param fsmGroup The FSMGroup containing the FSM structures to be displayed.
     */
    public AbstractFSMPanel(FSMGroup fsmGroup) {
        this.fsmGroup = fsmGroup;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.detailsPanel = new JPanel(new GridBagLayout());
        this.add(detailsPanel);
    }

    /**
     * Creates a default {@link GridBagConstraints} object for use with GridBagLayout.
     * This method ensures consistent horizontal filling and weight for components in the layout.
     *
     * @return A {@code GridBagConstraints} object with default settings.
     */
    protected GridBagConstraints createDefaultGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        return gbc;
    }

    /**
     * Creates a titled border for a panel, with the specified FSM structure and title.
     * The border includes an outer empty border and an etched border with a title.
     *
     * @param title The title for the border.
     * @return A {@code Border} object with a title.
     */
    protected Border createTitledBorder(String title) {
        Border outerInnerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 0, 0),
                BorderFactory.createEtchedBorder()
        );
        TitledBorder titledBorder = BorderFactory.createTitledBorder(outerInnerBorder, title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        return titledBorder;
    }

    /**
     * Configures a {@link JTextArea} to wrap words, disable editing, and apply padding.
     * This method ensures that the text area is styled consistently across panels.
     *
     * @param text The {@code JTextArea} to be configured.
     * @return The configured {@code JTextArea}.
     */
    protected JTextArea setupTextArea(String text) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        return textArea;
    }

    /**
     * Toggles the visibility of the detailed view.
     * If the view is collapsed, it shows the minimized view; otherwise, it shows the expanded view.
     * This method is triggered by a toggle button.
     */
    protected void toggleDetails() {
        detailsPanel.removeAll();

        if (isCollapsed) {
            addMinimizedView();
        } else {
            addExpandedView();
        }

        revalidate();
        repaint();
    }

    /**
     * Creates a button to toggle between the minimized and expanded views of the FSM panel.
     * The button text changes depending on whether the view is collapsed or expanded.
     *
     * @return A {@code JButton} to toggle the view.
     */
    protected JButton createToggleButton() {
        JButton toggleButton = new JButton(isCollapsed ? "Show Details" : "Hide Details");
        toggleButton.addActionListener(e -> {
            isCollapsed = !isCollapsed;
            toggleDetails();
        });
        return toggleButton;
    }

    /**
     * Abstract method that must be implemented by subclasses to add the minimized view of the FSM process.
     * This view typically displays a summary or simplified version of the FSM.
     */
    protected abstract void addMinimizedView();

    /**
     * Abstract method that must be implemented by subclasses to add the expanded view of the FSM process.
     * This view typically displays detailed information about the FSM.
     */
    protected abstract void addExpandedView();

    // Getter and setter methods

    protected void resetStep() {
        step = 0;
    }

    protected int getStep() {
        step++;
        return step;
    }
}
