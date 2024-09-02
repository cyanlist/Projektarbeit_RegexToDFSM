package regToDEA.main.gui;

import regToDEA.main.logic.finiteStateMachine.FSMStructure;

import java.util.ArrayList;
import java.util.List;

// Neue Klasse FSMStorage, die als Container für alle FSMs dient
public class FSMStorage {
    private final List<FSMStructure> elementaryFSMs = new ArrayList<>();
    private final List<FSMGroup> fsmGroups = new ArrayList<>();

    public void addElementaryFSM(FSMStructure fsm) {
        elementaryFSMs.add(fsm);
    }

    public void addFSMGroup(FSMGroup group) {
        fsmGroups.add(group);
    }

    public List<FSMStructure> getElementaryFSMs() {
        return elementaryFSMs;
    }

    public List<FSMGroup> getFSMGroups() {
        return fsmGroups;
    }

    public FSMStructure getResultFsm() {
        int lastElement = fsmGroups.size() - 1;
        return fsmGroups.get(lastElement).getMinimizedFSM();
    }
}

