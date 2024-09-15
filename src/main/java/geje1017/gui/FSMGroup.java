package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

/**
 * Represents a group of finite state machines (FSMs) that are created during the processing of a regular expression.
 * This group includes the FSM generated from the original operation, the deterministic FSM,
 * the minimized FSM, and the simplified FSM.
 */
public class FSMGroup {
    private final FSMStructure operationFSM;
    private final FSMStructure deterministicFSM;
    private final FSMStructure minimizedFSM;
    private final FSMStructure simplifiedFSM;

    /**
     * Constructs an {@code FSMGroup} that holds the FSMs created during the evaluation process.
     *
     * @param op The FSM generated from the original operation.
     * @param det The deterministic FSM.
     * @param min The minimized FSM.
     * @param simple The simplified FSM.
     */
    public FSMGroup(FSMStructure op, FSMStructure det, FSMStructure min, FSMStructure simple) {
        this.operationFSM = op;
        this.deterministicFSM = det;
        this.minimizedFSM = min;
        this.simplifiedFSM = simple;
    }

    // Getter and setter methods

    public FSMStructure getOperationFSM() {
        return operationFSM;
    }

    public FSMStructure getDeterministicFSM() {
        return deterministicFSM;
    }

    public FSMStructure getMinimizedFSM() {
        return minimizedFSM;
    }

    public FSMStructure getSimplifiedFSM() {
        return simplifiedFSM;
    }
}
