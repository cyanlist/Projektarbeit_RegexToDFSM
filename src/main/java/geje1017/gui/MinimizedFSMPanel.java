package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier;
import geje1017.logic.finiteStateMachine.FSMStructure;
import javax.swing.*;
import java.awt.*;

public class MinimizedFSMPanel extends AbstractFSMPanel {

    public MinimizedFSMPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        resetStep();
    }

    @Override
    protected void setupPanel() {
        GridBagConstraints gbc = createDefaultGBC();

        FSMStructure result = fsmGroup.getSimplifiedFSM();

        this.setBorder(createTitledBorder(result, "Final result: " + result.getExpression()));
        addFSMDetails(gbc, result, "");
    }

    // Fügt die Details des FSM hinzu
    private void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        splitPane.setLeftComponent(new FSMVisualizer(FSMCopier.copyFsm(fsm)));

        JTextArea definitionArea = setupTextArea(new JTextArea(fsm.toString()));
        splitPane.setRightComponent(new JScrollPane(definitionArea));

        detailsPanel.add(splitPane, gbc);
    }
}
