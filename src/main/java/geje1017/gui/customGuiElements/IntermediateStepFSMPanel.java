package geje1017.gui.customGuiElements;

import geje1017.gui.FSMGroup;
import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import java.awt.*;

/**
 * IntermediateStepFSMPanel zeigt das Ergebnis des FSM-Prozesses mit einer umschaltbaren Detailansicht.
 * Es erbt gemeinsame Funktionalitäten von AbstractFSMPanel.
 */
public class IntermediateStepFSMPanel extends AbstractFSMPanel {

    private boolean isCollapsed = true;
    private int subStep = 1;
    private final int ownStep;

    public IntermediateStepFSMPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        ownStep = getStep();
        toggleDetails();
    }

    @Override
    protected void setupPanel() {
    }

    /**
     * Schaltet zwischen der minimierten und der erweiterten Ansicht um.
     */
    private void toggleDetails() {
        detailsPanel.removeAll();

        if (isCollapsed) {
            addMinimizedView();
        } else {
            addExpandedView();
        }

        revalidate();
        repaint();
    }

    /**
     * Fügt die minimierte Ansicht des FSM hinzu.
     */
    private void addMinimizedView() {
        GridBagConstraints gbc = createDefaultGBC();

        FSMStructure fsm = fsmGroup.getSimplifiedFSM();
        setBorder(createTitledBorder(ownStep + ") Current step: " +  fsm.getExpression()));
        detailsPanel.add(createToggleButton(), gbc);
        gbc.fill = GridBagConstraints.CENTER;

        detailsPanel.add(new FSMVisualizer(fsm), gbc);
    }

    /**
     * Fügt die erweiterte Ansicht des FSM hinzu.
     */
    private void addExpandedView() {
        GridBagConstraints gbc = createDefaultGBC();
        subStep = 1;

        detailsPanel.add(createToggleButton(), gbc);
        gbc.gridy++;

        addFSMDetails(gbc, fsmGroup.getOperationFSM(), "Operation FSM");
        addFSMDetails(gbc, fsmGroup.getDeterministicFSM(), "Deterministic FSM");
        addFSMDetails(gbc, fsmGroup.getMinimizedFSM(), "Minimized FSM");
        addFSMDetails(gbc, fsmGroup.getSimplifiedFSM(), "Simplified FSM");
    }

    /**
     * Erstellt einen Button, um zwischen minimierter und erweiterter Ansicht umzuschalten.
     * @return JButton, der die Ansicht umschaltet.
     */
    private JButton createToggleButton() {
        JButton toggleButton = new JButton(isCollapsed ? "Show Details" : "Hide Details");
        toggleButton.addActionListener(e -> {
            isCollapsed = !isCollapsed;
            toggleDetails();
        });
        return toggleButton;
    }

    /**
     * Fügt detaillierte FSM-Informationen zur erweiterten Ansicht hinzu.
     * @param gbc GridBagConstraints für das Layout.
     * @param fsm FSMStructure mit den anzuzeigenden Daten.
     * @param description Beschreibung des FSM-Typs (z.B. "Operation FSM").
     */
    protected void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description) {
        gbc.gridy++;

        JTextArea step = setupTextArea(new JTextArea(subStep++ + ") " + description));
        detailsPanel.add(step, gbc);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(new FSMVisualizer(fsm));

        JTextArea definitionArea = setupTextArea(new JTextArea());
        definitionArea.append(fsm.toString() + "\n\n");
        definitionArea.append("--------------------------------------------------\n\n");
        definitionArea.append(fsm.getExplanation());

        splitPane.setRightComponent(new JScrollPane(definitionArea));

        gbc.gridy++;
        detailsPanel.add(splitPane, gbc);

        JTextArea explanationArea = setupTextArea(new JTextArea("\n"));
        gbc.gridy++;
        detailsPanel.add(explanationArea, gbc);
    }
}
