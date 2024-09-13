package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.*;

public class FSMMinimizer {

    // (ab)|(cb)

    public static FSMStructure minimize(FSMStructure fsm) {
        // Schritt 1: Initiale Partitionierung der Zustände in Final- und Nicht-Finalzustände
        Set<State> finalStates = fsm.getFinalStates();
        Set<State> nonFinalStates = new HashSet<>(fsm.getStates());
        nonFinalStates.removeAll(finalStates);

        // Initiale Partitionen: Finalzustände und Nicht-Finalzustände
        List<Set<State>> partitions = new ArrayList<>();
        if (!finalStates.isEmpty()) partitions.add(finalStates);
        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);

        System.out.println("Initiale Partitionen: " + partitions);

        // Schritt 2: Paarweises Vergleichen und Verfeinerung der Partitionen
        boolean changed;
        do {
            changed = false;
            List<Set<State>> newPartitions = new ArrayList<>();

            // Durchlaufe jede Partition
            for (Set<State> partition : partitions) {
                // Map für die Verfeinerung basierend auf Signaturen
                Map<String, Set<State>> split = new HashMap<>();

                // Erstelle die Signatur für jeden Zustand in der Partition
                for (State state : partition) {
                    String signature = getSignature(state, fsm, partitions);  // Signatur als String
                    split.computeIfAbsent(signature, k -> new HashSet<>()).add(state);
                }

                // Neue Partitionen basierend auf den Signaturen
                newPartitions.addAll(split.values());

                // Wenn die Partition aufgeteilt wurde, setze "changed" auf true
                if (split.size() > 1) {
                    changed = true;
                }
            }

            partitions = newPartitions;
            System.out.println("Aktualisierte Partitionen nach Verfeinerung: " + partitions);

        } while (changed);

        // Schritt 3: Erzeuge den minimierten Automaten
        return buildMinimizedFsm(partitions, fsm);
    }


    // Erzeuge eine Signatur eines Zustands basierend auf den Übergängen und Zielzuständen
    private static String getSignature(State state, FSMStructure fsm, List<Set<State>> partitions) {
        StringBuilder signature = new StringBuilder();
        Map<State, Set<String>> transitions = fsm.getTargetStatesAndSymbols(state);

        // Durchlaufe alle Übergänge des Zustands
        for (Map.Entry<State, Set<String>> entry : transitions.entrySet()) {
            State target = entry.getKey();
            Set<String> symbols = entry.getValue();

            // Finde die Partition, in der der Zielzustand liegt
            for (Set<State> partition : partitions) {
                if (partition.contains(target)) {
                    // Verwende die Partition selbst als Teil der Signatur
                    signature.append("Symbol: ").append(symbols).append(" -> Partition: ").append(partition.hashCode()).append(";");
                    break;
                }
            }
        }

        // Füge eine Kennzeichnung für Final-/Nicht-Finalzustände hinzu
        if (state.isFinalState()) {
            signature.append("Final;");
        } else {
            signature.append("NonFinal;");
        }

        // Rückgabe der Signatur als String
        return signature.toString();
    }




    // Baut den minimierten Automaten basierend auf den verfeinerten Partitionen
    private static FSMStructure buildMinimizedFsm(List<Set<State>> partitions, FSMStructure fsm) {
        FSMStructure minimizedFsm = new FSMStructure();

        // Erstelle eine Repräsentation für jeden Zustand in der Partition
        Map<State, State> representativeMap = new HashMap<>();
        for (Set<State> partition : partitions) {
            State representative = State.fuseState(partition); // Füge die Zustände zusammen
            for (State state : partition) {
                representativeMap.put(state, representative);
            }
            minimizedFsm.addTransition(representative, null, null);
        }

        // Füge die Übergänge für den minimierten Automaten hinzu
        for (Map.Entry<State, Map<State, Set<String>>> entry : fsm.getTransitions().entrySet()) {
            State originalState = entry.getKey();
            State representative = representativeMap.get(originalState);

            for (Map.Entry<State, Set<String>> transition : entry.getValue().entrySet()) {
                State target = transition.getKey();
                State targetRepresentative = representativeMap.get(target);

                minimizedFsm.addTransition(representative, transition.getValue(), targetRepresentative);
            }
        }

        // Setze die Startzustände des minimierten Automaten
        Set<State> startStates = fsm.getStartStates();
        for (State startState : startStates) {
            State representative = representativeMap.get(startState);
            if (representative != null) {
                representative.setStartState(true);
            }
        }

        // Setze die Finalzustände des minimierten Automaten
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
