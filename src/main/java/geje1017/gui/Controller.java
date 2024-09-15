package geje1017.gui;

import geje1017.gui.customGuiElements.FinalResultFSMPanel;
import geje1017.gui.customGuiElements.IntermediateStepFSMPanel;
import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.postfix.ExpressionEvaluator;
import geje1017.logic.postfix.ExpressionValidator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Handles user input events (both action and key events) and updates the GUI accordingly.
 * It processes user-provided regular expressions, validates them, converts them to FSMs,
 * and displays the results in the associated GUI components.
 */
public class Controller implements ActionListener, KeyListener {

    private final Frame frame;

    /**
     * Constructs a {@code Controller} with a reference to the associated {@code Frame}.
     *
     * @param frame The frame that this controller manages.
     */
    public Controller(Frame frame) {
        this.frame = frame;
    }

    /**
     * Handles action events, typically triggered by button clicks.
     * When an action is performed, the event is processed by validating the input and updating the GUI.
     *
     * @param e The {@code ActionEvent} that occurred.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent();
    }

    /**
     * Handles key press events. If the Enter key is pressed, the event is processed.
     *
     * @param e The {@code KeyEvent} that occurred.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            processEvent();
        }
    }

    /**
     * Processes the current input event by validating the expression, cleaning the result panels,
     * evaluating the input expression, and updating the GUI.
     */
    private void processEvent() {
        try {
            ExpressionValidator.validateInfix(frame.getInputFieldText());
            this.frame.setupScrollPane();
            cleanResult();
            processInput();
            updateGui();
        } catch (ExpressionValidator.InvalidExpressionException e) {
            frame.setErrorLabel(e.getMessage());
        }
    }

    /**
     * Cleans the result panels by removing all components and resetting them.
     */
    private void cleanResult() {
        cleanPanel(frame.solutionPanel);
        cleanPanel(frame.resultPanel);
    }

    /**
     * Removes all components from the specified panel and repaints it.
     *
     * @param panel The panel to clean.
     */
    private void cleanPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Processes the input regular expression by converting it to FSMs and displaying the results.
     */
    private void processInput() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String inputText = frame.getInputFieldText();
        FSMStorage fsmStorage = evaluator.evaluateExpression(inputText);

        displayResultFSM(fsmStorage.getResultFsm());
        displayElementaryFSMs(fsmStorage.getElementaryFSMs());
        displayGroupedFSMs(fsmStorage.getFSMGroups());
    }

    /**
     * Displays the resulting FSM in the result panel.
     *
     * @param fsm The FSM structure to display.
     */
    public void displayResultFSM(FSMStructure fsm) {
        FinalResultFSMPanel resultPanel = new FinalResultFSMPanel(new FSMGroup(fsm, fsm, fsm, fsm)); // Simplified for elementary FSMs
        frame.resultPanel.add(resultPanel);
    }

    /**
     * Displays a list of elementary FSMs in the solution panel.
     *
     * @param currFsm A list of FSM structures representing the elementary FSMs.
     */
    private void displayElementaryFSMs(List<FSMStructure> currFsm) {
        /*ElementaryFSMPanel resultPanel = new ElementaryFSMPanel(currFsm);
        frame.solutionPanel.add(resultPanel);*/
    }

    /**
     * Displays a list of grouped FSMs in the solution panel.
     *
     * @param groups A list of FSM groups to display.
     */
    private void displayGroupedFSMs(List<FSMGroup> groups) {
        for (FSMGroup group : groups) {
            addGroupResult(group);
        }
    }

    /**
     * Adds the result of an FSM to the solution panel.
     *
     * @param fsm The FSM structure to display.
     */
    private void addResult(FSMStructure fsm) {
        IntermediateStepFSMPanel resultPanel = new IntermediateStepFSMPanel(new FSMGroup(fsm, fsm, fsm, fsm)); // Simplified for elementary FSMs
        frame.solutionPanel.add(resultPanel);
    }

    /**
     * Adds a grouped FSM result to the solution panel.
     *
     * @param group The FSM group to display.
     */
    private void addGroupResult(FSMGroup group) {
        IntermediateStepFSMPanel resultPanel = new IntermediateStepFSMPanel(group);
        frame.solutionPanel.add(resultPanel);
    }

    /**
     * Updates the GUI by revalidating and repainting the solution and result panels.
     */
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