package geje1017.logic.finiteStateMachine.Operation;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import static geje1017.logic.finiteStateMachine.Operation.FSMCopier.copyFsm;
import static geje1017.logic.finiteStateMachine.Operation.FSMSymbolConverter.convertEmptyString;

public class FSMClosureApplier extends FSMOperation{

    /**
     * Applies positive closure to a deterministic FSM.
     * Positive closure means that the FSM can repeat its entire sequence of states one or more times.
     *
     * @param fsm The deterministic FSM to apply positive closure to.
     * @return The FSM after applying positive closure.
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

        System.out.println(positiveClosureFsm);

        positiveClosureFsm.setExpression("(" + copyFsm.getExpression() + ")+");
        return positiveClosureFsm;
    }

    /**
     * Applies Kleene closure to a deterministic FSM.
     *
     * @param fsm The deterministic FSM to apply Kleene closure to.
     * @return The FSM after applying Kleene closure.
     */
    public static FSMStructure applyKleeneClosure(FSMStructure fsm) {

        String temp = fsm.getExpression() + "*";

        FSMStructure kleeneClosureFsm = applyPositiveClosure(fsm);
        convertEmptyString(kleeneClosureFsm);

        kleeneClosureFsm.setExpression(temp);
        return kleeneClosureFsm;

    }

}
