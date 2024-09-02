package regToDEA.main.logic.finiteStateMachine;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the implementation of a finite state machine.
 * This class manages states and transitions, allowing for operations like adding transitions,
 * converting to a deterministic finite state machine, and more.
 */
public class FSMStructure {

    // Maps a source state to a map of target states and the corresponding transition symbols
    public Map<State, Map<State, Set<String>>> transitions;
    private String expression = "";
    public String foo = "";

    /**
     * Constructs a new finite state machine with empty transitions.
     */
    public FSMStructure() {
        this.transitions = new TreeMap<>(Comparator
                .comparing(State::isStartState, Comparator.reverseOrder())
                // Dann nach der ersten Nummer sortieren
                .thenComparing(State::getFirstNumber)
                // Dann nach der String-Repräsentation sortieren, falls notwendig
                .thenComparing(State::toString));
    }

    /**
     * Adds a transition from a source state to a target state with given input symbols.
     *
     * @param sourceState The starting state of the transition.
     * @param inputSymbol The symbols that trigger the transition.
     * @param targetState The ending state of the transition.
     */
    public void addTransition(State sourceState, Set<String> inputSymbol, State targetState) {
        // TODO: "Konstellationen" besser definieren
        if (sourceState != null) {
            transitions.putIfAbsent(sourceState, new HashMap<>());
            if (targetState != null) {
                transitions.putIfAbsent(targetState, new HashMap<>());
                transitions.get(sourceState).putIfAbsent(targetState, new CustomHashSet<>());
                transitions.get(sourceState).get(targetState).addAll(inputSymbol);
            }
        }
    }

    /**
     * Returns a string representation of the finite state machine.
     * This includes the represented expression, states, transitions, startStates and finalStates.
     *
     * @return A string detailing the current definition of the FSM.
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("M=(Q,∑,δ,S,F)");
        res.append("\nQ=").append(getStates());
        res.append("\n∑=").append(transitions.values().stream()
                .flatMap(map -> map.values().stream())
                .flatMap(Set::stream)
                .distinct()
                .collect(Collectors.toList()));
        res.append("\nδ: Q x ∑ --> Q: {");
        transitions.forEach((sourceState, transitionMap) -> {
            transitionMap.forEach((targetState, symbols) -> {
                symbols.forEach(symbol -> {
                    res.append("\n\tδ(").append(sourceState).append(", ").append(symbol).append(") = ").append(targetState);
                });
            });
        });
        res.append("}");

        res.append("\nS=").append(getStartStates());
        res.append("\nF=").append(getFinalStates());

        return res.toString();
    }

    /**
     * Adds all transitions from another map of transitions to this finite state machine.
     *
     * @param transitionsToAdd A map of transitions to add to this FSM.
     */
    public void addAllTransitions(Map<State, Map<State, Set<String>>> transitionsToAdd) {
        transitionsToAdd.forEach((sourceState, transitionMap) -> {
            transitionMap.forEach((targetState, inputSymbol) -> {
                addTransition(sourceState, inputSymbol, targetState);
            });
        });
    }

    // Getter and setter methods

    public String getExpression() {
        return this.expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<State, Map<State, Set<String>>> getTransitions() {
        return transitions;
    }

    public Set<State> getStates() {
        return getTransitions().keySet();
    }

    public Set<State> getStartStates() {
        return getStates().stream().filter(State::isStartState).collect(Collectors.toSet());
    }

    public State getStartState() {
        return getStates().stream().filter(State::isStartState).findFirst().get();
    }

    public Set<State> getFinalStates() {
        return getStates().stream().filter(State::isFinalState).collect(Collectors.toSet());
    }


    public Map<State, Set<String>> getTargetStatesAndSymbols(State sourceState) {
        return this.transitions.getOrDefault(sourceState, Map.of());
    }

}
