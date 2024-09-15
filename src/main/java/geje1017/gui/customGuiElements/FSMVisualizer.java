package geje1017.gui.customGuiElements;

import geje1017.gui.Frame;
import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import java.util.List;

/**
 * Visualizes a finite state machine (FSM) by rendering its states and transitions.
 * Each state is represented as an oval, and transitions between states are depicted as arrows.
 * The FSM structure is laid out dynamically, and states can be styled based on their properties.
 */
public class FSMVisualizer extends JPanel {

    private final int OVAL_SIZE_MIN = 50;

    private final int ovalSize;
    private final FSMStructure fsm;
    private final Map<State, Point> statePositions;
    private StateRepresentation stateRepresentation;
    private TransitionRepresentation transitionRepresentation;

    /**
     * Constructs a visualizer for the given FSM structure.
     * Initializes the FSM, calculates the oval size, and computes state positions.
     *
     * @param fsm The FSM structure to visualize.
     */
    public FSMVisualizer(FSMStructure fsm) {
        this.fsm = fsm;
        this.ovalSize = calculateOvalSize();
        this.statePositions = calculatePositions();
    }

    /**
     * Calculates the appropriate size for the ovals representing states based on
     * the number of states and available canvas space.
     *
     * @return The calculated size for the state ovals.
     */
    private int calculateOvalSize() {
        int availableWidth = Frame.TRY.width / 3;
        int cols = (int) Math.ceil(Math.sqrt(fsm.getStates().size()));
        int calculatedSize = availableWidth / (cols * 2 + 1);

        return Math.max(calculatedSize, OVAL_SIZE_MIN);
    }

    /**
     * Calculates the positions of each state on the canvas. States are laid out
     * in a grid dynamically, with their positions calculated based on the oval size.
     *
     * @return A map associating each state with a specific position (Point) on the canvas.
     */
    private Map<State, Point> calculatePositions() {
        List<State> states = new ArrayList<>(fsm.getStates());

        int cols = (int) Math.ceil(Math.sqrt(states.size()));
        int rows = (int) Math.ceil((double) states.size() / cols);

        int cellWidth = ovalSize * 2;
        int cellHeight = ovalSize * 2;

        Map<State, Point> positions = new HashMap<>();

        for (int i = 0; i < states.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int x = col * cellWidth + ovalSize;
            int y = row * cellHeight + ovalSize;
            positions.put(states.get(i), new Point(x, y));
        }

        setPreferredSize(new Dimension(cols * cellWidth, rows * cellHeight));
        setMaximumSize(new Dimension(cols * cellWidth, rows * cellHeight));
        setMinimumSize(new Dimension(cols * cellWidth, rows * cellHeight));

        return positions;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.stateRepresentation = new StateRepresentation(g2, ovalSize);
        this.transitionRepresentation = new TransitionRepresentation(g2, ovalSize);

        statePositions.forEach((state, position) -> stateRepresentation.draw(state, position));

        fsm.getTransitions().forEach((source, targets) -> targets.forEach((target, symbols) -> {
            transitionRepresentation.draw(statePositions.get(source), statePositions.get(target), symbols);
        }));
    }

    /**
     * Handles the visual representation of a state, including drawing its oval,
     * label (state name), and any additional markers for final or start states.
     */
    private static class StateRepresentation {

        private final int ovalSize;
        private final Graphics2D g2;

        /**
         * Initializes the state representation with the specified oval size and graphics context.
         *
         * @param g2       The graphics context used for drawing.
         * @param ovalSize The size of the oval representing the state.
         */
        public StateRepresentation (Graphics2D g2, int ovalSize) {
            this.ovalSize = ovalSize;
            this.g2 = g2;
        }

        /**
         * Draws a state at the specified position on the canvas.
         * The state is drawn as an oval, with different styles for new, final, and start states.
         *
         * @param state    The state to draw.
         * @param position The position on the canvas where the state should be drawn.
         */
        public void draw(State state, Point position) {
            g2.setColor(state.isNew() ? Color.RED : Color.BLACK);
            g2.drawOval(position.x - ovalSize / 2, position.y - ovalSize / 2, ovalSize, ovalSize);
            drawStateLabel(state, position);

            if (state.isFinalState()) {
                drawInnerOval(position);
            }

            if (state.isStartState()) {
                new TransitionRepresentation(g2, ovalSize).drawStartArrow(position);
            }
        }

