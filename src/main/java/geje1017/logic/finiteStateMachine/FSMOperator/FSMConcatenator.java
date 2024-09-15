package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;
import geje1017.logic.postfix.InputManager;

import java.util.*;

import static geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier.copyFsm;

/**
 * Concatenates two deterministic finite state machines (FSMs).
 * Concatenation is defined as connecting the final states of the first FSM to the start state of the second FSM,
 * creating a new FSM that processes the sequence of the first FSM followed by the sequence of the second FSM.
 */
public class FSMConcatenator {

    /**
     * Concatenates two finite state machines (FSMs) by linking the final states of the first FSM
     * directly to the start state of the second FSM.
     *
     * @param fsm1 The first FSM to concatenate.
     * @param fsm2 The second FSM to concatenate.
     * @return The new FSM resulting from the concatenation of fsm1 and fsm2.
     */
    public static FSMStructure concatenate(FSMStructure fsm1, FSMStructure fsm2) {

        FSMStructure copyFsm1 = copyFsm(fsm1);
        FSMStructure copyFsm2 = copyFsm(fsm2);

        FSMStructure specialCaseResult = checkSpecialCases(fsm1, fsm2);
        if (specialCaseResult != null) {
            return specialCaseResult;
        }

        return performConcatenation(copyFsm1, copyFsm2);
    }

    /**
     * Checks for special concatenation cases such as empty set or empty string,
     * which can simplify the concatenation process. For example, if one FSM represents an empty set,
     * it directly affects the result.
     *
     * @param fsm1 The first FSM.
     * @param fsm2 The second FSM.
     * @return A simplified FSM if a special case applies; otherwise, null.
     */
    private static FSMStructure checkSpecialCases(FSMStructure fsm1, FSMStructure fsm2) {
        if (fsm1.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
            return copyFsm(fsm1);
        }
        if (fsm2.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
            return copyFsm(fsm2);
        }
        if (fsm2.getExpression().equals(String.valueOf(InputManager.getEmptySymbol()))) {
            return copyFsm(fsm1);
        }
        if (fsm1.getExpression().equals(String.valueOf(InputManager.getEmptySymbol()))) {
            return copyFsm(fsm2);
        }
        return null;
    }

    /**
     * Performs the actual concatenation of two FSMs, linking the final states of the first FSM
     * with the start state of the second FSM. It also merges transitions between the two FSMs.
     *
     * @param fsm1 The first FSM.
     * @param fsm2 The second FSM.
     * @return The concatenated FSM.
     */
    private static FSMStructure performConcatenation(FSMStructure fsm1, FSMStructure fsm2) {

        FSMStructure concatenatedFsm = new FSMStructure();
        concatenatedFsm.setExpression(fsm1.getExpression() + fsm2.getExpression());
        concatenatedFsm.setExplanation("Merging two DFSM by connecting the final states of the first to the start state of the second.\n");

        fsm1 = FSMCopier.copyFsm(fsm1);
        fsm2 = FSMCopier.copyFsm(fsm2);

        State fsm2StartState = fsm2.getStartState();
        Set<State> fsm1FinalStates = fsm1.getFinalStates();

        concatenatedFsm.addAllTransitions(fsm1.getTransitions());

        // Add transitions for FSM2, connecting start states and final states
        fsm2.getTransitions().forEach((sourceState, transitions) -> transitions.forEach((targetState, symbols) -> {

            if (sourceState.isStartState() || targetState.isStartState()) {
                for (State currentFinalState : fsm1FinalStates) {
                    State newSource = sourceState.isStartState() ? currentFinalState : sourceState;
                    State newTarget = targetState.isStartState() ? currentFinalState : targetState;
                    concatenatedFsm.addTransition(newSource, symbols, newTarget);
                }
            }
            else {
                concatenatedFsm.addTransition(sourceState, symbols, targetState);
            }
        }));

        // Handle case where the start state of fsm2 is not a final state
        if (!fsm2StartState.isFinalState) {
            for (State currentState : fsm1FinalStates) {
                currentState.fuseStateName(fsm2StartState);
                currentState.resetFinalState();
            }
        }

        fsm2StartState.resetStartState();
        return concatenatedFsm;
    }

}
