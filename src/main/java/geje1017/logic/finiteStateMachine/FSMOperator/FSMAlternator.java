package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.postfix.InputManager;

import static geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier.copyFsm;

/**
 * Alternates two finite state machines (FSM).
 * It combines the transition sets of two FSMs and creates a new FSM that allows transitions from either FSM.
 */
public class FSMAlternator {

    /**
     * Alternates two FSM structures by creating a new FSM that allows inputs from either FSM.
     * It first creates copies of the input FSMs, checks for special cases (like empty sets),
     * and then combines the two FSMs.
     *
     * @param fsm1 the first FSM structure
     * @param fsm2 the second FSM structure
     * @return a new FSM structure that represents the alternation of {@code fsm1} and {@code fsm2}
     */
    public static FSMStructure alternate(FSMStructure fsm1, FSMStructure fsm2) {
        FSMStructure copyFsm1 = copyFsm(fsm1);
        FSMStructure copyFsm2 = copyFsm(fsm2);

        FSMStructure specialCaseResult = checkSpecialCases(fsm1, fsm2);
        if (specialCaseResult != null) {
            return specialCaseResult;
        }
        return performAlternation(copyFsm1, copyFsm2);
    }

    /**
     * Checks for special cases when alternating two FSMs.
     * If one of the FSMs represents the empty set, the method returns the other FSM.
     *
     * @param fsm1 the first FSM structure
     * @param fsm2 the second FSM structure
     * @return {@code fsm2} if {@code fsm1} represents the empty set,
     *         {@code fsm1} if {@code fsm2} represents the empty set, or {@code null} otherwise
     */
    private static FSMStructure checkSpecialCases(FSMStructure fsm1, FSMStructure fsm2) {
        if (fsm1.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
            return fsm2;
        }
        if (fsm2.getExpression().equals(String.valueOf(InputManager.getEmptySet()))) {
            return fsm2;
        }
        return null;
    }

    /**
     * Performs the alternation of two FSMs by combining their transitions and expressions.
     * A new FSM is created with transitions that allow inputs from either {@code fsm1} or {@code fsm2}.
     *
     * @param fsm1 the first FSM structure (copy)
     * @param fsm2 the second FSM structure (copy)
     * @return a new FSM structure representing the alternation of the two FSMs
     */
    private static FSMStructure performAlternation(FSMStructure fsm1, FSMStructure fsm2) {
        FSMStructure alternatedFsm = new FSMStructure();
        alternatedFsm.setExpression("(" + fsm1.getExpression() + "|" + fsm2.getExpression() + ")");
        alternatedFsm.setExplanation("Combining two DFSM by creating transitions that allow inputs from either automaton.\n");

        alternatedFsm.addAllTransitions(fsm1.getTransitions());
        alternatedFsm.addAllTransitions(fsm2.getTransitions());
        return alternatedFsm;
    }

}
