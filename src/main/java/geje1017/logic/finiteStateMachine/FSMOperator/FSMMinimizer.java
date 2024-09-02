package geje1017.logic.finiteStateMachine.Operation;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.*;

import static geje1017.logic.finiteStateMachine.Operation.FSMCopier.copyFsm;

public class FSMMinimizer extends FSMOperation{

    /**
     * Minimizes a deterministic FSM by merging equivalent states.
     * This method first ensures that the provided FSM is deterministic,
     * then identifies reachable states, forms equivalence classes of states,
     * and finally merges states within the same equivalence class.
     *
     * @param fsm The deterministic FSM to minimize.
     * @return The minimized FSM.
     */
    public static FSMStructure minimize(FSMStructure fsm) {

        fsm = copyFsm(fsm);
        FSMStructure minimizedFsm;

        if (fsm.getExpression().length() <= 1)  {
            return fsm;
        }

        Set<State> states = fsm.getTransitions().keySet();
        Map<State, Map<String, State>> transitionTable = buildTransitionTable(fsm);

        // Initialize partitions: Final and non-final states
        Set<Set<State>> partitions = new HashSet<>();
        Set<State> finalStates = new HashSet<>();
        Set<State> nonFinalStates = new HashSet<>();

        for (State state : states) {
            if (state.isFinalState()) {
                finalStates.add(state);
            } else {
                nonFinalStates.add(state);
            }
        }

        if (!finalStates.isEmpty()) partitions.add(finalStates);
        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);

        // Refine partitions
        boolean wasRefined;
        do {
            wasRefined = false;
            Set<Set<State>> newPartitions = new HashSet<>();

            for (Set<State> partition : partitions) {
                Set<Set<State>> refinedPartition = refinePartition(partition, partitions, transitionTable);
                newPartitions.addAll(refinedPartition);
                if (refinedPartition.size() > 1) wasRefined = true;
            }

            partitions = newPartitions;
        } while (wasRefined);

        // Build the minimized FSM from partitions

        minimizedFsm = buildMinimizedFsm(partitions, transitionTable);
        minimizedFsm.setExpression(fsm.getExpression());
        minimizedFsm.foo = "Minimized: ";

        return minimizedFsm;
    }

    private static Map<State, Map<String, State>> buildTransitionTable(FSMStructure fsm) {
        Map<State, Map<String, State>> table = new HashMap<>();
        fsm.getTransitions().forEach((src, tgtMap) -> {
            Map<String, State> transitionMap = new HashMap<>();
            tgtMap.forEach((tgt, symbols) -> symbols.forEach(symbol -> transitionMap.put(symbol, tgt)));
            table.put(src, transitionMap);
        });
        return table;
    }

    private static Set<Set<State>> refinePartition(Set<State> partition, Set<Set<State>> partitions, Map<State, Map<String, State>> transitionTable) {
        Map<Map<String, Set<State>>, Set<State>> signatureToStates = new HashMap<>();

        for (State state : partition) {
            Map<String, Set<State>> signature = new HashMap<>();
            transitionTable.get(state).forEach((symbol, target) -> partitions.forEach(p -> {
                if (p.contains(target)) {
                    signature.putIfAbsent(symbol, new HashSet<>());
                    signature.get(symbol).add(target);
                }
            }));
            signatureToStates.putIfAbsent(signature, new HashSet<>());
            signatureToStates.get(signature).add(state);
        }

        return new HashSet<>(signatureToStates.values());
    }

    private static FSMStructure buildMinimizedFsm(Set<Set<State>> partitions, Map<State, Map<String, State>> transitionTable) {
        FSMStructure minimizedFsm = new FSMStructure();
        Map<State, State> stateMap = new HashMap<>();

        // Create a representative state for each partition
        for (Set<State> partition : partitions) {
            State representative = State.fuseState(partition);
            partition.forEach(state -> stateMap.put(state, representative));
            minimizedFsm.getTransitions().putIfAbsent(representative, new HashMap<>());
        }

        // Add transitions for the minimized FSM
        for (State original : transitionTable.keySet()) {
            State newSrc = stateMap.get(original);
            transitionTable.get(original).forEach((symbol, target) -> {
                State newTgt = stateMap.get(target);
                minimizedFsm.addTransition(newSrc, new HashSet<>(Collections.singletonList(symbol)), newTgt);
            });
        }

        return minimizedFsm;
    }

}
