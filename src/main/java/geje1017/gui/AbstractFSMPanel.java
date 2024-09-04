package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public abstract class AbstractFSMPanel extends JPanel {

    protected FSMGroup fsmGroup;
    protected JPanel detailsPanel;

    protected static int stepCounter;

    public AbstractFSMPanel(FSMGroup fsmGroup) {
        this.fsmGroup = fsmGroup;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.detailsPanel = new JPanel(new GridBagLayout());
        this.add(detailsPanel);
    }

    protected GridBagConstraints createDefaultGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        return gbc;
    }

    protected JTextArea setupTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBorder(null);
        return textArea;
    }

    protected Border createTitledBorder(FSMStructure fsm, String title) {
        Border outerInnerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(40, 0, 0, 0),
                BorderFactory.createEtchedBorder()
        );
        TitledBorder titledBorder = BorderFactory.createTitledBorder(outerInnerBorder, title);

        // Setze eine benutzerdefinierte Schriftart für den Titel
        Font titleFont = new Font("Arial", Font.BOLD, 14); // Ändere 'Arial' zu deiner gewünschten Schriftart und 16 zu deiner gewünschten Größe
        titledBorder.setTitleFont(titleFont);

        return titledBorder;
    }

    protected abstract void addFSMDetails(GridBagConstraints gbc, FSMStructure fsm, String description, int startRow);

    protected void resetStepCounter() {
        AbstractFSMPanel.stepCounter = 1;
    }
}
