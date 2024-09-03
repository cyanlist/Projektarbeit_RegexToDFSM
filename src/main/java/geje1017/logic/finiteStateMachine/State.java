package geje1017.logic.finiteStateMachine;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a state in an automaton, with unique identification and properties to define its role in transitions.
 */
public class State {

    private static int uniqueName = 0;
    private final List<Integer> numbers;
    // TODO: Temporary public
    public boolean isFinalState;
    private boolean isStartState;
    private boolean isNew = false;

    // Constructor for a standard state with unique name generation
    public State(boolean isStartState, boolean isFinalState) {
        this.numbers = new ArrayList<>(Collections.singletonList(uniqueName++));
        this.isStartState = isStartState;
        this.isFinalState = isFinalState;
    }

    // Constructor for creating a state from a set of numbers
    public State(List<Integer> numbers, boolean isStartState, boolean isFinalState) {
        this.numbers = new ArrayList<>(numbers);
        this.isStartState = isStartState;
        this.isFinalState = isFinalState;
    }

    // Copy constructor
    public State(State other) {
        this.numbers = new ArrayList<>(other.numbers);
        this.isStartState = other.isStartState;
        this.isFinalState = other.isFinalState;
        this.isNew = other.isNew;
    }

    // Static method to fuse multiple states into one
    public static State fuseState(Set<State> statesToFuse) {
        List<Integer> fusedNameNumbers = statesToFuse.stream()
                .flatMap(state -> state.getNumbers().stream())
                .distinct()
                .collect(Collectors.toList());

        boolean isStart = statesToFuse.stream().anyMatch(State::isStartState);
        boolean isFinal = statesToFuse.stream().anyMatch(State::isFinalState);
        State fusedState = new State(fusedNameNumbers, isStart, isFinal);
        if (statesToFuse.size() > 1) fusedState.isNew = true;
        return fusedState;
    }

    public List<Integer> getNumbers() {
        return numbers;
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

    // Reset methods for properties
    public void resetStartState() {
        isStartState = false;
    }

    public void resetFinalState() {
        isFinalState = false;
    }

    public static void resetUniqueName() {
        uniqueName = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return numbers.equals(state.numbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numbers);
    }

    @Override
    public String toString() {
        return numbers.stream()
                .sorted()
                .map(number -> "q" + number)
                .collect(Collectors.joining(""));
    }
}
