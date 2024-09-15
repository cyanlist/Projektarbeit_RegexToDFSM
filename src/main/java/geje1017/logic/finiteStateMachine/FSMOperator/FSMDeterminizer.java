package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.CustomHashSet;
import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.*;

import static geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier.copyFsm;

/**
 * Converts a non-deterministic finite automaton (NFA) into a deterministic finite automaton (DFA)
 * using the subset construction method.
 */
public class FSMDeterminizer {

    /**
     * Converts a non-deterministic finite automaton (NFA) to a deterministic finite automaton (DFA)
     * using the subset construction method.
     * The conversion involves creating new states in the DFA that correspond to sets of states from the NFA.
     * Transitions between these sets of states are determined by analyzing the transitions in the NFA.
     *
     * @param fsm The NFA to be converted.
     * @return A new DFA that represents the deterministic version of the input NFA.
     */
    public static FSMStructure toDeterministicFsm(FSMStructure fsm) {

        FSMStructure copyFsm = copyFsm(fsm);
        FSMStructure deterministicFsm = new FSMStructure();
        deterministicFsm.setExplanation("Transforming the FSM into a deterministic version.\n");

        // Map from sets of NFA states to DFA states.
        Map<Set<State>, State> stateMap = new HashMap<>();
        // Queue to manage sets of NFA states for processing.
        Queue<Set<State>> queue = new LinkedList<>();

        // Create the start state for the DFA.
        Set<State> startSet = copyFsm.getStartStates();
        State dfaStartState = State.fuseState(startSet);
        stateMap.put(startSet, dfaStartState);
        queue.add(startSet);
        // deterministicFsm.transitions.put(dfaStartState, new HashMap<>());
        deterministicFsm.addTransition(dfaStartState, null, null);

        // Process all sets of states.
        while (!queue.isEmpty()) {
            Set<State> currentSet = queue.poll();
            State currentState = stateMap.get(currentSet);
            // Map to track transitions for the current set based on input symbols.
            Map<String, Set<State>> transitionMap = new HashMap<>();
            for (State state : currentSet) {
                copyFsm.transitions.getOrDefault(state, new HashMap<>()).forEach((target, symbols) -> symbols.forEach(symbol -> transitionMap.computeIfAbsent(symbol, k -> new CustomHashSet<>()).add(target)));
            }

            // Create new transitions and states in the DFA.
            transitionMap.forEach((symbol, targetSet) -> {
                if (!stateMap.containsKey(targetSet)) {
                    State newState;
                    newState = State.fuseState(targetSet);
                    newState.resetStartState();
                    stateMap.put(targetSet, newState);
                    queue.add(targetSet);
                    deterministicFsm.transitions.put(newState, new HashMap<>());
                }
                State targetState = stateMap.get(targetSet);
                deterministicFsm.transitions.get(currentState).computeIfAbsent(targetState, k -> new CustomHashSet<>()).add(symbol);
            });
        }

        deterministicFsm.setExpression(copyFsm.getExpression());
        return deterministicFsm;
    }

}
