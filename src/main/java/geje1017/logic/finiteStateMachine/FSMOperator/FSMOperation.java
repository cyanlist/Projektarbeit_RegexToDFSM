package geje1017.logic.finiteStateMachine.Operation;

import geje1017.logic.finiteStateMachine.CustomHashSet;
import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;
import geje1017.logic.postfix.InputManager;

import java.util.*;

public class FSMOperation {

//    // TODO: Skript Seite 58 für Sonderfälle
//
//    /**
//     * Converts the input symbol to an FSM.
//     *
//     * @param inputSymbol The input symbol to be converted.
//     * @return The converted FSM.
//     */
//    public static FSMStructure convertInputCharacter(char inputSymbol) {
//
//        FSMStructure convertedFSM = new FSMStructure();
//
//        if (!InputManager.isOperand(inputSymbol)) {
//            throw new IllegalArgumentException("Invalid input symbol: " + inputSymbol);
//        }
//
//        if (inputSymbol == InputManager.getEmptySymbol()) {
//            convertEmptyString(convertedFSM);
//        }
//        else if (inputSymbol == InputManager.getEmptySet()) {
//            convertEmptySet(convertedFSM);
//        }
//        else {
//            convertRegularSymbol(convertedFSM, inputSymbol);
//        }
//        return convertedFSM;
//    }
//
//    /**
//     * Handles the case when the input symbol is "e".
//     *
//     * @param fsm The FSM to be modified.
//     */
//    private static void convertEmptyString(FSMStructure fsm) {
//        State startEndState = new State(true, true);
//        fsm.addTransition(startEndState, Collections.emptySet(), startEndState);
//        fsm.setExpression(fsm.getExpression().isEmpty() ? String.valueOf(InputManager.getEmptySymbol()) : fsm.getExpression());
//
//    }
//
//    /**
//     * Handles the case when the input symbol is "0/".
//     *
//     * @param fsm The FSM to be modified.
//     */
//    private static void convertEmptySet(FSMStructure fsm) {
//        State startState = new State(true, false);
//        fsm.addTransition(startState, Collections.emptySet(), null);
//        fsm.setExpression(fsm.getExpression().isEmpty() ? String.valueOf(InputManager.getEmptySet()) : fsm.getExpression());
//
//    }
//
//    /**
//     * Handles the case when the input symbol is a regular symbol.
//     *
//     * @param fsm The FSM to be modified.
//     * @param inputSymbol The input symbol.
//     */
//    private static void convertRegularSymbol(FSMStructure fsm, char inputSymbol) {
//        State startState = new State(true, false);
//        State finalState = new State(false, true);
//        fsm.addTransition(startState, new HashSet<>(Collections.singletonList(String.valueOf(inputSymbol))), finalState);
//        fsm.setExpression(String.valueOf(inputSymbol));
//    }
//
//    /**
//     * Concatenates two deterministic FSMs.
//     *
//     * @param fsm1 The first deterministic FSM.
//     * @param fsm2 The second deterministic FSM.
//     * @return The concatenated FSM.
//     */
//
////    public static FSMStructure concatenate(FSMStructure fsm1, FSMStructure fsm2) {
////
////        FSMStructure copyFsm1 = copyFsm(fsm1);
////        FSMStructure copyFsm2 = copyFsm(fsm2);
////
////        if (fsm1.getExpression().equals("0") ) {
////            return copyFsm1;
////        }
////
////        if (fsm2.getExpression().equals("0") ) {
////            return copyFsm2;
////        }
////
////        if (fsm2.getExpression().equals("e")) {
////            return copyFsm1;
////        }
////
////        if (fsm1.getExpression().equals("e")) {
////            return copyFsm2;
////        }
////
////        FSMStructure concatenatedFsm = new FSMStructure();
////
////        Set<State> finalStatesFsm1 = new HashSet<>(copyFsm1.getFinalStates());
////        State initialStateFsm2 = copyFsm2.getStartState();
////
////        // TODO: Error: Hier werden alle fsm1Finalstates miteinander gefused.
////        // Eigentlich: Jeder fms1Finalstate einzeln mit fsm2Startstate fusen.
////        Set<State> fuseStateSet = new HashSet<>(finalStatesFsm1);
////        fuseStateSet.add(initialStateFsm2);
////        State fusedState = State.fuseState(fuseStateSet);
////
////        copyFsm1.getTransitions().forEach((sourceState, transitions) -> {
////            State newSource = finalStatesFsm1.contains(sourceState) ? fusedState : sourceState;
////            transitions.forEach((targetState, symbols) -> {
////                State newTarget = finalStatesFsm1.contains(targetState) ? fusedState : targetState;
////                concatenatedFsm.addTransition(newSource, symbols, newTarget);
////            });
////        });
////
////        copyFsm2.getTransitions().forEach((sourceState, transitions) -> {
////            State newSource = sourceState.equals(initialStateFsm2) ? fusedState : sourceState;
////            transitions.forEach((targetState, symbols) -> {
////                State newTarget = targetState.equals(initialStateFsm2) ? fusedState : targetState;
////                concatenatedFsm.addTransition(newSource, symbols, newTarget);
////            });
////        });
////
////        // Konfigurieren des fusionierten Zustandes
////        if (finalStatesFsm1.stream().noneMatch(State::isStartState)) fusedState.resetStartState();
////        if (!initialStateFsm2.isFinalState()) fusedState.resetFinalState();
////
////        concatenatedFsm.setExpression(copyFsm1.getExpression() + copyFsm2.getExpression());
////        return concatenatedFsm;
////    }
//
//    public static FSMStructure concatenate(FSMStructure fsm1, FSMStructure fsm2) {
//
//        FSMStructure copyFsm1 = copyFsm(fsm1);
//        FSMStructure copyFsm2 = copyFsm(fsm2);
//
//        if (fsm1.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
//            return copyFsm1;
//        }
//
//        if (fsm2.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
//            return copyFsm2;
//        }
//
//        if (fsm2.getExpression().equals(String.valueOf(InputManager.getEmptySymbol()))) {
//            return copyFsm1;
//        }
//
//        if (fsm1.getExpression().equals(String.valueOf(InputManager.getEmptySymbol()))) {
//            return copyFsm2;
//        }
//
//        FSMStructure concatenatedFsm = new FSMStructure();
//
//        Set<State> finalStatesFsm1 = new HashSet<>(copyFsm1.getFinalStates());
//        State initialStateFsm2 = copyFsm2.getStartState();
//
//        // Iterate over each final state of fsm1
//        Map<State, State> fusedStatesMap = new HashMap<>();
//        for (State finalStateFsm1 : finalStatesFsm1) {
//            // Fuse each final state of fsm1 with the start state of fsm2 individually
//            Set<State> fuseStateSet = new HashSet<>();
//            fuseStateSet.add(finalStateFsm1);
//            fuseStateSet.add(initialStateFsm2);
//            State fusedState = State.fuseState(fuseStateSet);
//            if (!initialStateFsm2.isFinalState()) fusedState.resetFinalState();
//            if (!finalStateFsm1.isStartState()) fusedState.resetStartState();
//            // Store the fused state to use in transition copying
//            fusedStatesMap.put(finalStateFsm1, fusedState);
//        }
//
//        // Copy transitions from fsm1 to the concatenated FSM
//        copyFsm1.getTransitions().forEach((sourceState, transitions) -> {
//            transitions.forEach((targetState, symbols) -> {
//                State newSource = fusedStatesMap.getOrDefault(sourceState, sourceState);
//                State newTarget = fusedStatesMap.getOrDefault(targetState, targetState);
//                concatenatedFsm.addTransition(newSource, symbols, newTarget);
//            });
//        });
//
//        // Copy transitions from fsm2 to the concatenated FSM
//        copyFsm2.getTransitions().forEach((sourceState, transitions) -> {
//            transitions.forEach((targetState, symbols) -> {
//                // Handle transitions involving the start state of fsm2
//                if (sourceState.equals(initialStateFsm2)) {
//                    // For each fused state, add transitions to targetState
//                    fusedStatesMap.values().forEach(fusedState -> {
//                        concatenatedFsm.addTransition(fusedState, symbols, targetState);
//                    });
//                } else if (targetState.equals(initialStateFsm2)) {
//                    // Add transitions from sourceState to each fused state
//                    fusedStatesMap.forEach((finalStateFsm1, fusedState) -> {
//                        concatenatedFsm.addTransition(sourceState, symbols, fusedState);
//                    });
//                } else {
//                    // Standard transition copy for other states
//                    concatenatedFsm.addTransition(sourceState, symbols, targetState);
//                }
//            });
//        });
//
//        initialStateFsm2.resetStartState();
//
//        // Set the final and start states for the concatenated FSM
//        concatenatedFsm.setExpression(copyFsm1.getExpression() + copyFsm2.getExpression());
//        return concatenatedFsm;
//    }
//
//
//    /*
//    public static FSMStructure concatenate(FSMStructure fsm1, FSMStructure fsm2) {
//
//        FSMStructure copyFsm1 = copyFsm(fsm1);
//        FSMStructure copyFsm2 = copyFsm(fsm2);
//
//        FSMStructure concatenatedFsm = new FSMStructure();
//
//        concatenatedFsm.addAllTransitions(copyFsm1.getTransitions());
//        Set<State> finalStatesFsm1 = copyFsm1.getFinalStates();
//
//        copyFsm2.getTransitions().forEach((sourceState, transition) -> transition.forEach((targetState, inputSymbol) -> {
//            if (sourceState.isStartState()) {
//
//                finalStatesFsm1.forEach(finalState -> concatenatedFsm.addTransition(finalState, inputSymbol, targetState.isStartState() ? finalState : targetState));
//                // Todo: besser machen
//                finalStatesFsm1.forEach(finalState -> State.fuseName(sourceState, finalState));
//
//            }
//            else {
//                concatenatedFsm.addTransition(sourceState, inputSymbol, targetState);
//            }
//        }));
//
//        if(!copyFsm2.getStartState().isFinalState() || !copyFsm1.getStartState().isFinalState()) finalStatesFsm1.forEach(State::resetFinalState);
//
//        concatenatedFsm.setExpression(copyFsm1.getExpression() + copyFsm2.getExpression());
//        return concatenatedFsm;
//    }
//
//     */
//
//
//    /**
//     * Creates an alternation of two deterministic FSMs.
//     *
//     * @param fsm1 The first deterministic FSM.
//     * @param fsm2 The second deterministic FSM.
//     * @return The alternated FSM.
//     */
////    public static FSMStructure alternate(FSMStructure fsm1, FSMStructure fsm2) {
////
////        // TODO: e und 0? --> Führt bisher zum falschen Ergebnis
////        // TODO: StartStates fusionieren
////
////        FSMStructure copyFsm1 = copyFsm(fsm1);
////        FSMStructure copyFsm2 = copyFsm(fsm2);
////
////        FSMStructure alternatedFsm = new FSMStructure();
////
////        alternatedFsm.addAllTransitions(copyFsm1.getTransitions());
////        State startStatesFsm1 = copyFsm1.getStartState();
////
////        copyFsm2.getTransitions().forEach((sourceState, transition) -> transition.forEach((targetState, inputSymbol) -> {
////
////            State tempSourceState = sourceState.isStartState() ? startStatesFsm1 : sourceState;
////            State tempTargetState = targetState.isStartState() ? startStatesFsm1 : targetState;
////
////            alternatedFsm.addTransition(tempSourceState, inputSymbol, tempTargetState);
////        }));
////
////        alternatedFsm.setExpression(copyFsm1.getExpression() + "|" + copyFsm2.getExpression());
////        return alternatedFsm;
////    }
//
//    public static FSMStructure alternate(FSMStructure fsm1, FSMStructure fsm2) {
//        FSMStructure alternatedFsm = new FSMStructure();
//
//        // Neuer Startzustand
//        State newStartState = new State(true, false); // true für Startzustand, false für Endzustand
//        alternatedFsm.addTransition(newStartState, null, null);
//
//        // Füge den neuen Startzustand Übergänge hinzu
//        initializeStartStateTransitions(alternatedFsm, newStartState, fsm1);
//        initializeStartStateTransitions(alternatedFsm, newStartState, fsm2);
//
//        // Füge alle Übergänge aus beiden FSMs hinzu
//        alternatedFsm.addAllTransitions(fsm1.getTransitions());
//        alternatedFsm.addAllTransitions(fsm2.getTransitions());
//
//        // Setze den Ausdruck
//        alternatedFsm.setExpression(fsm1.getExpression() + "|" + fsm2.getExpression());
//        return alternatedFsm;
//    }
//
//    private static void initializeStartStateTransitions(FSMStructure resultFsm, State newStartState, FSMStructure fsm) {
//        Set<State> startStates = fsm.getStartStates();
//        for (State startState : startStates) {
//            // Gehe durch alle Übergänge der Startzustände
//            Map<State, Set<String>> startTransitions = fsm.getTransitions().get(startState);
//            if (startTransitions != null) {
//                for (Map.Entry<State, Set<String>> transition : startTransitions.entrySet()) {
//                    // Füge für jedes Eingabesymbol einen Übergang vom neuen Startzustand zum Zielzustand hinzu
//                    resultFsm.addTransition(newStartState, transition.getValue(), transition.getKey());
//                }
//            }
//        }
//    }
//
//    /**
//     * Applies positive closure to a deterministic FSM.
//     * Positive closure means that the FSM can repeat its entire sequence of states one or more times.
//     *
//     * @param fsm The deterministic FSM to apply positive closure to.
//     * @return The FSM after applying positive closure.
//     */
//    public static FSMStructure applyPositiveClosure(FSMStructure fsm) {
//
//        FSMStructure copyFsm = copyFsm(fsm);
//
//        FSMStructure positiveClosureFsm = new FSMStructure();
//
//        positiveClosureFsm.addAllTransitions(copyFsm.getTransitions());
//        State startStatesFsm = copyFsm.getStartState();
//
//        copyFsm.getTransitions().forEach((sourceState, transition) -> transition.forEach((targetState, inputSymbol) -> {
//            if (targetState.isFinalState()) {
//                positiveClosureFsm.addTransition(sourceState, inputSymbol, startStatesFsm);
//            }
//        }));
//
//        System.out.println(positiveClosureFsm);
//
//        positiveClosureFsm.setExpression("(" + copyFsm.getExpression() + ")+");
//        return positiveClosureFsm;
//    }
//
//    /**
//     * Applies Kleene closure to a deterministic FSM.
//     *
//     * @param fsm The deterministic FSM to apply Kleene closure to.
//     * @return The FSM after applying Kleene closure.
//     */
//    public static FSMStructure applyKleeneClosure(FSMStructure fsm) {
//
//        String temp = fsm.getExpression() + "*";
//
//        FSMStructure kleeneClosureFsm = applyPositiveClosure(fsm);
//        convertEmptyString(kleeneClosureFsm);
//
//        kleeneClosureFsm.setExpression(temp);
//        return kleeneClosureFsm;
//
//    }
//
//    /**
//     * Minimizes a deterministic FSM by merging equivalent states.
//     * This method first ensures that the provided FSM is deterministic,
//     * then identifies reachable states, forms equivalence classes of states,
//     * and finally merges states within the same equivalence class.
//     *
//     * @param fsm The deterministic FSM to minimize.
//     * @return The minimized FSM.
//     */
//    public static FSMStructure minimize(FSMStructure fsm) {
//
//        fsm = copyFsm(fsm);
//        FSMStructure minimizedFsm;
//
//        if (fsm.getExpression().length() <= 1)  {
//            return fsm;
//        }
//
//        Set<State> states = fsm.getTransitions().keySet();
//        Map<State, Map<String, State>> transitionTable = buildTransitionTable(fsm);
//
//        // Initialize partitions: Final and non-final states
//        Set<Set<State>> partitions = new HashSet<>();
//        Set<State> finalStates = new HashSet<>();
//        Set<State> nonFinalStates = new HashSet<>();
//
//        for (State state : states) {
//            if (state.isFinalState()) {
//                finalStates.add(state);
//            } else {
//                nonFinalStates.add(state);
//            }
//        }
//
//        if (!finalStates.isEmpty()) partitions.add(finalStates);
//        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);
//
//        // Refine partitions
//        boolean wasRefined;
//        do {
//            wasRefined = false;
//            Set<Set<State>> newPartitions = new HashSet<>();
//
//            for (Set<State> partition : partitions) {
//                Set<Set<State>> refinedPartition = refinePartition(partition, partitions, transitionTable);
//                newPartitions.addAll(refinedPartition);
//                if (refinedPartition.size() > 1) wasRefined = true;
//            }
//
//            partitions = newPartitions;
//        } while (wasRefined);
//
//        // Build the minimized FSM from partitions
//
//        minimizedFsm = buildMinimizedFsm(partitions, transitionTable);
//        minimizedFsm.setExpression(fsm.getExpression());
//        minimizedFsm.foo = "Minimized: ";
//
//        return minimizedFsm;
//    }
//
//    private static Map<State, Map<String, State>> buildTransitionTable(FSMStructure fsm) {
//        Map<State, Map<String, State>> table = new HashMap<>();
//        fsm.getTransitions().forEach((src, tgtMap) -> {
//            Map<String, State> transitionMap = new HashMap<>();
//            tgtMap.forEach((tgt, symbols) -> symbols.forEach(symbol -> transitionMap.put(symbol, tgt)));
//            table.put(src, transitionMap);
//        });
//        return table;
//    }
//
//    private static Set<Set<State>> refinePartition(Set<State> partition, Set<Set<State>> partitions, Map<State, Map<String, State>> transitionTable) {
//        Map<Map<String, Set<State>>, Set<State>> signatureToStates = new HashMap<>();
//
//        for (State state : partition) {
//            Map<String, Set<State>> signature = new HashMap<>();
//            transitionTable.get(state).forEach((symbol, target) -> partitions.forEach(p -> {
//                if (p.contains(target)) {
//                    signature.putIfAbsent(symbol, new HashSet<>());
//                    signature.get(symbol).add(target);
//                }
//            }));
//            signatureToStates.putIfAbsent(signature, new HashSet<>());
//            signatureToStates.get(signature).add(state);
//        }
//
//        return new HashSet<>(signatureToStates.values());
//    }
//
//    private static FSMStructure buildMinimizedFsm(Set<Set<State>> partitions, Map<State, Map<String, State>> transitionTable) {
//        FSMStructure minimizedFsm = new FSMStructure();
//        Map<State, State> stateMap = new HashMap<>();
//
//        // Create a representative state for each partition
//        for (Set<State> partition : partitions) {
//            State representative = State.fuseState(partition);
//            partition.forEach(state -> stateMap.put(state, representative));
//            minimizedFsm.getTransitions().putIfAbsent(representative, new HashMap<>());
//        }
//
//        // Add transitions for the minimized FSM
//        for (State original : transitionTable.keySet()) {
//            State newSrc = stateMap.get(original);
//            transitionTable.get(original).forEach((symbol, target) -> {
//                State newTgt = stateMap.get(target);
//                minimizedFsm.addTransition(newSrc, new HashSet<>(Collections.singletonList(symbol)), newTgt);
//            });
//        }
//
//        return minimizedFsm;
//    }
//
//    /**
//     * Converts a non-deterministic finite automaton (NFA) to a deterministic finite automaton (DFA)
//     * using the subset construction method.
//     * @param fsm The NFA to be converted.
//     * @return A new DFA that represents the deterministic version of the input NFA.
//     */
//    public static FSMStructure toDeterministicFsm(FSMStructure fsm) {
//
//        FSMStructure copyFsm = copyFsm(fsm);
//        FSMStructure deterministicFsm = new FSMStructure();
//
//        // Map from sets of NFA states to DFA states.
//        Map<Set<State>, State> stateMap = new HashMap<>();
//        // Queue to manage sets of NFA states for processing.
//        Queue<Set<State>> queue = new LinkedList<>();
//
//        // Create the start state for the DFA.
//        Set<State> startSet = copyFsm.getStartStates();
//        State dfaStartState = State.fuseState(startSet);
//        stateMap.put(startSet, dfaStartState);
//        queue.add(startSet);
//        // deterministicFsm.transitions.put(dfaStartState, new HashMap<>());
//        deterministicFsm.addTransition(dfaStartState, null, null);
//
//        // Process all sets of states.
//        while (!queue.isEmpty()) {
//            Set<State> currentSet = queue.poll();
//            State currentState = stateMap.get(currentSet);
//            // Map to track transitions for the current set based on input symbols.
//            Map<String, Set<State>> transitionMap = new HashMap<>();
//            for (State state : currentSet) {
//                copyFsm.transitions.getOrDefault(state, new HashMap<>()).forEach((target, symbols) -> symbols.forEach(symbol -> transitionMap.computeIfAbsent(symbol, k -> new CustomHashSet<>()).add(target)));
//            }
//
//            // Create new transitions and states in the DFA.
//            transitionMap.forEach((symbol, targetSet) -> {
//                if (!stateMap.containsKey(targetSet)) {
//                    State newState;
//                    newState = State.fuseState(targetSet);
//                    newState.resetStartState();
//                    stateMap.put(targetSet, newState);
//                    queue.add(targetSet);
//                    deterministicFsm.transitions.put(newState, new HashMap<>());
//                }
//                State targetState = stateMap.get(targetSet);
//                deterministicFsm.transitions.get(currentState).computeIfAbsent(targetState, k -> new CustomHashSet<>()).add(symbol);
//            });
//        }
//
//        deterministicFsm.setExpression(copyFsm.getExpression());
//        return deterministicFsm;
//    }
//
//
//    /**
//     * Creates a deep copy of a finite state machine (FSM).
//     * This method duplicates the given FSM, including all its states, transitions, and the current expression,
//     * ensuring that changes to the copy do not affect the original FSM.
//     *
//     * @param fsm The FSM to be copied.
//     * @return A new FSM instance that is a complete copy of the original FSM.
//     */
//    public static FSMStructure copyFsm(FSMStructure fsm) {
//        FSMStructure copy = new FSMStructure();
//
//        // A mapping to keep track of old states to their corresponding new states.
//        // This is crucial for maintaining the structure of the FSM while replacing references.
//        Map<State, State> stateMapping = new HashMap<>();
//
//        // First, copy all states and create a mapping from old to new states.
//        // This loop goes through each state used as a key in the transitions map.
//        for (State oldState : fsm.getTransitions().keySet()) {
//            State newState = new State(oldState);
//            stateMapping.put(oldState, newState);
//            copy.addTransition(newState, null, null);
//        }
//        // Next, copy all transitions using the previously created state mapping.
//        // This loop iterates over all entries in the original FSM's transitions map.
//        fsm.getTransitions().forEach((src, tgtMap) -> {
//            State newSrc = stateMapping.get(src);
//            tgtMap.forEach((tgt, symbols) -> {
//                State newTgt = stateMapping.get(tgt);
//                Set<String> newSymbols = new HashSet<>(symbols);
//                copy.addTransition(newSrc, newSymbols, newTgt);
//            });
//        });
//
//        copy.setExpression(fsm.getExpression());
//        return copy;
//    }

}
