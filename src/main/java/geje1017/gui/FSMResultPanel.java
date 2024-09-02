package regToDEA.main.gui;

import regToDEA.main.logic.finiteStateMachine.FSMStructure;
import regToDEA.main.logic.finiteStateMachine.FSMVisualizer;

import javax.swing.*;
import java.awt.*;

public class FSMResultPanel extends JPanel {

    private boolean isCollapsed = true;
    private final FSMGroup fsmGroup;
    private final JPanel detailsPanel;

    public FSMResultPanel(FSMGroup fsmGroup) {
        this.fsmGroup = fsmGroup;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 50, 10, 50), // Äußerer Rand
                BorderFactory.createEtchedBorder() // Innerer geätzter Rand
        ));

        this.detailsPanel = new JPanel();
        this.detailsPanel.setLayout(new GridBagLayout());
        this.add(detailsPanel);

        this.toggleDetails(); // Initialize the view based on the collapse state
    }

    private void toggleDetails() {
        this.detailsPanel.removeAll(); // Remove all components before adding new ones
        if (this.isCollapsed) {
            this.addMinimizedView();
        } else {
            this.addExpandedView();
        }
        this.revalidate();
        this.repaint();
    }

    private void addMinimizedView() {
        this.detailsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across the end of row

        // Toggle button configuration
        JButton toggleButton = createToggleButton();
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.detailsPanel.add(toggleButton, gbc);

        // Configuration for each FSM's details
        FSMStructure fsm = fsmGroup.getMinimizedFSM();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        this.detailsPanel.add(new JLabel("Step: " + fsm.getExpression()), gbc);

        // Visualizer setup
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        this.detailsPanel.add(new FSMVisualizer(fsm), gbc);
    }

    private void addExpandedView() {
        detailsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across the end of row

        // Toggle button configuration
        JButton toggleButton = createToggleButton();
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.detailsPanel.add(toggleButton, gbc);

        // Configuration for each FSM's details
        this.addFSMDetails(gbc, fsmGroup.getOperationFSM(), "Operation FSM", 1);
        this.addFSMDetails(gbc, fsmGroup.getDeterministicFSM(), "Deterministic FSM", 4);
        this.addFSMDetails(gbc, fsmGroup.getMinimizedFSM(), "Minimized FSM", 7);
    }

    private void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description, int startRow) {
        // Label with expression
        gbc.gridx = 0;
        gbc.gridy = startRow;
        gbc.gridwidth = 2;
        this.detailsPanel.add(new JLabel(description + ": " + fsm.getExpression()), gbc);

        // Visualizer setup
        gbc.gridy = startRow + 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        this.detailsPanel.add(new FSMVisualizer(fsm), gbc);

        // Definition area setup
        JTextArea definitionArea = new JTextArea(fsm.toString());
        this.setupTextArea(definitionArea);
        gbc.gridx = 1;
        this.detailsPanel.add(new JScrollPane(definitionArea), gbc);

        // Explanation area setup
        JTextArea explanationArea = new JTextArea("Explanation for " + description, 5, 40);
        this.setupTextArea(explanationArea);
        gbc.gridx = 0;
        gbc.gridy = startRow + 2;
        gbc.gridwidth = 2; // Span across two columns
        this.detailsPanel.add(new JScrollPane(explanationArea), gbc);
    }

    private JButton createToggleButton() {
        JButton toggleButton = new JButton(isCollapsed ? "Show Details" : "Hide Details");
        toggleButton.addActionListener(e -> {
            this.isCollapsed = !isCollapsed;
            this.toggleDetails();
        });
        return toggleButton;
    }

    private void setupTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
    }
}