package geje1017.gui.customGuiElements;

import geje1017.logic.finiteStateMachine.FSMStructure;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ElementaryFSMPanel extends AbstractFSMPanel {

    private List<FSMStructure> fsmList;

    public ElementaryFSMPanel(List<FSMStructure> fsmList) {
        super(null);  // keine FSMGroup, nur einzelne FSMs
        this.fsmList = fsmList;
    }

    @Override
    protected void setupPanel() {
        setLayout(new GridLayout(0, 2, 0, 0));
        setBorder(BorderFactory.createTitledBorder("Elementary FSMs"));

        for (FSMStructure fsm : fsmList) {
            add(createFSMVisualizerPanel(fsm));
        }
    }

    private JPanel createFSMVisualizerPanel(FSMStructure fsm) {
        JPanel panel = new FSMVisualizer(fsm);
        panel.setBorder(BorderFactory.createTitledBorder(fsm.getExpression()));
        return panel;
    }
}
