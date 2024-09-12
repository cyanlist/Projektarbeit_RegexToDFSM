package geje1017.logic.postfix;

/**
 * Provides a utility to validate the syntactic correctness of an infix expression.
 * This class checks the expression against a set of predefined rules,
 * ensuring that it contains only allowed characters, balanced parentheses,
 * correctly placed operators, and adheres to special syntax rules.
 */
public abstract class ExpressionValidator {

    private static final int maxLengthExpression = 30;

    /**
     * Validates an entire infix expression using multiple helper methods to check
     * different aspects of the expression's syntax.
     *
     * @param expression The expression to validate.
     * @throws InvalidExpressionException If any part of the expression violates the syntax rules.
     */
    public static void validateInfix(String expression) throws InvalidExpressionException {
        checkLength(expression);
        expression = ExpressionConverter.removeEmptyParentheses(expression);
        validateCharacters(expression);
        validateParenthesesBalance(expression);
        validateUnaryOperatorPlacement(expression);
        validateBinaryOperatorPlacement(expression);
    }

    private static void checkLength(String expression) throws InvalidExpressionException {
        if (expression.length() > maxLengthExpression) {throw new InvalidExpressionException("Expression is too long");};
    }

    /**
     * Validates that the expression contains only allowed characters.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If the expression contains illegal characters.
     */
    private static void validateCharacters(String expression) throws InvalidExpressionException {
        for (char c : expression.toCharArray()) {
            if (!InputManager.isOperand(c)
                    && !InputManager.isOperator(c)) {
                throw new InvalidExpressionException("Invalid character in expression: " + c);
            }
        }
    }

    /**
     * Checks that all parentheses in the expression are balanced.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If the parentheses are unbalanced.
     */
    private static void validateParenthesesBalance(String expression) throws InvalidExpressionException {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance < 0) {
                    throw new InvalidExpressionException("More closing than opening parentheses.");
                }
            }
        }
        if (balance != 0) {
            throw new InvalidExpressionException("Unbalanced parentheses.");
        }
    }

    /**
     * Ensures that operators are placed correctly within the expression,
     * such as not starting with an operator unless permitted by syntax rules.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If operators are misplaced.
     */
    private static void validateUnaryOperatorPlacement(String expression) throws InvalidExpressionException {
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // Prüfen, ob es ein unary operator ist
            if (InputManager.isUnaryOperator(c)) {
                // Prüfen auf unzulässige Positionen (nach '(' oder '|')
                if (i > 0) {
                    char previousChar = expression.charAt(i - 1);
                    if (previousChar == '(' || previousChar == '|') {
                        throw new InvalidExpressionException("Invalid unary operator placement after " + previousChar);
                    }
                }

                // Überprüfung, ob der Ausdruck mit einem Unary-Operator beginnt
                if (i == 0) {
                    throw new InvalidExpressionException("Expression cannot start with unary operator: " + c);
                }
            }
        }
    }

    /**
     * Checks for violations of special syntax rules, such as consecutive '|' characters,
     * or '|' characters at the beginning or end of the expression.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If special syntax rules are violated.
     */
    private static void validateBinaryOperatorPlacement(String expression) throws InvalidExpressionException {
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // Prüfen, ob es ein binärer Operator ist
            if (InputManager.isOperator(c) && !InputManager.isUnaryOperator(c)) {

                // Überprüfen auf unzulässige Platzierungen: Doppelte '|' oder am Anfang/Ende des Ausdrucks
                if (i == 0 || i == expression.length() - 1) {
                    throw new InvalidExpressionException("Expression cannot start or end with binary operator: " + c);
                }

                // Prüfen, ob der vorherige oder nächste Charakter auch ein Operator ist
                char previousChar = expression.charAt(i - 1);
                char nextChar = expression.charAt(i + 1);

                if (InputManager.isOperator(previousChar)
                        || InputManager.isOperator(nextChar)) {
                    throw new InvalidExpressionException("Invalid binary operator placement near: " + c);
                }
            }
        }
    }

    /**
     * Custom exception class for invalid expressions. This provides more specific error
     * information than a generic exception class.
     */
    public static class InvalidExpressionException extends Exception {
        public InvalidExpressionException(String message) {
            super(message);
        }
    }
}
