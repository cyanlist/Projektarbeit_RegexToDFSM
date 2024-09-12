package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import java.awt.*;

/**
 * MinimizedFSMPanel displays the minimized FSM and its details.
 */
public class MinimizedFSMPanel extends AbstractFSMPanel {

    /**
     * Constructor to initialize the panel with FSMGroup.
     * @param fsmGroup FSMGroup to display minimized FSM results.
     */
    public MinimizedFSMPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        this.addExpandedView();
        resetStepCounter();
    }

    /**
     * Adds an expanded view of the minimized FSM.
     */
    private void addExpandedView() {
        GridBagConstraints gbc = createDefaultGBC();
        this.setBorder(createTitledBorder(fsmGroup.getMinimizedFSM(), "Your final result is: " + fsmGroup.getMinimizedFSM().getExpression()));
        addFSMDetails(gbc, fsmGroup.getMinimizedFSM(), "Minimized FSM");
    }

    @Override
    protected void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description) {
        gbc.gridy++;
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
