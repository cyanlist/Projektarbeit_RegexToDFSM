package geje1017.logic.finiteStateMachine.Operation;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.Map;
import java.util.Set;

public class FSMAlternator {

    public static FSMStructure alternate(FSMStructure fsm1, FSMStructure fsm2) {
        FSMStructure alternatedFsm = new FSMStructure();

        // Neuer Startzustand
        State newStartState = new State(true, false); // true für Startzustand, false für Endzustand
        alternatedFsm.addTransition(newStartState, null, null);

        // Füge den neuen Startzustand Übergänge hinzu
        initializeStartStateTransitions(alternatedFsm, newStartState, fsm1);
        initializeStartStateTransitions(alternatedFsm, newStartState, fsm2);

        // Füge alle Übergänge aus beiden FSMs hinzu
        alternatedFsm.addAllTransitions(fsm1.getTransitions());
        alternatedFsm.addAllTransitions(fsm2.getTransitions());

        // Setze den Ausdruck
        alternatedFsm.setExpression(fsm1.getExpression() + "|" + fsm2.getExpression());
        return alternatedFsm;
    }

    private static void initializeStartStateTransitions(FSMStructure resultFsm, State newStartState, FSMStructure fsm) {
        Set<State> startStates = fsm.getStartStates();
        for (State startState : startStates) {
            // Gehe durch alle Übergänge der Startzustände
            Map<State, Set<String>> startTransitions = fsm.getTransitions().get(startState);
            if (startTransitions != null) {
                for (Map.Entry<State, Set<String>> transition : startTransitions.entrySet()) {
                    // Füge für jedes Eingabesymbol einen Übergang vom neuen Startzustand zum Zielzustand hinzu
                    resultFsm.addTransition(newStartState, transition.getValue(), transition.getKey());
                }
            }
        }
    }

}
