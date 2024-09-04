package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import java.awt.*;

public class FSMResultPanel extends AbstractFSMPanel {

    private boolean isCollapsed = true;

    public FSMResultPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        this.toggleDetails();
    }

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

    private void addMinimizedView() {
        GridBagConstraints gbc = createDefaultGBC();
        this.detailsPanel.add(createToggleButton(), gbc);
        FSMStructure fsm = fsmGroup.getMinimizedFSM();
        this.setBorder(createTitledBorder(fsm, "Step " + stepCounter++ + ": " + fsm.getExpression()));
        gbc.gridy = 1;
        this.detailsPanel.add(new FSMVisualizer(fsm), gbc);
    }

    private void addExpandedView() {
        GridBagConstraints gbc = createDefaultGBC();
        this.detailsPanel.add(createToggleButton(), gbc);
        addFSMDetails(gbc, fsmGroup.getOperationFSM(), "Operation FSM", 1);
        addFSMDetails(gbc, fsmGroup.getDeterministicFSM(), "Deterministic FSM", 4);
        addFSMDetails(gbc, fsmGroup.getMinimizedFSM(), "Minimized FSM", 7);
    }

    private JButton createToggleButton() {
        JButton toggleButton = new JButton(isCollapsed ? "Show Details" : "Hide Details");
        toggleButton.addActionListener(e -> {
            this.isCollapsed = !isCollapsed;
            this.toggleDetails();
        });
        return toggleButton;
    }

    @Override
    protected void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description, int startRow) {
        gbc.gridy = startRow;
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        splitPane.setLeftComponent(new FSMVisualizer(fsm));

        JTextArea definitionArea = setupTextArea(new JTextArea(fsm.toString()));
        splitPane.setRightComponent(new JScrollPane(definitionArea));

        JTextArea explanationArea = setupTextArea(new JTextArea("Explanation text here... \n\n\n"));

        gbc.gridy++;
        detailsPanel.add(splitPane, gbc);
        gbc.gridy++;
        detailsPanel.add(explanationArea, gbc);
        gbc.gridy++;
    }
}
