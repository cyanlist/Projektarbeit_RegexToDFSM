package regToDEA.main.gui;

import regToDEA.main.logic.finiteStateMachine.FSMStructure;
import regToDEA.main.logic.postfix.ExpressionEvaluator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class Controller implements ActionListener, KeyListener {

    private final Frame frame;

    public Controller(Frame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            processEvent();
        }
    }

    private void processEvent() {
        this.frame.setupScrollPane();
        cleanResult();
        processInput();
        updateGui();
    }

    private void cleanResult() {
        cleanPanel(frame.solutionPanel);
        cleanPanel(frame.resultPanel);
    }

    private void cleanPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    private void processInput() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String inputText = frame.getInputFieldText();
        FSMStorage fsmStorage = evaluator.evaluateExpression(inputText);

        displayResultFSM(fsmStorage.getResultFsm());
        displayElementaryFSMs(fsmStorage.getElementaryFSMs());
        displayGroupedFSMs(fsmStorage.getFSMGroups());
    }

    public void displayResultFSM(FSMStructure fsm) {
        FSMResultPanel resultPanel = new FSMResultPanel(new FSMGroup(fsm, fsm, fsm)); // Simplified for elementary FSMs
        frame.resultPanel.add(resultPanel);
    }

    private void displayElementaryFSMs(List<FSMStructure> currFsm) {
        for (FSMStructure fsm : currFsm) {
            addResult(fsm);
        }
    }

    private void displayGroupedFSMs(List<FSMGroup> groups) {
        for (FSMGroup group : groups) {
            addGroupResult(group);
        }
    }

    private void addResult(FSMStructure fsm) {
        FSMResultPanel resultPanel = new FSMResultPanel(new FSMGroup(fsm, fsm, fsm)); // Simplified for elementary FSMs
        frame.solutionPanel.add(resultPanel);
    }

    private void addGroupResult(FSMGroup group) {
        FSMResultPanel resultPanel = new FSMResultPanel(group);
        frame.solutionPanel.add(resultPanel);
    }

    private void updateGui() {
        frame.solutionPanel.revalidate();
        frame.solutionPanel.repaint();
        frame.resultPanel.revalidate();
        frame.resultPanel.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}