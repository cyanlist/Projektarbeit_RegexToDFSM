package regToDEA.main.logic.postfix;

import regToDEA.main.gui.FSMGroup;
import regToDEA.main.gui.FSMStorage;
import regToDEA.main.logic.finiteStateMachine.FSMStructure;
import regToDEA.main.logic.finiteStateMachine.FSMOperation;
import regToDEA.main.logic.finiteStateMachine.State;

import java.util.Stack;

public class ExpressionEvaluator {

    /**
     * Evaluates an infix regular expression and returns an FSMStorage object.
     */
    public FSMStorage evaluateExpression(String expression) {
        State.resetUniqueName();
        String postfixExpression = ExpressionConverter.infixToPostfix(expression);
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
                    FSMStructure deterministicFSM = FSMOperation.toDeterministicFsm(operationFSM);
                    FSMStructure minimizedFSM = FSMOperation.minimize(deterministicFSM);
                    FSMGroup group = new FSMGroup(operationFSM, deterministicFSM, minimizedFSM);
                    storage.addFSMGroup(group);
                    stack.push(minimizedFSM);
                }
            } else if (InputManager.isOperand(currentChar)) {
                fsm = FSMOperation.convertInputCharacter(currentChar);
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
            result = FSMOperation.applyKleeneClosure(fsm1);
        } else if (InputManager.OperatorType.POSITIVE_CLOSURE.getSymbol() == operator) {
            fsm1 = stack.pop();
            result = FSMOperation.applyPositiveClosure(fsm1);
        } else if (InputManager.OperatorType.CONCATENATION.getSymbol() == operator) {
            fsm2 = stack.pop();
            fsm1 = stack.pop();
            result = FSMOperation.concatenate(fsm1, fsm2);
        } else if (InputManager.OperatorType.ALTERNATION.getSymbol() == operator) {
            fsm2 = stack.pop();
            fsm1 = stack.pop();
            result = FSMOperation.alternate(fsm1, fsm2);
        }
        return result;
    }
}
