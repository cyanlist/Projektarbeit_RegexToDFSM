package geje1017.gui.customGuiElements;

import geje1017.gui.FSMGroup;
import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the results of the FSM process with a toggleable detailed view.
 * It inherits common functionalities from AbstractFSMPanel.
 */
public class IntermediateStepFSMPanel extends AbstractFSMPanel {

    private int subStep = 1;
    private final int ownStep;

    /**
     * Constructs an IntermediateStepFSMPanel for displaying the FSM process at a specific step.
     * It initializes the FSM group and toggles the details based on the current step.
     *
     * @param fsmGroup The FSMGroup representing the group of finite state machines to display.
     */
    public IntermediateStepFSMPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        ownStep = getStep();

        toggleDetails();
    }

    @Override
    protected void addMinimizedView() {
        GridBagConstraints gbc = createDefaultGBC();
        FSMStructure fsm = fsmGroup.getSimplifiedFSM();
        setBorder(createTitledBorder("Step " + ownStep + ": " +  fsm.getExpression()));

        detailsPanel.add(createToggleButton(), gbc);
        gbc.fill = GridBagConstraints.CENTER;

        detailsPanel.add(new FSMVisualizer(fsm), gbc);
    }

    @Override
    protected void addExpandedView() {
        subStep = 1;

        GridBagConstraints gbc = createDefaultGBC();
        gbc.anchor = GridBagConstraints.CENTER;

        detailsPanel.add(createToggleButton(), gbc);
        gbc.gridy++;

        addFSMDetails(gbc, fsmGroup.getOperationFSM(), "Apply operation");
        if (!fsmGroup.getOperationFSM().equals(fsmGroup.getMinimizedFSM()))
            addFSMDetails(gbc, fsmGroup.getDeterministicFSM(), "Convert to deterministic FSM");
        if (!fsmGroup.getMinimizedFSM().equals(fsmGroup.getDeterministicFSM()))
            addFSMDetails(gbc, fsmGroup.getMinimizedFSM(), "Minimize FSM");
        if (!fsmGroup.getDeterministicFSM().equals(fsmGroup.getSimplifiedFSM()))
            addFSMDetails(gbc, fsmGroup.getSimplifiedFSM(), "Simplify state names");
    }

    /**
     * Fügt detaillierte FSM-Informationen zur erweiterten Ansicht hinzu.
     * @param gbc GridBagConstraints für das Layout.
     * @param fsm FSMStructure mit den anzuzeigenden Daten.
     * @param description Beschreibung des FSM-Typs (z.B. "Operation FSM").
     */
    private void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description) {
        gbc.gridy++;

        JTextArea step = setupTextArea(subStep++ + ") " + description);
        detailsPanel.add(step, gbc);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setEnabled(true);

        splitPane.setLeftComponent(new FSMVisualizer(fsm));

        String definitionText = fsm.toString() +
                "\n" +
                " \n" +
                fsm.getExplanation();
        JTextArea definitionArea = setupTextArea(definitionText);

        splitPane.setRightComponent((definitionArea));

        gbc.gridy++;
        detailsPanel.add(splitPane, gbc);

        gbc.gridy++;
        detailsPanel.add(new JSeparator(), gbc);
    }
}
