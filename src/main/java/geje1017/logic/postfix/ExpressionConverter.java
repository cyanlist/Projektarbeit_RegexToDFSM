package geje1017.logic.postfix;

import java.util.Stack;

/**
 * Converts infix expressions to postfix notation.
 * The conversion process includes handling operators, operands, parentheses,
 * and inserting implicit concatenation operators where needed.
 */
public abstract class ExpressionConverter {

    /**
     * Converts an infix expression to its equivalent postfix expression.
     * The method processes the infix expression by removing whitespace, replacing special symbols,
     * and inserting necessary concatenation operators before converting it to postfix notation.
     *
     * @param infix The infix expression to be converted.
     * @return The resulting postfix expression.
     */
    public static String convertInfixToPostfix(String infix) {

        infix = prepareInfixExpression(infix);
        String postfix = processInfixToPostfix(infix);

        return postfix.trim();
    }

    /**
     * Prepares the infix expression by removing whitespaces, replacing special symbols,
     * and inserting implicit concatenation operators where needed.
     *
     * @param infix The original infix expression.
     * @return The cleaned and prepared infix expression.
     */
    private static String prepareInfixExpression(String infix) {
        infix = infix.replace("\\e", "ε");
        infix = infix.replace("\\0", "Ø");
        infix = infix.replaceAll("\\s+", "");  // Remove white spaces

        infix = removeEmptyParentheses(infix);
        return insertImplicitConcatenation(infix);  // Insert commas where needed
    }

    /**
     * Inserts implicit concatenation operators (',') in the infix expression.
     * It checks for situations where concatenation is implied between characters.
     *
     * @param expression The infix expression.
     * @return The modified infix expression with explicit concatenation operators.
     */
    private static String insertImplicitConcatenation(String expression) {
        if (expression.isEmpty()) return expression;

        StringBuilder result = new StringBuilder();
        char[] chars = expression.toCharArray();

        for (int i = 0; i < chars.length - 1; i++) {
            char current = chars[i];
            char next = chars[i + 1];
            result.append(current);

            // Check for cases where implicit concatenation should be inserted:
            if (shouldInsertConcatenation(current, next)) {
                result.append(','); // Append concatenation symbol
            }
        }
        result.append(chars[chars.length - 1]); // Add the last character to handle edge cases
        return result.toString();
    }

    /**
     * Determines whether a concatenation operator should be inserted between two characters.
     * Concatenation is inserted between operands, between an operand and an opening parenthesis,
     * between a closing parenthesis and an operand, and between two parentheses (when the first is closing and the second is opening).
     *
     * @param current The current character in the expression.
     * @param next The next character in the expression.
     * @return {@code true} if a concatenation operator should be inserted, otherwise {@code false}.
     */
    private static boolean shouldInsertConcatenation(char current, char next) {
        return (InputManager.isOperand(current) && InputManager.isOperand(next)) ||
                (InputManager.isOperand(current) && next == InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()) ||
                (current == InputManager.OperatorType.PARENTHESIS_CLOSE.getSymbol() && InputManager.isOperand(next)) ||
                (current == InputManager.OperatorType.PARENTHESIS_CLOSE.getSymbol() && next == InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()) ||
                (InputManager.isUnaryOperator(current) && (InputManager.isOperand(next) || next == InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()));
    }

    /**
     * Removes empty parentheses from the infix expression.
     *
     * @param expression The expression that may contain empty parentheses.
     * @return The expression with all empty parentheses removed.
     */
    public static String removeEmptyParentheses(String expression) {
        String previousExpression;
        do {
            previousExpression = expression;
            expression = expression.replace("()", "");
        } while (!expression.equals(previousExpression));
        return expression;
    }

    /**
     * Processes the infix expression and converts it into postfix notation.
     * This method handles operators, parentheses, and operator precedence during the conversion.
     *
     * @param infix The infix expression to be converted.
     * @return The converted postfix expression.
     */
    private static String processInfixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (InputManager.isOperand(c)) { // Operand
                postfix.append(c).append(' ');
            } else if (c == InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()) { // Left parenthesis
                stack.push(c);
            } else if (c == InputManager.OperatorType.PARENTHESIS_CLOSE.getSymbol()) { // Right parenthesis
                while (!stack.isEmpty() && stack.peek() != InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.pop(); // Remove the '(' from stack
            } else if (InputManager.isOperator(c)) { // Operator
                while (!stack.isEmpty()
                        && InputManager.hasEqualOrHigherPriority(stack.peek(), c)
                        && stack.peek() != InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.toString().trim();
    }

}