        /**
         * Draws a smaller inner oval to indicate that the state is a final state.
         *
         * @param position The position of the state on the canvas.
         */
        private void drawInnerOval(Point position) {
            int innerOvalSize = (int) (ovalSize * 0.8);
            g2.drawOval(position.x - innerOvalSize / 2, position.y - innerOvalSize / 2, innerOvalSize, innerOvalSize);
        }

        /**
         * Draws the label (state name) inside the oval.
         *
         * @param state    The state whose name will be displayed.
         * @param position The position on the canvas where the label should be drawn.
         */
        private void drawStateLabel(State state, Point position) {
            String stateName = state.toString();

            int fontSize = (int) (ovalSize * 0.2);
            Font font = new Font("Arial", Font.PLAIN, fontSize);

            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            int stringWidth = fm.stringWidth(stateName);

            g2.drawString(stateName, position.x - stringWidth / 2, position.y + fm.getAscent() / 2 - fm.getDescent() / 2);
        }
    }

    /**
     * Handles the visual representation of transitions between states.
     * Transitions can be drawn as straight arrows, curved arrows, or loops, depending
     * on the relationship between the states.
     */
    private static class TransitionRepresentation {

        private final int ovalSize;
        private final Graphics2D g2;

        /**
         * Initializes the transition representation with the specified oval size and graphics context.
         *
         * @param g2       The graphics context used for drawing.
         * @param ovalSize The size of the ovals representing the states.
         */
        public TransitionRepresentation (Graphics2D g2, int ovalSize) {
            this.ovalSize = ovalSize;
            this.g2 = g2;
        }

        /**
         * Draws a transition (arrow) between two states. The transition can either
         * be a straight arrow, a curved arrow, or a loop, depending on the positions of the states.
         *
         * @param from         The starting point of the transition.
         * @param to           The ending point of the transition.
         * @param inputSymbols The input symbols that trigger the transition.
         */
        public void draw(Point from, Point to, Set<String> inputSymbols) {
            if (from.equals(to)) {
                drawLoop(from, inputSymbols);
            } else if (isStraightLine(from, to)) {
                drawStraightArrow(from, to, inputSymbols);
            } else {
                drawCurvedArrow(from, to, inputSymbols);
            }
        }

        /**
         * Draws an arrow indicating the starting state of the FSM.
         *
         * @param to The position of the start state.
         */
        public void drawStartArrow(Point to) {
            g2.setColor(Color.BLACK);
            Point from = new Point(to.x - this.ovalSize / 3, to.y);
            to = new Point(to.x - this.ovalSize, to.y);
            drawStraightArrow(from, to, Collections.emptySet());
        }

        /**
         * Checks if the line between two points is a straight line (either horizontal or vertical).
         * The target state must either be directly to the right or directly below the source state.
         *
         * @param from The starting point of the transition.
         * @param to   The ending point of the transition.
         * @return true if it's a straight line, otherwise false.
         */
        private boolean isStraightLine(Point from, Point to) {
            boolean isRight = (from.y == to.y
                    && to.x > from.x
                    && to.x - from.x == 2 * this.ovalSize);
            boolean isBelow = to.y > from.y;

            return isRight || isBelow;
        }

        /**
         * Draws a straight arrow between two states.
         *
         * @param from         The starting point of the arrow.
         * @param to           The ending point of the arrow.
         * @param inputSymbols The input symbols that trigger the transition.
         */
        private void drawStraightArrow(Point from, Point to, Set<String> inputSymbols) {
            g2.setColor(Color.BLACK);
            Point adjustedFrom = adjustPoint(from, to);
            Point adjustedTo = adjustPoint(to, from);

            g2.drawLine(adjustedFrom.x, adjustedFrom.y, adjustedTo.x, adjustedTo.y);
            double angle = Math.atan2(adjustedTo.y - adjustedFrom.y, adjustedTo.x - adjustedFrom.x);
            drawArrowhead(adjustedTo, angle);
            drawArrowLabel(inputSymbols, adjustedFrom, adjustedTo);
        }

