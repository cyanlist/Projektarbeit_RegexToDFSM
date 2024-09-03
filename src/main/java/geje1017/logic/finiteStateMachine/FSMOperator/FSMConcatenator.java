package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;
import geje1017.logic.postfix.InputManager;

import java.util.*;

import static geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier.copyFsm;

/**
 * Provides functionality to concatenate two deterministic finite state machines (FSMs).
 * Concatenation is defined as connecting the final states of the first FSM to the start state of the second FSM,
 * effectively making the sequence of the first FSM followed by the sequence of the second FSM.
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
     * which can significantly simplify the concatenation operation.
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
     * with the start state of the second FSM.
     *
     * @param fsm1 The first FSM.
     * @param fsm2 The second FSM.
     * @return The concatenated FSM.
     */
    private static FSMStructure performConcatenation(FSMStructure fsm1, FSMStructure fsm2) {
        FSMStructure concatenatedFsm = new FSMStructure();
        concatenatedFsm.setExpression(fsm1.getExpression() + fsm2.getExpression());

        State fsm2StartState = fsm2.getStartState();
        Set<State> fsm1FinalStates = fsm1.getFinalStates();

        concatenatedFsm.addAllTransitions(fsm1.getTransitions());

        fsm2.getTransitions().forEach((sourceState, transitions) -> {
            transitions.forEach((targetState, symbols) -> {

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
            });
        });

        if (!fsm2StartState.isFinalState) {
            for (State currentState : fsm1FinalStates) {
                currentState.resetFinalState();
            }
        }

        fsm2StartState.resetStartState();
        return concatenatedFsm;
    }

    private static FSMStructure performConcatenation2(FSMStructure fsm1, FSMStructure fsm2) {

        FSMStructure concatenatedFsm = new FSMStructure();
        concatenatedFsm.setExpression(fsm1.getExpression() + fsm2.getExpression());

        Set<State> finalStatesFsm1 = new HashSet<>(fsm1.getFinalStates());
        State initialStateFsm2 = fsm2.getStartState();

        // Iterate over each final state of fsm1
        Map<State, State> fusedStatesMap = new HashMap<>();
        for (State finalStateFsm1 : finalStatesFsm1) {
            // Fuse each final state of fsm1 with the start state of fsm2 individually
            Set<State> fuseStateSet = new HashSet<>();
            fuseStateSet.add(finalStateFsm1);
            fuseStateSet.add(initialStateFsm2);
            State fusedState = State.fuseState(fuseStateSet);
            if (!initialStateFsm2.isFinalState()) fusedState.resetFinalState();
            if (!finalStateFsm1.isStartState()) fusedState.resetStartState();
            // Store the fused state to use in transition copying
            fusedStatesMap.put(finalStateFsm1, fusedState);
        }

        // Copy transitions from fsm1 to the concatenated FSM
        fsm1.getTransitions().forEach((sourceState, transitions) -> {
            transitions.forEach((targetState, symbols) -> {
                State newSource = fusedStatesMap.getOrDefault(sourceState, sourceState);
                State newTarget = fusedStatesMap.getOrDefault(targetState, targetState);
                concatenatedFsm.addTransition(newSource, symbols, newTarget);
            });
        });

        // Copy transitions from fsm2 to the concatenated FSM
        fsm2.getTransitions().forEach((sourceState, transitions) -> {
            transitions.forEach((targetState, symbols) -> {
                // Handle transitions involving the start state of fsm2
                if (sourceState.equals(initialStateFsm2)) {
                    // For each fused state, add transitions to targetState
                    fusedStatesMap.values().forEach(fusedState -> {
                        concatenatedFsm.addTransition(fusedState, symbols, targetState);
                    });
                } else if (targetState.equals(initialStateFsm2)) {
                    // Add transitions from sourceState to each fused state
                    fusedStatesMap.forEach((finalStateFsm1, fusedState) -> {
                        concatenatedFsm.addTransition(sourceState, symbols, fusedState);
                    });
                } else {
                    // Standard transition copy for other states
                    concatenatedFsm.addTransition(sourceState, symbols, targetState);
                }
            });
        });

        initialStateFsm2.resetStartState();

        return concatenatedFsm;
    }

}
