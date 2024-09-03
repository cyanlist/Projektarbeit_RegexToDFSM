package geje1017.logic.postfix;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages input processing by identifying and categorizing different types
 * of symbols in expressions, particularly focusing on operators and special symbols
 * like empty symbol and empty set.
 */
public class InputManager {

    /**
     * Enumerates different types of operators with their symbols and precedence levels.
     */
    public enum OperatorType {
        ALTERNATION('|', 1),
        CONCATENATION(',', 2),
        KLEENE_CLOSURE('*', 3),
        POSITIVE_CLOSURE('+', 3),
        PARENTHESIS_OPEN('(', 4),
        PARENTHESIS_CLOSE(')', 4);

        private final char symbol;
        private final int priority;

        OperatorType(char symbol, int priority) {
            this.symbol = symbol;
            this.priority = priority;
        }

        public char getSymbol() {
            return symbol;
        }

        public int getPriority() {
            return priority;
        }
    }

    // Map to associate character symbols with their corresponding OperatorType.
    private static final Map<Character, OperatorType> operatorMap = new HashMap<>();

    // Populates the map with all operator types.
    static {
        for (OperatorType op : OperatorType.values()) {
            operatorMap.put(op.getSymbol(), op);
        }
    }

    /**
     * Determines if a character is an operand, which includes letters, digits,
     * the empty symbol (epsilon), and the empty set.
     *
     * @param c the character to check
     * @return true if the character is an operand, false otherwise
     */
    public static boolean isOperand(char c) {
        return Character.isLetterOrDigit(c)
                || c == InputManager.getEmptySymbol()
                || c == InputManager.getEmptySet();
    }

    /**
     * Returns the symbol used to represent the empty string in expressions.
     *
     * @return the empty symbol character
     */
    public static char getEmptySymbol() {
        return 'ε';
    }

    /**
     * Returns the symbol used to represent the empty set in expressions.
     *
     * @return the empty set character
     */
    public static char getEmptySet() {
        return 'Ø';
    }

    /**
     * Checks if a given character is an operator by checking its presence in the operator map.
     *
     * @param c the character to check
     * @return true if the character is an operator, false otherwise
     */
    public static boolean isOperator(char c) {
        return operatorMap.containsKey(c);
    }

    /**
     * Determines if the character is a unary operator, specifically Kleene closure or positive closure.
     *
     * @param c the character to check
     * @return true if the character is a unary operator, false otherwise
     */
    public static boolean isUnaryOperator(char c) {
        return c == OperatorType.KLEENE_CLOSURE.getSymbol()
                || c == OperatorType.POSITIVE_CLOSURE.getSymbol();
    }

    /**
     * Retrieves the priority of an operator character.
     *
     * @param operator the operator character whose priority is to be retrieved
     * @return the priority of the operator
     * @throws IllegalArgumentException if the character is not a valid operator
     */
    public static int getPriority(char operator) {
        OperatorType opType = operatorMap.get(operator);
        if (opType != null) {
            return opType.getPriority();
        }
        throw new IllegalArgumentException("Invalid operator: " + operator);
    }

    /**
     * Compares the priority of two operators and determines if the first has equal or higher priority than the second.
     *
     * @param op1 the first operator character
     * @param op2 the second operator character
     * @return true if the first operator has equal or higher priority, false otherwise
     */
    public static boolean hasEqualOrHigherPriority(char op1, char op2) {
        return getPriority(op1) >= getPriority(op2);
    }
}
