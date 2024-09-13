package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

// Angepasste FSMGroup Klasse
public class FSMGroup {
    private final FSMStructure operationFSM;
    private final FSMStructure deterministicFSM;
    private final FSMStructure minimizedFSM;
    private final FSMStructure simplifiedFSM;

    // Konstruktor und Getter
    public FSMGroup(FSMStructure op, FSMStructure det, FSMStructure min, FSMStructure simple) {
        this.operationFSM = op;
        this.deterministicFSM = det;
        this.minimizedFSM = min;
        this.simplifiedFSM = simple;
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

    public FSMStructure getSimplifiedFSM() {
        return simplifiedFSM;
    }
}
