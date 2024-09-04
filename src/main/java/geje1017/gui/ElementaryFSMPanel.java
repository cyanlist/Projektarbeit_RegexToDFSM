package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class ElementaryFSMPanel extends JPanel {

    public ElementaryFSMPanel(List<FSMStructure> fsmList) {
        // Setzen eines Grid-Layouts, das sich dynamisch an die Anzahl der FSMs anpasst
        int rows = (int) Math.ceil(Math.sqrt(fsmList.size()));
        setLayout(new GridLayout(rows, 0, 10, 10)); // Grid mit dynamischer Anzahl von Reihen, Spalten basierend auf der Liste, Abstände von 10 Pixel

        Border outerInnerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(40, 0, 0, 0),
                BorderFactory.createEtchedBorder()
        );
        this.setBorder(BorderFactory.createTitledBorder(outerInnerBorder, "Elementary FSM:"));

        // Erstellen und Hinzufügen der Panels für jede FSM
        for (FSMStructure fsm : fsmList) {
            add(createFSMVisualizerPanel(fsm));
        }
    }

    private JPanel createFSMVisualizerPanel(FSMStructure fsm) {
        // Ein einzelnes Panel, das den FSMVisualizer enthält
        JPanel panel = new FSMVisualizer(fsm);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        panel.setBorder( BorderFactory.createTitledBorder(emptyBorder, fsm.getExpression()));
        return panel;
    }
}
