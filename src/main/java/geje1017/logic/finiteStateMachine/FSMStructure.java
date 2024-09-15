package geje1017.logic.finiteStateMachine;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a finite state machine (FSM) implementation.
 * This class manages states and transitions, providing functionalities such as adding transitions,
 * retrieving states and transitions, and creating a string representation of the FSM.
 */
public class FSMStructure {

    // Maps a source state to a map of target states and the corresponding transition symbols
    public Map<State, Map<State, Set<String>>> transitions;
    private String expression = "";
    private String explanation = "";

    /**
     * Constructs a new finite state machine (FSM) with empty transitions.
     * States are ordered by whether they are start states, by their first number,
     * and by their string representation.
     */
    public FSMStructure() {
        this.transitions = new TreeMap<>(Comparator
                .comparing(State::isStartState, Comparator.reverseOrder())
                .thenComparing(State::getFirstNumber)
                .thenComparing(State::toString)
                .thenComparing(State::isFinalState, Comparator.reverseOrder()));
    }

    /**
     * Adds a transition from a source state to a target state with the given input symbols.
     * If the source or target states do not already exist in the FSM, they are added.
     *
     * @param sourceState The state from which the transition begins.
     * @param inputSymbol The set of symbols that trigger the transition.
     * @param targetState The state to which the transition leads.
     */
    public void addTransition(State sourceState, Set<String> inputSymbol, State targetState) {
        if (sourceState != null) {
            transitions.putIfAbsent(sourceState, new HashMap<>());
            if (targetState != null) {
                transitions.putIfAbsent(targetState, new HashMap<>());
                if (inputSymbol != null) {
                    transitions.get(sourceState).putIfAbsent(targetState, new CustomHashSet<>());
                    transitions.get(sourceState).get(targetState).addAll(inputSymbol);
                }
            }
        }
    }

    /**
     * Returns a string representation of the FSM.
     * This representation includes the set of states (Q), the input alphabet (∑), the transition function (δ),
     * the set of start states (S), and the set of final states (F).
     *
     * @return A string detailing the current configuration of the FSM.
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
                    res.append("\n     δ(").append(sourceState).append(", ").append(symbol).append(") = ").append(targetState);
                });
            });
        });
        res.append("}");

        res.append("\nS=").append(getStartStates());
        res.append("\nF=").append(getFinalStates());

        return res.toString();
    }

    /**
     * Adds all transitions from another FSM to this FSM.
     * Transitions are added from the provided transition map to this FSM's transitions.
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

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void addExplanation(String explanation) {
        this.explanation = this.explanation + explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public Map<State, Set<String>> getTargetStatesAndSymbols(State sourceState) {
        return this.transitions.getOrDefault(sourceState, Map.of());
    }

}
