package geje1017.gui.customGuiElements;

import geje1017.gui.FSMGroup;
import geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier;
import geje1017.logic.finiteStateMachine.FSMStructure;
import javax.swing.*;
import java.awt.*;

/**
 * Represents a panel that displays the final minimized finite state machine (FSM)
 * after all transformations (determinization, minimization, and simplification) have been applied.
 * It extends {@code AbstractFSMPanel} and includes a visual representation and detailed description of the FSM.
 */
public class FinalResultFSMPanel extends AbstractFSMPanel {

    /**
     * Constructs a new {@code FinalResultFSMPanel} to display the minimized FSM.
     *
     * @param fsmGroup The group of FSMs generated from the regular expression processing.
     */
    public FinalResultFSMPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        resetStep();
        toggleDetails();
    }

    @Override
    protected void addMinimizedView() {
        GridBagConstraints gbc = createDefaultGBC();
        gbc.fill = GridBagConstraints.CENTER;

        FSMStructure result = fsmGroup.getSimplifiedFSM();
        this.setBorder(createTitledBorder("Your final result is: " + result.getExpression()));
        addFSMDetails(gbc, result);
    }

    @Override
    protected void addExpandedView() {

    }

    /**
     * Adds the details of the FSM to the panel, including a visual representation and a textual description.
     *
     * @param gbc         The {@link GridBagConstraints} object used for layout.
     * @param fsm         The FSM structure to display.
     */
    private void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(new FSMVisualizer(FSMCopier.copyFsm(fsm)));

        JTextArea definitionArea = setupTextArea(fsm.toString());
        splitPane.setRightComponent(definitionArea);

        splitPane.revalidate();
        splitPane.repaint();
        detailsPanel.add(splitPane, gbc);
    }
}
