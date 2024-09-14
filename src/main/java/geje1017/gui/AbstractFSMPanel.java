package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public abstract class AbstractFSMPanel extends JPanel {

    protected FSMGroup fsmGroup;
    protected JPanel detailsPanel;

    protected static int step = 0;

    public AbstractFSMPanel(FSMGroup fsmGroup) {
        this.fsmGroup = fsmGroup;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.detailsPanel = new JPanel(new GridBagLayout());
        this.add(detailsPanel);
        setupPanel();
    }

    // Methode, die von Unterklassen implementiert werden muss
    protected abstract void setupPanel();

    // Hilfsmethode zum Erstellen von GridBagConstraints
    protected GridBagConstraints createDefaultGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        return gbc;
    }

    // Hilfsmethode zum Setzen des Panel-Borders
    protected Border createTitledBorder(FSMStructure fsm, String title) {
        Border outerInnerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(40, 0, 0, 0),
                BorderFactory.createEtchedBorder()
        );
        TitledBorder titledBorder = BorderFactory.createTitledBorder(outerInnerBorder, title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        return titledBorder;
    }

    // Hilfsmethode für TextArea Einstellungen
    protected JTextArea setupTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBorder(null);
        return textArea;
    }

    protected void resetStep() {
        step = 1;
    }

    protected int getStep() {
        step++;
        return step;
    }
}
