package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * AbstractFSMPanel serves as the base class for different FSM-related panels.
 * It provides common functionality such as setting up layout, borders, and components.
 */
public abstract class AbstractFSMPanel extends JPanel {

    protected FSMGroup fsmGroup; // FSMGroup containing FSM structures
    protected JPanel detailsPanel; // Panel for displaying FSM details
    protected static int stepCounter; // Counter to track the current step

    /**
     * Constructor to initialize FSMGroup and layout settings.
     * @param fsmGroup FSMGroup containing the finite state machines.
     */
    public AbstractFSMPanel(FSMGroup fsmGroup) {
        this.fsmGroup = fsmGroup;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.detailsPanel = new JPanel(new GridBagLayout());
        this.add(detailsPanel);
    }

    /**
     * Creates default GridBagConstraints for layout.
     * @return GridBagConstraints object with default settings.
     */
    protected GridBagConstraints createDefaultGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        return gbc;
    }

    /**
     * Sets up a JTextArea with default settings for displaying FSM information.
     * @param textArea JTextArea to be customized.
     * @return Configured JTextArea.
     */
    protected JTextArea setupTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBorder(null);
        return textArea;
    }

    /**
     * Creates a titled border with a specific font and styling.
     * @param fsm The FSMStructure used for display.
     * @param title Title for the border.
     * @return A Border object with a custom title.
     */
    protected Border createTitledBorder(FSMStructure fsm, String title) {
        Border outerInnerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(40, 0, 0, 0),
                BorderFactory.createEtchedBorder()
        );
        TitledBorder titledBorder = BorderFactory.createTitledBorder(outerInnerBorder, title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        return titledBorder;
    }

    /**
     * Abstract method to add details of the FSM. This method must be implemented by subclasses.
     * @param gbc GridBagConstraints for layout.
     * @param fsm FSMStructure containing the FSM data.
     * @param description Description of the FSM.
     */
    protected abstract void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description);

    /**
     * Resets the step counter to 1 for tracking the current FSM step.
     */
    protected void resetStepCounter() {
        AbstractFSMPanel.stepCounter = 1;
    }
}
