package geje1017.logic.postfix;

import geje1017.gui.FSMGroup;
import geje1017.gui.FSMStorage;
import geje1017.logic.finiteStateMachine.FSMOperator.*;
import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.Stack;

public class ExpressionEvaluator {

    /**
     * Evaluates an infix regular expression and returns an FSMStorage object.
     */
    public FSMStorage evaluateExpression(String expression) {
        State.resetUniqueName();
        String postfixExpression = ExpressionConverter.convertInfixToPostfix(expression);
        System.out.println("Postfix Expression: " + postfixExpression);
        return evaluatePostfixExpression(postfixExpression);
    }

    private FSMStorage evaluatePostfixExpression(String postfixExpression) {
        FSMStorage storage = new FSMStorage();
        Stack<FSMStructure> stack = new Stack<>();

        for (int i = 0; i < postfixExpression.length(); i++) {
            char currentChar = postfixExpression.charAt(i);
            FSMStructure fsm;

            if (InputManager.isOperator(currentChar)) {
                fsm = processOperator(currentChar, stack);
                if (fsm != null) {
                    FSMStructure operationFSM = fsm;
                    FSMStructure deterministicFSM = FSMDeterminizer.toDeterministicFsm(operationFSM);
                    FSMStructure minimizedFSM = FSMMinimizer.minimize(deterministicFSM);
                    FSMGroup group = new FSMGroup(operationFSM, deterministicFSM, minimizedFSM);
                    storage.addFSMGroup(group);
                    stack.push(minimizedFSM);
                }
            } else if (InputManager.isOperand(currentChar)) {
                fsm = FSMSymbolConverter.convertInputCharacter(currentChar);
                stack.push(fsm);
                storage.addElementaryFSM(fsm);
            }
        }

        return storage;
    }

    private FSMStructure processOperator(char operator, Stack<FSMStructure> stack) {
        FSMStructure result = null;
        FSMStructure fsm1, fsm2;

        if (InputManager.OperatorType.KLEENE_CLOSURE.getSymbol() == operator) {
            fsm1 = stack.pop();
            result = FSMClosureApplier.applyKleeneClosure(fsm1);
        } else if (InputManager.OperatorType.POSITIVE_CLOSURE.getSymbol() == operator) {
            fsm1 = stack.pop();
            result = FSMClosureApplier.applyPositiveClosure(fsm1);
        } else if (InputManager.OperatorType.CONCATENATION.getSymbol() == operator) {
            fsm2 = stack.pop();
            fsm1 = stack.pop();
            result = FSMConcatenator.concatenate(fsm1, fsm2);
        } else if (InputManager.OperatorType.ALTERNATION.getSymbol() == operator) {
            fsm2 = stack.pop();
            fsm1 = stack.pop();
            result = FSMAlternator.alternate(fsm1, fsm2);
        }
        return result;
    }
}
