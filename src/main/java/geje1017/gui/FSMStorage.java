package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the storage of FSMs during the evaluation of regular expressions.
 * It stores elementary FSMs and grouped FSMs, providing methods to add FSMs to the storage and retrieve results.
 */
public class FSMStorage {
    private final List<FSMStructure> elementaryFSMs = new ArrayList<>();
    private final List<FSMGroup> fsmGroups = new ArrayList<>();

    /**
     * Adds an elementary FSM to the storage.
     * Elementary FSMs are the initial FSMs created for individual operands in the regular expression.
     *
     * @param fsm The elementary FSM to add.
     */
    public void addElementaryFSM(FSMStructure fsm) {
        elementaryFSMs.add(fsm);
    }

    /**
     * Adds a grouped FSM to the storage.
     * Grouped FSMs represent the FSMs created during operations like concatenation, alternation, or closure.
     *
     * @param group The FSM group to add.
     */
    public void addFSMGroup(FSMGroup group) {
        fsmGroups.add(group);
    }

    // Getter and setter methods

    public List<FSMStructure> getElementaryFSMs() {
        return elementaryFSMs;
    }

    public List<FSMGroup> getFSMGroups() {
        return fsmGroups;
    }

    public FSMStructure getResultFsm() {
        int lastElement = fsmGroups.size() - 1;

        if (lastElement < 0) {
            return elementaryFSMs.get(0);
        }
        return fsmGroups.get(lastElement).getSimplifiedFSM();
    }
}

