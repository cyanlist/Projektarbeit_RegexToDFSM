package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import java.awt.*;

/**
 * FSMResultPanel zeigt das Ergebnis des FSM-Prozesses mit einer umschaltbaren Detailansicht.
 * Es erbt gemeinsame Funktionalitäten von AbstractFSMPanel.
 */
public class FSMResultPanel extends AbstractFSMPanel {

    private boolean isCollapsed = true;
    private int subStep = 1;
    private int ownStep;

    public FSMResultPanel(FSMGroup fsmGroup) {
        super(fsmGroup);
        ownStep = getStep();
        toggleDetails();
    }

    @Override
    protected void setupPanel() {
        // Diese Methode kann leer bleiben, da wir die Ansicht in toggleDetails() initialisieren
    }

    /**
     * Schaltet zwischen der minimierten und der erweiterten Ansicht um.
     */
    private void toggleDetails() {
        detailsPanel.removeAll();

        if (isCollapsed) {
            addMinimizedView(); // Zeigt nur grundlegende Informationen an
        } else {
            addExpandedView();  // Zeigt detaillierte FSM-Informationen an
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
        setBorder(createTitledBorder(fsm, ownStep + ") " +  fsm.getExpression()));
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

        // Umschalt-Button oben hinzufügen
        detailsPanel.add(createToggleButton(), gbc);
        gbc.gridy++;

        // FSMs (Operation, Deterministisch, Minimiert) mit Details hinzufügen
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

        // Beschreibung der FSM (Schrittzahl und Typ)
        JTextArea step = setupTextArea(new JTextArea(subStep++ + ") " + description));
        detailsPanel.add(step, gbc);

        // Visualisierung und Erklärung der FSM
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        splitPane.setLeftComponent(new FSMVisualizer(fsm));

        // FSM-Details (Textbereich für die Definition und Erklärung)
        JTextArea definitionArea = setupTextArea(new JTextArea(fsm.toString()));
        splitPane.setRightComponent(new JScrollPane(definitionArea));

        gbc.gridy++;
        detailsPanel.add(splitPane, gbc);

        // Optional: Erklärung zu dem FSM hinzufügen (falls verfügbar)
        JTextArea explanationArea = setupTextArea(new JTextArea(fsm.getExplanation()));
        gbc.gridy++;
        detailsPanel.add(explanationArea, gbc);
    }
}
