package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

// Angepasste FSMGroup Klasse
public class FSMGroup {
    private final FSMStructure operationFSM;
    private final FSMStructure deterministicFSM;
    private final FSMStructure minimizedFSM;

    // Konstruktor und Getter
    public FSMGroup(FSMStructure op, FSMStructure det, FSMStructure min) {
        this.operationFSM = op;
        this.deterministicFSM = det;
        this.minimizedFSM = min;
    }

    public FSMStructure getOperationFSM() {
        return operationFSM;
    }

    public FSMStructure getDeterministicFSM() {
        return deterministicFSM;
    }

    public FSMStructure getMinimizedFSM() {
        return minimizedFSM;
    }
}
