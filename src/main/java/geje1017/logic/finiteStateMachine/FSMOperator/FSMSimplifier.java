package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import static geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier.copyFsm;

/**
 * Simplifies the representation of a finite state machine (FSM).
 * This is done by renaming the states to make the FSM easier to read and understand without altering its functionality.
 */
public class FSMSimplifier {

    /**
     * Simplifies the given FSM by renaming its states.
     * The simplification process involves assigning new, simplified names to all states in the FSM
     * while preserving the structure and transitions of the original automaton.
     *
     * @param fsm The FSM to be simplified.
     * @return A new FSM structure with simplified state names.
     */
    public static FSMStructure simplify(FSMStructure fsm) {

        FSMStructure simplifiedFSM = copyFsm(fsm);
        simplifiedFSM.setExplanation("Renaming states to make the automaton more readable.\n");

        for (State currstate : simplifiedFSM.getStates()) {
            currstate.simplifyName();
        }
        return simplifiedFSM;
    }

}
