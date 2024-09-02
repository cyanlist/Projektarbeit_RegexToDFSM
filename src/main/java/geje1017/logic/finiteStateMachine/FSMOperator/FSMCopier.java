package geje1017.logic.finiteStateMachine.Operation;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FSMCopier extends FSMOperation {

    /**
     * Creates a deep copy of a finite state machine (FSM).
     * This method duplicates the given FSM, including all its states, transitions, and the current expression,
     * ensuring that changes to the copy do not affect the original FSM.
     *
     * @param fsm The FSM to be copied.
     * @return A new FSM instance that is a complete copy of the original FSM.
     */
    public static FSMStructure copyFsm(FSMStructure fsm) {
        FSMStructure copy = new FSMStructure();

        // A mapping to keep track of old states to their corresponding new states.
        // This is crucial for maintaining the structure of the FSM while replacing references.
        Map<State, State> stateMapping = new HashMap<>();

        // First, copy all states and create a mapping from old to new states.
        // This loop goes through each state used as a key in the transitions map.
        for (State oldState : fsm.getTransitions().keySet()) {
            State newState = new State(oldState);
            stateMapping.put(oldState, newState);
            copy.addTransition(newState, null, null);
        }
        // Next, copy all transitions using the previously created state mapping.
        // This loop iterates over all entries in the original FSM's transitions map.
        fsm.getTransitions().forEach((src, tgtMap) -> {
            State newSrc = stateMapping.get(src);
            tgtMap.forEach((tgt, symbols) -> {
                State newTgt = stateMapping.get(tgt);
                Set<String> newSymbols = new HashSet<>(symbols);
                copy.addTransition(newSrc, newSymbols, newTgt);
            });
        });

        copy.setExpression(fsm.getExpression());
        return copy;
    }

}
