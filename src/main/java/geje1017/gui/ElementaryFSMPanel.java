package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

/**
 * ElementaryFSMPanel is responsible for displaying a list of elementary FSMs in a grid layout.
 */
public class ElementaryFSMPanel extends JPanel {

    /**
     * Constructor to initialize and set up the grid of FSM panels.
     * @param fsmList List of FSMStructure objects to display.
     */
    public ElementaryFSMPanel(List<FSMStructure> fsmList) {
        setLayout(new GridLayout(0, 2, 0, 0));

        Border outerInnerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(40, 0, 0, 0),
                BorderFactory.createEtchedBorder()
        );
        this.setBorder(BorderFactory.createTitledBorder(outerInnerBorder, "Elementary FSM:"));

        for (FSMStructure fsm : fsmList) {
            add(createFSMVisualizerPanel(fsm));
        }
    }

    /**
     * Creates a panel for visualizing an individual FSM.
     * @param fsm FSMStructure to visualize.
     * @return JPanel containing FSM visualization.
     */
    private JPanel createFSMVisualizerPanel(FSMStructure fsm) {
        JPanel panel = new FSMVisualizer(fsm);
        panel.setBorder(BorderFactory.createTitledBorder(fsm.getExpression()));
        return panel;
    }
}
