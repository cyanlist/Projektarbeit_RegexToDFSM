package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import java.util.List;

/**
 * Visualizes a finite state machine (FSM) by rendering its states and transitions.
 */
public class FSMVisualizer extends JPanel {
    private static int OVAL_SIZE = 50;
    private static int PADDING = OVAL_SIZE;  // Padding between states
    private final FSMStructure fsm;
    private final Map<State, Point> statePositions;

    /**
     * Constructs a visualizer for the given FSM structure.
     *
     * @param fsm The FSM structure to visualize.
     */
    public FSMVisualizer(FSMStructure fsm) {
        this.fsm = fsm;
        this.statePositions = calculatePositions();
    }

    /**
     * Calculates positions for each state, laying them out in a grid dynamically.
     *
     * @return A map associating each state with a specific point on the canvas.
     */
    private Map<State, Point> calculatePositions() {
        List<State> states = new ArrayList<>(fsm.getStates());

        // Calculate the number of columns and rows based on the number of states
        int cols = (int) Math.ceil(Math.sqrt(states.size()));
        int rows = (int) Math.ceil((double) states.size() / cols);

        int scrollPaneWidth = (Frame.TRY.width - 100) / 2;

        FSMVisualizer.OVAL_SIZE = cols * OVAL_SIZE * 2 > scrollPaneWidth ? scrollPaneWidth / (cols * 2) : 50;
        FSMVisualizer.PADDING = OVAL_SIZE;

        System.out.println(scrollPaneWidth + " = " + OVAL_SIZE + " * 2 * " + cols);

        // Calculate the size of each "cell" based on the OVAL_SIZE and PADDING
        int cellWidth = OVAL_SIZE + OVAL_SIZE;
        int cellHeight = OVAL_SIZE + OVAL_SIZE;

        // Create a map to store positions
        Map<State, Point> positions = new HashMap<>();

        // Place each state in a grid layout
        for (int i = 0; i < states.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int x = col * cellWidth + OVAL_SIZE;
            int y = row * cellHeight + OVAL_SIZE;
            positions.put(states.get(i), new Point(x, y));
        }

        // Dynamically set the preferred size of the JPanel based on the number of rows and columns
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

        // Draw states
        statePositions.forEach((state, position) -> StateRepresentation.draw(g2, state, position, fsm));

        // Draw transitions
        fsm.getTransitions().forEach((source, targets) -> {
            targets.forEach((target, symbols) -> {
                TransitionRepresentation.draw(g2, statePositions.get(source), statePositions.get(target), symbols);
            });
        });
    }

    /**
     * Handles the visual representation of a state.
     */
    private static class StateRepresentation {

        /**
         * Draws a state at the specified position on the canvas.
         *
         * @param g2      The graphics context.
         * @param state   The state to draw.
         * @param position The position where the state should be drawn.
         * @param fsm     The FSM structure to which the state belongs.
         */
        public static void draw(Graphics2D g2, State state, Point position, FSMStructure fsm) {
            // Set color based on whether the state is new
            g2.setColor(state.isNew() ? Color.RED : Color.BLACK);

            // Draw the state as an oval
            g2.drawOval(position.x - OVAL_SIZE / 2, position.y - OVAL_SIZE / 2, OVAL_SIZE, OVAL_SIZE);

            // Draw the state label inside the oval
            drawStateLabel(g2, state, position);

            // Draw inner oval if the state is a final state
            if (state.isFinalState()) {
                drawInnerOval(g2, position);
            }

            // Draw start arrow if the state is a start state
            if (state.isStartState()) {
                TransitionRepresentation.drawStartArrow(g2, position, new HashSet<>());
            }
        }

        /**
         * Draws a smaller inner oval for final states.
         *
         * @param g2      The graphics context.
         * @param position The position of the state.
         */
        private static void drawInnerOval(Graphics2D g2, Point position) {
            int innerOvalSize = (int) (OVAL_SIZE * 0.8);
            g2.drawOval(position.x - innerOvalSize / 2, position.y - innerOvalSize / 2, innerOvalSize, innerOvalSize);
        }

