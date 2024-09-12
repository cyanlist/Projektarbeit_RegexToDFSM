package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import java.awt.*;

/**
 * FSMResultPanel displays the result of the FSM operations with collapsible details.
 * It inherits common functionalities from AbstractFSMPanel.
 */
public class FSMResultPanel extends AbstractFSMPanel {

    private boolean isCollapsed = true; // Flag to track the collapsed state
    private final int step = stepCounter++; // Step number for tracking FSM operations
    private int subStep = 1;

    /**
     * Constructor for FSMResultPanel, initializing FSMGroup and toggling details.
     * @param fsmGroup The FSMGroup to display results for.
     */
    public FSMResultPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        this.toggleDetails();
    }

    /**
     * Toggles between the minimized and expanded views of the FSM details.
     */
    private void toggleDetails() {
        this.detailsPanel.removeAll();
        if (this.isCollapsed) {
            addMinimizedView();
        } else {
            addExpandedView();
        }
        this.revalidate();
        this.repaint();
    }

    /**
     * Adds a minimized view of the FSM with basic details.
     */
    private void addMinimizedView() {
        GridBagConstraints gbc = createDefaultGBC();
        this.detailsPanel.add(createToggleButton(), gbc);
        FSMStructure fsm = fsmGroup.getMinimizedFSM();
        this.setBorder(createTitledBorder(fsm, "Step " + step + ": " + fsm.getExpression()));
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.CENTER;
        this.detailsPanel.add(new FSMVisualizer(fsm), gbc);
    }

    /**
     * Adds an expanded view with detailed FSM information.
     */
    private void addExpandedView() {
        GridBagConstraints gbc = createDefaultGBC();
        this.subStep = 1;
        this.detailsPanel.add(createToggleButton(), gbc);
        gbc.gridy = 0;
        addFSMDetails(gbc, fsmGroup.getOperationFSM(), "Operation FSM" );
        addFSMDetails(gbc, fsmGroup.getDeterministicFSM(), "Deterministic FSM" );
        addFSMDetails(gbc, fsmGroup.getMinimizedFSM(), "Minimized FSM");
    }

    /**
     * Creates a toggle button to switch between expanded and minimized views.
     * @return JButton to toggle FSM details.
     */
    private JButton createToggleButton() {
        JButton toggleButton = new JButton(isCollapsed ? "Show Details" : "Hide Details");
        toggleButton.addActionListener(e -> {
            this.isCollapsed = !isCollapsed;
            this.toggleDetails();
        });
        return toggleButton;
    }

    @Override
    protected void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description) {
        gbc.gridy++;

        JTextArea step = setupTextArea(new JTextArea(this.step + "." + this.subStep++ + ": " + description));
        detailsPanel.add(step, gbc);
        gbc.gridy++;

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        splitPane.setLeftComponent(new FSMVisualizer(fsm));

        JTextArea definitionArea = setupTextArea(new JTextArea(fsm.toString()));
        splitPane.setRightComponent(new JScrollPane(definitionArea));

        gbc.gridy++;
        detailsPanel.add(splitPane, gbc);

        gbc.gridy++;
        JTextArea explanationArea = setupTextArea(new JTextArea(fsm.getExplanation()));
        detailsPanel.add(explanationArea, gbc);
        gbc.gridy++;
    }
}
