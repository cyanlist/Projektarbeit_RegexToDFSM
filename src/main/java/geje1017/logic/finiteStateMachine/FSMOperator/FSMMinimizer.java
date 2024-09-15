package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.*;

/**
 * Minimizes a deterministic finite state machine (FSM) by merging equivalent states.
 * The minimization is achieved using partition refinement, where states
 * that have the same behavior with respect to future transitions are grouped into equivalence classes.
 */
public class FSMMinimizer {

    /**
     * Minimizes a deterministic finite state machine (FSM) by reducing the number of states.
     * The algorithm merges states that are equivalent, meaning they have the same behavior
     * with respect to future transitions.
     *
     * @param fsm The FSM to be minimized.
     * @return A new minimized FSM with the same language but fewer states.
     */
    public static FSMStructure minimize(FSMStructure fsm) {

        fsm = FSMCopier.copyFsm(fsm);

        Set<State> finalStates = fsm.getFinalStates();
        Set<State> nonFinalStates = new HashSet<>(fsm.getStates());
        nonFinalStates.removeAll(finalStates);

        // Initial partition: final states and non-final states
        List<Set<State>> partitions = new ArrayList<>();
        if (!finalStates.isEmpty()) partitions.add(finalStates);
        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);

        boolean changed;
        do {
            changed = false;
            List<Set<State>> newPartitions = new ArrayList<>();

            // Refine each partition based on state transitions
            for (Set<State> partition : partitions) {
                // Map for refining the partition based on transition signatures
                Map<String, Set<State>> split = new HashMap<>();

                // Create the signature for each state in the partition
                for (State state : partition) {
                    String signature = getSignature(state, fsm, partitions);  // Signatur als String
                    split.computeIfAbsent(signature, k -> new HashSet<>()).add(state);
                }

                // Add new partitions based on the signatures
                newPartitions.addAll(split.values());

                // If the partition was split, mark that changes occurred
                if (split.size() > 1) {
                    changed = true;
                }
            }

            partitions = newPartitions;

        } while (changed);

        return buildMinimizedFsm(partitions, fsm);
    }

    /**
     * Generates a unique signature for a given state. The signature represents the transitions
     * from this state and the partitions that the target states belong to.
     *
     * @param state The state for which to generate the signature.
     * @param fsm The FSM that contains the state.
     * @param partitions The current partitions of states.
     * @return A string signature representing the state's transitions.
     */
    private static String getSignature(State state, FSMStructure fsm, List<Set<State>> partitions) {
        StringBuilder signature = new StringBuilder();
        Map<State, Set<String>> transitions = fsm.getTargetStatesAndSymbols(state);

        // Process each transition from the current state
        for (Map.Entry<State, Set<String>> entry : transitions.entrySet()) {
            State target = entry.getKey();
            Set<String> symbols = entry.getValue();

            // Find the partition containing the target state
            for (Set<State> partition : partitions) {
                if (partition.contains(target)) {
                    // Use the partition's hash code as part of the signature
                    signature.append("Symbol: ").append(symbols).append(" -> Partition: ").append(partition.hashCode()).append(";");
                    break;
                }
            }
        }

        // Append information on whether the state is final or non-final
        if (state.isFinalState()) {
            signature.append("Final;");
        } else {
            signature.append("NonFinal;");
        }

        return signature.toString();
    }

    /**
     * Builds the minimized FSM based on the refined partitions of states.
     * Equivalent states are merged, and new transitions are created accordingly.
     *
     * @param partitions The refined partitions of states.
     * @param fsm The original FSM.
     * @return The minimized FSM.
     */
    private static FSMStructure buildMinimizedFsm(List<Set<State>> partitions, FSMStructure fsm) {
        FSMStructure minimizedFsm = new FSMStructure();
        minimizedFsm.setExpression(fsm.getExpression());
        minimizedFsm.setExplanation("Reducing the number of states by merging equivalent ones.");

        // Create a representative state for each partition
        Map<State, State> representativeMap = new HashMap<>();
        for (Set<State> partition : partitions) {
            State representative = State.fuseState(partition); // Füge die Zustände zusammen
            for (State state : partition) {
                representativeMap.put(state, representative);
            }
            minimizedFsm.addTransition(representative, null, null);
        }

        // Add transitions to the minimized FSM
        for (Map.Entry<State, Map<State, Set<String>>> entry : fsm.getTransitions().entrySet()) {
            State originalState = entry.getKey();
            State representative = representativeMap.get(originalState);

            for (Map.Entry<State, Set<String>> transition : entry.getValue().entrySet()) {
                State target = transition.getKey();
                State targetRepresentative = representativeMap.get(target);

                minimizedFsm.addTransition(representative, transition.getValue(), targetRepresentative);
            }
        }

        // Set the start states of the minimized FSM
        Set<State> startStates = fsm.getStartStates();
        for (State startState : startStates) {
            State representative = representativeMap.get(startState);
            if (representative != null) {
                representative.setStartState(true);
            }
        }

        // Set the final states of the minimized FSM
        Set<State> finalStates = fsm.getFinalStates();
        for (State finalState : finalStates) {
            State representative = representativeMap.get(finalState);
            if (representative != null) {
                representative.setFinalState(true);
            }
        }

        return minimizedFsm;
    }
}