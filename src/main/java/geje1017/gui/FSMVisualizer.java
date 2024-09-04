package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import java.util.List;

public class FSMVisualizer extends JPanel {
    private static final int OVAL_SIZE = 50;
    private static final int CELL_SIZE = OVAL_SIZE * 2;
    private static final int LOOP_HEIGHT = OVAL_SIZE * 2;

    private final FSMStructure fsm;
    private final Map<State, Point> statePositions;

    public FSMVisualizer(FSMStructure fsm) {
        this.fsm = fsm;
        this.statePositions = calculatePositions();
    }

    private Map<State, Point> calculatePositions() {
        List<State> states = new ArrayList<>(fsm.getStates());
        int cols = (int) Math.ceil(Math.sqrt(states.size()));
        int rows = (int) Math.ceil((double) states.size() / cols);
        Map<State, Point> positions = new HashMap<>();
        for (int i = 0; i < states.size(); i++) {
            int x = (i % cols) * CELL_SIZE + CELL_SIZE / 2;
            int y = ((i / cols) * CELL_SIZE + CELL_SIZE / 2); //+ CELL_SIZE / 8;
            positions.put(states.get(i), new Point(x, y));
        }
        setPreferredSize(new Dimension((cols) * CELL_SIZE, rows * CELL_SIZE));
        return positions;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        statePositions.forEach((state, position) -> StateRepresentation.draw(g2, state, position, fsm));
        fsm.getTransitions().forEach((source, targets) -> {
            targets.forEach((target, symbols) -> {
                TransitionRepresentation.draw(g2, statePositions.get(source), statePositions.get(target), symbols);
            });
        });
    }

    private static class StateRepresentation {
        public static void draw(Graphics2D g2, State state, Point position, FSMStructure fsm) {

            if (state.isNew()) g2.setColor(Color.RED);
            else g2.setColor(Color.BLACK);

            g2.drawOval(position.x - OVAL_SIZE / 2, position.y - OVAL_SIZE / 2, OVAL_SIZE, OVAL_SIZE);

            drawStateLabel(g2, state, position);

            if (state.isFinalState()) {
                drawInnerOval(g2, position);
            }

            if (state.isStartState()) {
                TransitionRepresentation.drawStartArrow(g2, new Point(position.x, position.y), new HashSet<>());
            }
        }

        private static int calculateOvalSize(String stateName, Graphics2D g2) {
            FontMetrics fm = g2.getFontMetrics();
            int nameWidth = fm.stringWidth(stateName);
            return Math.max(OVAL_SIZE, nameWidth + 20);  // Add some padding
        }


        private static void drawInnerOval(Graphics2D g2, Point position) {
            int innerOvalSize = (int) (OVAL_SIZE * 0.6);
            g2.drawOval(position.x - innerOvalSize / 2, position.y - innerOvalSize / 2, innerOvalSize, innerOvalSize);
        }

        private static void drawStateLabel(Graphics2D g2, State state, Point position) {
            String stateName = state.toString();
            FontMetrics fm = g2.getFontMetrics();
            int stringWidth = fm.stringWidth(stateName);

            int ovalDiameter = calculateOvalSize(state.toString(), g2);

            // Adjust font size if state name is too wide
            if (stringWidth > ovalDiameter - 10) {
                float newSize = Math.min(18.0f, (float) (OVAL_SIZE * 18.0 / stringWidth));
                g2.setFont(g2.getFont().deriveFont(newSize));
                fm = g2.getFontMetrics();
                stringWidth = fm.stringWidth(stateName);
            }

            // Truncate text if still too large
            if (stringWidth > ovalDiameter - 10) {
//                stateName = truncateText(stateName, fm, ovalDiameter - 10);
            }

            g2.drawString(stateName, position.x - stringWidth / 2, position.y + fm.getAscent() / 2 - fm.getDescent() / 2);
        }
    }

    private static class TransitionRepresentation {
        public static void draw(Graphics2D g2, Point from, Point to, Set<String> inputSymbols) {
            if (from != null && from.equals(to)) {
                drawLoop(g2, from, inputSymbols);
            } else {
                drawArrow(g2, from, to, inputSymbols);
            }
        }

        private static void drawStartArrow(Graphics2D g2, Point position, Set<String> inputSymbols) {
            Point from = new Point(position.x - OVAL_SIZE / 2, position.y);
            Point to = new Point(position.x - OVAL_SIZE, position.y);
            drawArrow(g2, from, to, inputSymbols);
        }

        private static void drawArrow(Graphics2D g2, Point from, Point to, Set<String> inputSymbols) {
            g2.setColor(Color.BLACK);
            Point adjustedFrom = adjustPoint(from, to);
            Point adjustedTo = adjustPoint(to, from);
            g2.drawLine(adjustedFrom.x, adjustedFrom.y, adjustedTo.x, adjustedTo.y);

            double angle = Math.atan2(adjustedTo.y - adjustedFrom.y, adjustedTo.x - adjustedFrom.x);
            drawArrowhead(g2, adjustedTo, angle);

            drawArrowLabel(g2, inputSymbols, adjustedFrom, adjustedTo);
        }

        private static void drawArrowLabel(Graphics2D g2, Set<String> inputSymbols, Point from, Point to) {
            String input = String.join(", ", inputSymbols); // Join symbols with comma for better readability
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(input);
            int height = fm.getHeight();

            // Calculate midpoint of the line for label placement
            int midX = (from.x + to.x) / 2;
            int midY = (from.y + to.y) / 2;

            // Draw string centered at the adjusted midpoint
            g2.drawString(input, midX - width / 2, midY + height / 2);
        }

        private static void drawLoop(Graphics2D g2, Point center, Set<String> inputSymbols) {
            QuadCurve2D q = new QuadCurve2D.Float();
            q.setCurve(center.x - 5, center.y - OVAL_SIZE / 2, center.x, center.y - LOOP_HEIGHT/2, center.x + 5, center.y - OVAL_SIZE / 2);
            g2.draw(q);

            drawArrowhead(g2, new Point(center.x + 5, center.y - OVAL_SIZE / 2), Math.PI / 2);

            Point temp = new Point(center.x, center.y - OVAL_SIZE*2);

            drawArrowLabel(g2, inputSymbols, center, temp);
        }

        private static void drawLoopLabel(Graphics2D g2, Set<String> inputSymbols, Point center) {
            String input = String.join(", ", inputSymbols);
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(input);

            // Position above the loop
            int labelX = center.x - width / 2;
            int labelY = center.y - LOOP_HEIGHT / 2 - fm.getHeight();

            g2.drawString(input, labelX, labelY);
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

        private static Point adjustPoint(Point from, Point to) {
            double angle = Math.atan2(to.y - from.y, to.x - from.x);
            return new Point((int) (from.x + OVAL_SIZE / 2 * Math.cos(angle)), (int) (from.y + OVAL_SIZE / 2 * Math.sin(angle)));
        }
    }
}
