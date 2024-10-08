package geje1017.logic.finiteStateMachine;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a state in a finite state machine (FSM).
 * Each state has a unique identifier, and it can be marked as a start state, final state, or new state.
 * States can also be fused with other states, combining their identifiers.
 */
public class State {

    private static int uniqueName = 0;
    private List<Integer> numbers;

    public boolean isFinalState;
    private boolean isStartState;
    private boolean isNew;

    /**
     * Constructs a new state with a unique identifier.
     *
     * @param isStartState Indicates if this is a start state.
     * @param isFinalState Indicates if this is a final state.
     */
    public State(boolean isStartState, boolean isFinalState) {
        this.numbers = new ArrayList<>(Collections.singletonList(uniqueName++));
        this.isStartState = isStartState;
        this.isFinalState = isFinalState;
        this.isNew = false;
    }

    /**
     * Constructs a new state from a list of numbers representing the state's identifier.
     *
     * @param numbers A list of integers representing the state.
     * @param isStartState Indicates if this is a start state.
     * @param isFinalState Indicates if this is a final state.
     */
    public State(List<Integer> numbers, boolean isStartState, boolean isFinalState) {
        this.numbers = new ArrayList<>(numbers);
        this.isStartState = isStartState;
        this.isFinalState = isFinalState;
        this.isNew = false;
    }

    /**
     * Copy constructor to create a new state from an existing state.
     *
     * @param other The state to copy.
     */
    public State(State other) {
        this(other.numbers, other.isStartState, other.isFinalState);
        this.isNew = false;
    }

    /**
     * Static method to fuse multiple states into one by combining their identifiers.
     *
     * @param statesToFuse A set of states to fuse together. Must not be null or empty.
     * @return A new fused state. If only one state is provided, returns that state.
     * @throws IllegalArgumentException If the set of states to fuse is null or empty.
     */
    public static State fuseState(Set<State> statesToFuse) {

        if (statesToFuse == null) {
            throw new IllegalArgumentException("The set of states to fuse must not be null.");
        }
        if (statesToFuse.isEmpty()) {
            throw new IllegalArgumentException("The set of states to fuse must not be empty.");
        }
        if (statesToFuse.size() == 1) {
            return statesToFuse.iterator().next();
        }

        List<Integer> fusedNameNumbers = statesToFuse.stream()
                .flatMap(state -> state.getNumbers().stream())
                .distinct()
                .collect(Collectors.toList());

        boolean isStart = statesToFuse.stream().anyMatch(State::isStartState);
        boolean isFinal = statesToFuse.stream().anyMatch(State::isFinalState);

        State fusedState = new State(fusedNameNumbers, isStart, isFinal);
        fusedState.isNew = true;

        return fusedState;
    }

    /**
     * Fuses the current state with another state by combining their identifiers.
     *
     * @param stateToFuseWith The state to fuse with this state.
     */
    public void fuseStateName(State stateToFuseWith) {
        this.numbers.addAll(stateToFuseWith.getNumbers());
        this.isNew = true;
    }

    // Getter and setter methods

    public List<Integer> getNumbers() {
        return Collections.unmodifiableList(numbers);
    }

    public boolean isStartState() {
        return isStartState;
    }

    public boolean isFinalState() {
        return isFinalState;
    }

    public boolean isNew() {
        return isNew;
    }

    public int getFirstNumber() {
        return numbers.get(0);
    }

    public void resetStartState() {
        isStartState = false;
    }

    public void resetFinalState() {
        isFinalState = false;
    }

    public static void resetUniqueName() {
        uniqueName = 0;
    }

    public void simplifyName() {
        if (numbers.size() > 1) {
            numbers = new ArrayList<>(Collections.singletonList(uniqueName++));
            this.isNew = true;
        }
    }

    public void setStartState(boolean startState) {
        isStartState = startState;
    }

    public void setFinalState(boolean finalState) {
        isFinalState = finalState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State state)) return false;
        return numbers.equals(state.numbers);
    }

    @Override
    public String toString() {
        return numbers.stream()
                .sorted()
                .map(number -> "s" + number)
                .collect(Collectors.joining(""));
    }
}
