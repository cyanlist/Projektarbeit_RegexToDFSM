package geje1017.logic.postfix;

import geje1017.gui.FSMGroup;
import geje1017.gui.FSMStorage;
import geje1017.logic.finiteStateMachine.FSMOperator.*;
import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import java.util.Stack;

/**
 * Evaluates infix regular expressions by converting them to postfix notation,
 * generating finite state machines (FSMs) for the expression, and performing operations such as concatenation,
 * alternation, and closure.
 */
public class ExpressionEvaluator {

    /**
     * Evaluates an infix regular expression, converts it to postfix notation, and processes it into FSM structures.
     *
     * @param expression The infix regular expression to evaluate.
     * @return An FSMStorage object containing the generated FSM structures for the expression.
     */
    public FSMStorage evaluateExpression(String expression) {
        State.resetUniqueName();
        String postfixExpression = ExpressionConverter.convertInfixToPostfix(expression);
        System.out.println("Postfix Expression: " + postfixExpression);
        return evaluatePostfixExpression(postfixExpression);
    }

    /**
     * Evaluates a postfix expression by generating FSMs for the operands and applying the appropriate operations
     * (concatenation, alternation, or closure) based on the operators.
     *
     * @param postfixExpression The postfix expression to evaluate.
     * @return An FSMStorage object containing the FSM structures.
     */
    private FSMStorage evaluatePostfixExpression(String postfixExpression) {
        FSMStorage storage = new FSMStorage();
        Stack<FSMStructure> stack = new Stack<>();

        for (int i = 0; i < postfixExpression.length(); i++) {
            char currentChar = postfixExpression.charAt(i);
            FSMStructure fsm;

            if (InputManager.isOperator(currentChar)) {
                fsm = processOperator(currentChar, stack);
                if (fsm != null) {
                    FSMStructure deterministicFSM = FSMDeterminizer.toDeterministicFsm(fsm);
                    FSMStructure minimizedFSM = FSMMinimizer.minimize(deterministicFSM);
                    FSMStructure simplifiedFSM = FSMSimplifier.simplify(minimizedFSM);
                    FSMGroup group = new FSMGroup(fsm, deterministicFSM, minimizedFSM, simplifiedFSM);
                    storage.addFSMGroup(group);
                    stack.push(simplifiedFSM);
                }
            } else if (InputManager.isOperand(currentChar)) {
                fsm = FSMSymbolConverter.convertInputCharacter(currentChar);
                stack.push(fsm);
                storage.addElementaryFSM(fsm);
            }
        }

        return storage;
    }

    /**
     * Processes an operator in the postfix expression and applies the corresponding FSM operation.
     *
     * @param operator The operator to process (e.g., concatenation, alternation, closure).
     * @param stack The stack of FSM structures used in the evaluation.
     * @return The resulting FSM after applying the operator.
     */
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
