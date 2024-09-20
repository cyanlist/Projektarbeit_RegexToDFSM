package geje1017.gui.customGuiElements;

import geje1017.logic.finiteStateMachine.FSMStructure;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ElementaryFSMPanel extends AbstractFSMPanel {

    private List<FSMStructure> fsmList;
    private final int ownStep;

    public ElementaryFSMPanel(List<FSMStructure> fsmList) {
        super(null);
        ownStep = getStep();
        this.fsmList = fsmList;
        toggleDetails();
    }

    @Override
    protected void addMinimizedView() {
        GridBagConstraints gbc = createDefaultGBC();
        gbc.gridy=0;

        detailsPanel.setBorder(createTitledBorder(ownStep + " : Convert all input alphabet like"));
        detailsPanel.add(createToggleButton(), gbc);
        gbc.fill = GridBagConstraints.CENTER;

        gbc.gridy++;
        addFSMDetails(gbc, fsmList.get(0));
    }

    @Override
    protected void addExpandedView() {

        GridBagConstraints gbc = createDefaultGBC();
        gbc.fill = GridBagConstraints.CENTER;

        gbc.gridy=0;
        gbc.gridx=0;

        detailsPanel.add(createToggleButton(), gbc);
        gbc.gridy++;

        for (FSMStructure fsm : fsmList) {
            addFSMDetails(gbc, fsm);
            gbc.gridy++;

        }

    }

    private void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm) {
        detailsPanel.add(new FSMVisualizer(fsm), gbc);
    }
}
