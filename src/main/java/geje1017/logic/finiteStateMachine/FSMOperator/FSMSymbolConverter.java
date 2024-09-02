package geje1017.logic.finiteStateMachine.Operation;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;
import geje1017.logic.postfix.InputManager;

import java.util.Collections;
import java.util.HashSet;

public class FSMSymbolConverter extends FSMOperation{

    /**
     * Converts the input symbol to an FSM.
     *
     * @param inputSymbol The input symbol to be converted.
     * @return The converted FSM.
     */
    public static FSMStructure convertInputCharacter(char inputSymbol) {
        FSMStructure convertedFSM = new FSMStructure();

        if (!InputManager.isOperand(inputSymbol)) {
            throw new IllegalArgumentException("Invalid input symbol: " + inputSymbol);
        }

        if (inputSymbol == InputManager.getEmptySymbol()) {
            convertEmptyString(convertedFSM);
        }
        else if (inputSymbol == InputManager.getEmptySet()) {
            convertEmptySet(convertedFSM);
        }
        else {
            convertRegularSymbol(convertedFSM, inputSymbol);
        }
        return convertedFSM;
    }

    /**
     * Handles the case when the input symbol is "e".
     *
     * @param fsm The FSM to be modified.
     */
    protected static void convertEmptyString(FSMStructure fsm) {
        State startEndState = new State(true, true);
        fsm.addTransition(startEndState, Collections.emptySet(), startEndState);
        fsm.setExpression(fsm.getExpression().isEmpty() ? String.valueOf(InputManager.getEmptySymbol()) : fsm.getExpression());
    }

    /**
     * Handles the case when the input symbol is "0/".
     *
     * @param fsm The FSM to be modified.
     */
    private static void convertEmptySet(FSMStructure fsm) {
        State startState = new State(true, false);
        fsm.addTransition(startState, Collections.emptySet(), null);
        fsm.setExpression(fsm.getExpression().isEmpty() ? String.valueOf(InputManager.getEmptySet()) : fsm.getExpression());
    }

    /**
     * Handles the case when the input symbol is a regular symbol.
     *
     * @param fsm The FSM to be modified.
     * @param inputSymbol The input symbol.
     */
    private static void convertRegularSymbol(FSMStructure fsm, char inputSymbol) {
        State startState = new State(true, false);
        State finalState = new State(false, true);
        fsm.addTransition(startState, new HashSet<>(Collections.singletonList(String.valueOf(inputSymbol))), finalState);
        fsm.setExpression(String.valueOf(inputSymbol));
    }

}
