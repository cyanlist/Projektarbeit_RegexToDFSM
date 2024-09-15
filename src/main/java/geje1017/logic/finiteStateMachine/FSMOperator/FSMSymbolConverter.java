package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;
import geje1017.logic.postfix.InputManager;

import java.util.Collections;
import java.util.HashSet;

/**
 * Converts input symbols into corresponding finite state machines (FSM).
 * It supports conversion for regular input symbols, the empty string, and the empty set.
 */
public class FSMSymbolConverter{

        /**
         * Converts an input symbol to a finite state machine (FSM).
         * If the input symbol is a valid operand, it creates an FSM that represents the symbol.
         *
         * @param inputSymbol The input symbol to be converted.
         * @return A new FSM representing the input symbol.
         * @throws IllegalArgumentException If the input symbol is not a valid operand.
         */
        public static FSMStructure convertInputCharacter(char inputSymbol) {
            FSMStructure convertedFSM = new FSMStructure();

            // Validate the input symbol using InputManager
            if (!InputManager.isOperand(inputSymbol)) {
                throw new IllegalArgumentException("Invalid input symbol: " + inputSymbol);
            }
            // Handle special cases for empty string and empty set
            if (inputSymbol == InputManager.getEmptySymbol()) {
                convertEmptyString(convertedFSM);
            }
            else if (inputSymbol == InputManager.getEmptySet()) {
                convertEmptySet(convertedFSM);
            }
            // Handle regular input symbols
            else {
                convertRegularSymbol(convertedFSM, inputSymbol);
            }
            return convertedFSM;
        }

    /**
     * Converts the FSM to handle the empty string (epsilon).
     * In this case, the FSM consists of a single state that is both a start state and a final state.
     *
     * @param fsm The FSM to be modified.
     */
    protected static void convertEmptyString(FSMStructure fsm) {
        State startEndState = new State(true, true);
        fsm.addTransition(startEndState, null, startEndState);
        fsm.setExpression(fsm.getExpression().isEmpty() ? String.valueOf(InputManager.getEmptySymbol()) : fsm.getExpression());
    }

    /**
     * Converts the FSM to handle the empty set.
     * The FSM will consist of a start state with no transitions to any other states.
     *
     * @param fsm The FSM to be modified.
     */
    private static void convertEmptySet(FSMStructure fsm) {
        State startState = new State(true, false);
        fsm.addTransition(startState, Collections.emptySet(), null);
        fsm.setExpression(fsm.getExpression().isEmpty() ? String.valueOf(InputManager.getEmptySet()) : fsm.getExpression());
    }

    /**
     * Converts the FSM to handle a regular input symbol.
     * The FSM will have a start state and a final state, with a transition for the given input symbol.
     *
     * @param fsm The FSM to be modified.
     * @param inputSymbol The input symbol that the FSM should recognize.
     */
    private static void convertRegularSymbol(FSMStructure fsm, char inputSymbol) {
        State startState = new State(true, false);
        State finalState = new State(false, true);
        fsm.addTransition(startState, new HashSet<>(Collections.singletonList(String.valueOf(inputSymbol))), finalState);
        fsm.setExpression(String.valueOf(inputSymbol));
    }

}