        /**
         * Draws a curved arrow between two states.
         *
         * @param from         The starting point of the arrow.
         * @param to           The ending point of the arrow.
         * @param inputSymbols The input symbols that trigger the transition.
         */
        private void drawCurvedArrow(Point from, Point to, Set<String> inputSymbols) {
            g2.setColor(Color.BLACK);

            Point adjustedFrom = adjustPoint(from, to);
            Point adjustedTo = adjustPoint(to, from);

            int offset = (int) (ovalSize * 0.8);

            Point controlPoint;
            if (from.y == to.y) {
                controlPoint = new Point((from.x + to.x) / 2, from.y - offset);
            } else {
                controlPoint = new Point((from.x + to.x) / 2 + offset, (from.y + to.y) / 2);
            }

            QuadCurve2D curve = new QuadCurve2D.Float();
            curve.setCurve(adjustedFrom.x, adjustedFrom.y, controlPoint.x, controlPoint.y, adjustedTo.x, adjustedTo.y);
            g2.draw(curve);

            double angle = Math.atan2(adjustedTo.y - controlPoint.y, adjustedTo.x - controlPoint.x);
            drawArrowhead(adjustedTo, angle);
            drawArrowLabel(inputSymbols, controlPoint, controlPoint);
        }

        /**
         * Draws a loop (self-transition) on a state.
         *
         * @param center       The position of the state.
         * @param inputSymbols The input symbols that trigger the transition.
         */
        private void drawLoop(Point center, Set<String> inputSymbols) {
            g2.setColor(Color.BLACK);

            Point controlPoint = new Point(center.x, center.y - ovalSize + ovalSize/6);
            int ovalSizeHalf = ovalSize / 2;
            int offset = 5;

            QuadCurve2D q = new QuadCurve2D.Float();
            q.setCurve(center.x - offset, center.y - ovalSizeHalf, controlPoint.x, controlPoint.y, center.x + offset, center.y - ovalSizeHalf);
            g2.draw(q);

            double angle = Math.atan2(ovalSize * 2, 5);
            drawArrowhead(new Point(center.x + 5, center.y - ovalSize / 2), angle);
            drawArrowLabel(inputSymbols, controlPoint, controlPoint);
        }

        /**
         * Draws an arrowhead on the end of a transition.
         *
         * @param tip   The point where the arrowhead is to be drawn.
         * @param angle The angle of the arrowhead.
         */
        private void drawArrowhead(Point tip, double angle) {
            g2.setColor(Color.BLACK);
            int arrowLength = ovalSize / 8;
            int x1 = (int) (tip.x - arrowLength * Math.cos(angle + Math.PI / 6));
            int y1 = (int) (tip.y - arrowLength * Math.sin(angle + Math.PI / 6));
            int x2 = (int) (tip.x - arrowLength * Math.cos(angle - Math.PI / 6));
            int y2 = (int) (tip.y - arrowLength * Math.sin(angle - Math.PI / 6));
            g2.drawLine(tip.x, tip.y, x1, y1);
            g2.drawLine(tip.x, tip.y, x2, y2);
        }

        /**
         * Draws the label for a transition (the input symbols that trigger the transition).
         *
         * @param inputSymbols The input symbols.
         * @param from         The starting point of the transition.
         * @param to           The ending point of the transition.
         */
        private void drawArrowLabel(Set<String> inputSymbols, Point from, Point to) {
            g2.setColor(Color.BLACK);
            String input = String.join(", ", inputSymbols);
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(input);

            int midX = (from.x + to.x) / 2;
            int midY = (from.y + to.y) / 2;

            double offsetFactor = 0.25;
            int adjustedX = (int) (midX + offsetFactor * (from.x - to.x));
            int adjustedY = (int) (midY + offsetFactor * (from.y - to.y));

            g2.drawString(input, adjustedX - width / 2, adjustedY + fm.getAscent() / 2);
        }


        /**
         * Adjusts the position of a point based on the oval size and the angle between two points.
         *
         * @param from The source point.
         * @param to   The target point.
         * @return A new point adjusted based on the oval size.
         */
        private Point adjustPoint(Point from, Point to) {
            double angle = Math.atan2(to.y - from.y, to.x - from.x);
            int ovalSizeHalf = ovalSize / 2;

            return new Point((int) (from.x + ovalSizeHalf * Math.cos(angle)), (int) (from.y + ovalSizeHalf * Math.sin(angle)));
        }
    }

}
