package geje1017.gui.customGuiElements;

import geje1017.gui.FSMGroup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Represents an abstract panel used to display information about finite state machines (FSMs).
 * This class provides common setup methods and layout management for subclasses that visualize FSMs.
 * Subclasses must implement the {@code setupPanel()} method to define specific UI components.
 */
public abstract class AbstractFSMPanel extends JPanel {

    protected FSMGroup fsmGroup;
    protected JPanel detailsPanel;

    protected static int step = 0;

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
        setupPanel();
    }

    /**
     * Abstract method that must be implemented by subclasses to define specific UI components
     * and layout for the panel.
     */
    protected abstract void setupPanel();

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
                BorderFactory.createEmptyBorder(40, 0, 0, 0),
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
     * @param textArea The {@code JTextArea} to be configured.
     * @return The configured {@code JTextArea}.
     */
    protected JTextArea setupTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBorder(null);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return textArea;
    }

    // Getter and setter methods

    protected void resetStep() {
        step = 1;
    }

    protected int getStep() {
        step++;
        return step;
    }
}
