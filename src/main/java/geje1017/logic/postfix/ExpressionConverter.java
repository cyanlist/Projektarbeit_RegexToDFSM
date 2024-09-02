package regToDEA.main.logic.postfix;

import java.util.Stack;

/**
 * This class provides methods to convert infix expressions to postfix notation using InputManager.
 */
public class ExpressionConverter {

    /**
     * Converts an infix expression to a postfix expression.
     *
     * @param infix The infix expression to be converted.
     * @return The resulting postfix expression.
     */
    public static String infixToPostfix(String infix) {

        // if (!ExpressionValidator.isValidInfix(infix)) throw new IllegalArgumentException("Invalid infix: " + infix);

        infix = infix.replace("\\e", "ε");
        infix = infix.replace("\\0", "Ø");

        infix = infix.replaceAll("\\s+", ""); // Remove white spaces
        infix = removeEmptyParentheses(infix);
        System.out.println(infix + " " + infix.length());
        infix = insertImplicitConcatenation(infix); // Insert commas where needed

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

        while (!stack.isEmpty()) { // Clear the stack
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.toString().trim();
    }

    /**
     * Inserts implicit concatenation operators (',') where needed in the infix expression.
     * It checks for scenarios where concatenation is assumed between characters.
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
     * Determines if a concatenation operator should be inserted between two characters.
     *
     * @param current The current character in the expression.
     * @param next The next character in the expression.
     * @return true if concatenation should be inserted, false otherwise.
     */
    private static boolean shouldInsertConcatenation(char current, char next) {
        // Concatenation is necessary between two operands (e.g., "ab"), between an operand and an opening parenthesis (e.g., "a("),
        // between a closing parenthesis and an operand (e.g., ")a"), and between two parentheses if the first is closing and the second is opening (e.g., ")(").
        return (InputManager.isOperand(current) && InputManager.isOperand(next)) ||
                (InputManager.isOperand(current) && next == InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()) ||
                (current == InputManager.OperatorType.PARENTHESIS_CLOSE.getSymbol() && InputManager.isOperand(next)) ||
                (current == InputManager.OperatorType.PARENTHESIS_CLOSE.getSymbol() && next == InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()) ||
                (InputManager.isUnaryOperator(current) && (InputManager.isOperand(next) || next == InputManager.OperatorType.PARENTHESIS_OPEN.getSymbol()));
    }

    /**
     * Removes empty parentheses pairs from the expression efficiently.
     *
     * @param expression The original expression with potential empty parentheses.
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

}
