package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import java.awt.*;

public class MinimizedFSMPanel extends AbstractFSMPanel {

    public MinimizedFSMPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        this.addExpandedView();
        resetStepCounter();
    }

    private void addExpandedView() {
        GridBagConstraints gbc = createDefaultGBC();
        this.setBorder(createTitledBorder(fsmGroup.getMinimizedFSM(), "Your final result is: " + fsmGroup.getMinimizedFSM().getExpression()));
        addFSMDetails(gbc, fsmGroup.getMinimizedFSM(), "Minimized FSM", 0);
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

        gbc.gridy++;
        detailsPanel.add(splitPane, gbc);
    }
}
