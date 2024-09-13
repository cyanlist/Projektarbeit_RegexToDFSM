package geje1017.logic.finiteStateMachine.FSMOperator;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import static geje1017.logic.finiteStateMachine.FSMOperator.FSMCopier.copyFsm;

public class FSMSimplifier {

    public static FSMStructure simplify(FSMStructure fsm) {

        FSMStructure copyFsm = copyFsm(fsm);

        for (State currstate : copyFsm.getStates()) {
            currstate.simplifyName();
        }

        copyFsm = copyFsm(copyFsm);

        return copyFsm;
    }

}
