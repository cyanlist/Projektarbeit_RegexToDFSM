package geje1017.logic.finiteStateMachine.Operation;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;
import geje1017.logic.postfix.InputManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static geje1017.logic.finiteStateMachine.Operation.FSMCopier.copyFsm;

public class FSMConcatenator extends FSMOperation {

    public static FSMStructure concatenate(FSMStructure fsm1, FSMStructure fsm2) {

        FSMStructure copyFsm1 = copyFsm(fsm1);
        FSMStructure copyFsm2 = copyFsm(fsm2);

        if (fsm1.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
            return copyFsm1;
        }

        if (fsm2.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
            return copyFsm2;
        }

        if (fsm2.getExpression().equals(String.valueOf(InputManager.getEmptySymbol()))) {
            return copyFsm1;
        }

        if (fsm1.getExpression().equals(String.valueOf(InputManager.getEmptySymbol()))) {
            return copyFsm2;
        }

        FSMStructure concatenatedFsm = new FSMStructure();

        Set<State> finalStatesFsm1 = new HashSet<>(copyFsm1.getFinalStates());
        State initialStateFsm2 = copyFsm2.getStartState();

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
        copyFsm1.getTransitions().forEach((sourceState, transitions) -> {
            transitions.forEach((targetState, symbols) -> {
                State newSource = fusedStatesMap.getOrDefault(sourceState, sourceState);
                State newTarget = fusedStatesMap.getOrDefault(targetState, targetState);
                concatenatedFsm.addTransition(newSource, symbols, newTarget);
            });
        });

        // Copy transitions from fsm2 to the concatenated FSM
        copyFsm2.getTransitions().forEach((sourceState, transitions) -> {
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

        // Set the final and start states for the concatenated FSM
        concatenatedFsm.setExpression(copyFsm1.getExpression() + copyFsm2.getExpression());
        return concatenatedFsm;
    }

}
