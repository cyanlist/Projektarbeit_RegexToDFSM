package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import static geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier.copyFsm;
import static geje1017.logic.finiteStateMachine.FSMOperator.FSMSymbolConverter.convertEmptyString;

public class FSMClosureApplier{

    /**
     * Applies positive closure (also known as "plus" closure) to a deterministic finite state machine (FSM).
     * The positive closure of an FSM allows it to repeat its pattern one or more times.
     *
     * @param fsm The deterministic FSM to apply positive closure to.
     * @return A new FSM instance representing the original FSM with positive closure applied.
     */
    public static FSMStructure applyPositiveClosure(FSMStructure fsm) {

        FSMStructure copyFsm = copyFsm(fsm);

        FSMStructure positiveClosureFsm = new FSMStructure();

        positiveClosureFsm.addAllTransitions(copyFsm.getTransitions());
        State startStatesFsm = copyFsm.getStartState();

        copyFsm.getTransitions().forEach((sourceState, transition) -> transition.forEach((targetState, inputSymbol) -> {
            if (targetState.isFinalState()) {
                positiveClosureFsm.addTransition(sourceState, inputSymbol, startStatesFsm);
            }
        }));

        positiveClosureFsm.setExpression("(" + copyFsm.getExpression() + ")+");
        return positiveClosureFsm;
    }

    /**
     * Applies Kleene closure to a deterministic FSM.
     * Kleene closure (denoted as '*') allows the FSM to repeat its sequence zero or more times,
     * effectively including the option to accept the empty string.
     *
     * @param fsm The deterministic FSM to apply Kleene closure to.
     * @return A new FSM instance representing the original FSM with Kleene closure applied.
     */
    public static FSMStructure applyKleeneClosure(FSMStructure fsm) {

        String temp = "(" + fsm.getExpression() + ")*";

        FSMStructure kleeneClosureFsm = applyPositiveClosure(fsm);
        convertEmptyString(kleeneClosureFsm);

        kleeneClosureFsm.setExpression(temp);
        return kleeneClosureFsm;

    }

}