        /**
         * Draws the label (state name) inside the oval.
         *
         * @param g2      The graphics context.
         * @param state   The state whose name will be displayed.
         * @param position The position where the label should be drawn.
         */
        private static void drawStateLabel(Graphics2D g2, State state, Point position) {
            String stateName = state.toString();
            FontMetrics fm = g2.getFontMetrics();

            int stringWidth = fm.stringWidth(stateName);

            // Adjust label placement to center it inside the oval
            g2.drawString(stateName, position.x - stringWidth / 2, position.y + fm.getAscent() / 2 - fm.getDescent() / 2);
        }
    }

    /**
     * Handles the visual representation of a transition between states.
     */
    private static class TransitionRepresentation {

        /**
         * Draws a transition between two states.
         *
         * @param g2      The graphics context.
         * @param from    The starting point of the transition.
         * @param to      The ending point of the transition.
         * @param inputSymbols The input symbols that trigger the transition.
         */
        public static void draw(Graphics2D g2, Point from, Point to, Set<String> inputSymbols) {
            if (from != null && from.equals(to)) {
                drawLoop(g2, from, inputSymbols);
            } else {
                drawArrow(g2, from, to, inputSymbols);
            }
        }

        /**
         * Draws an arrow from the start point to the end point.
         *
         * @param g2      The graphics context.
         * @param from    The starting point of the arrow.
         * @param to      The ending point of the arrow.
         * @param inputSymbols The input symbols associated with the transition.
         */
        private static void drawArrow(Graphics2D g2, Point from, Point to, Set<String> inputSymbols) {
            g2.setColor(Color.BLACK);
            Point adjustedFrom = adjustPoint(from, to);
            Point adjustedTo = adjustPoint(to, from);

            // Draw the line for the arrow
            g2.drawLine(adjustedFrom.x, adjustedFrom.y, adjustedTo.x, adjustedTo.y);

            // Draw the arrowhead at the end of the arrow
            double angle = Math.atan2(adjustedTo.y - adjustedFrom.y, adjustedTo.x - adjustedFrom.x);
            drawArrowhead(g2, adjustedTo, angle);

            // Draw the label (input symbols) near the arrow
            drawArrowLabel(g2, inputSymbols, adjustedFrom, adjustedTo);
        }

        private static void drawArrowhead(Graphics2D g2, Point tip, double angle) {
            int arrowLength = 10;
            int x1 = (int) (tip.x - arrowLength * Math.cos(angle + Math.PI / 6));
            int y1 = (int) (tip.y - arrowLength * Math.sin(angle + Math.PI / 6));
            int x2 = (int) (tip.x - arrowLength * Math.cos(angle - Math.PI / 6));
            int y2 = (int) (tip.y - arrowLength * Math.sin(angle - Math.PI / 6));
            g2.drawLine(tip.x, tip.y, x1, y1);
            g2.drawLine(tip.x, tip.y, x2, y2);
        }

        private static void drawArrowLabel(Graphics2D g2, Set<String> inputSymbols, Point from, Point to) {
            String input = String.join(", ", inputSymbols);
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(input);
            int midX = (from.x + to.x) / 2;
            int midY = (from.y + to.y) / 2;
            g2.drawString(input, midX - width / 2, midY + fm.getAscent() / 2);
        }

        private static void drawLoop(Graphics2D g2, Point center, Set<String> inputSymbols) {
            QuadCurve2D q = new QuadCurve2D.Float();
            q.setCurve(center.x - 5, center.y - OVAL_SIZE / 2, center.x, center.y - OVAL_SIZE * 2, center.x + 5, center.y - OVAL_SIZE / 2);
            g2.draw(q);

            drawArrowhead(g2, new Point(center.x + 5, center.y - OVAL_SIZE / 2), Math.PI / 2);
            drawArrowLabel(g2, inputSymbols, center, new Point(center.x, center.y - OVAL_SIZE * 2));
        }

        private static Point adjustPoint(Point from, Point to) {
            double angle = Math.atan2(to.y - from.y, to.x - from.x);
            return new Point((int) (from.x + (double) OVAL_SIZE / 2 * Math.cos(angle)), (int) (from.y + (double) OVAL_SIZE / 2 * Math.sin(angle)));
        }

        public static void drawStartArrow(Graphics2D g2, Point position, Set<String> inputSymbols) {
            Point from = new Point(position.x - OVAL_SIZE / 2, position.y);
            Point to = new Point(position.x - OVAL_SIZE, position.y);
            drawArrow(g2, from, to, inputSymbols);
        }
    }
}
