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

    private final int OVAL_SIZE_MIN = 50;

    private int ovalSize;  // OVAL_SIZE ist jetzt eine Instanzvariable
    private final FSMStructure fsm;
    private final Map<State, Point> statePositions;
    private StateRepresentation stateRepresentation;
    private TransitionRepresentation transitionRepresentation;

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

        int cols = (int) Math.ceil(Math.sqrt(states.size()));
        int rows = (int) Math.ceil((double) states.size() / cols);

        int availableWidth = Frame.TRY.width / 3;
        this.ovalSize = availableWidth / (cols * 2 + 1);

        if (this.ovalSize < OVAL_SIZE_MIN) {
            this.ovalSize = OVAL_SIZE_MIN;
        }

        int cellWidth = this.ovalSize * 2;
        int cellHeight = this.ovalSize * 2;

        Map<State, Point> positions = new HashMap<>();

        for (int i = 0; i < states.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int x = col * cellWidth + this.ovalSize;
            int y = row * cellHeight + this.ovalSize;
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

        statePositions.forEach((state, position) -> stateRepresentation.draw(state, position, fsm));

        fsm.getTransitions().forEach((source, targets) -> {
            targets.forEach((target, symbols) -> {
                transitionRepresentation.draw(statePositions.get(source), statePositions.get(target), symbols);
            });
        });
    }

    /**
     * Handles the visual representation of a state.
     */
    private static class StateRepresentation {

        private final int ovalSize;
        private final Graphics2D g2;

        /**
         * Constructor to initialize the state representation with a specific oval size.
         * @param ovalSize The size of the state oval.
         */
        public StateRepresentation (Graphics2D g2, int ovalSize) {
            this.ovalSize = ovalSize;
            this.g2 = g2;
        }

        /**
         * Draws a state at the specified position on the canvas.
         *
         * @param state   The state to draw.
         * @param position The position where the state should be drawn.
         * @param fsm     The FSM structure to which the state belongs.
         */
        public void draw(State state, Point position, FSMStructure fsm) {
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
         * Draws a smaller inner oval for final states.
         *
         * @param position The position of the state.
         */
        private void drawInnerOval(Point position) {
            int innerOvalSize = (int) (ovalSize * 0.8);
            g2.drawOval(position.x - innerOvalSize / 2, position.y - innerOvalSize / 2, innerOvalSize, innerOvalSize);
        }

        /**
         * Draws the label (state name) inside the oval.
         *
         * @param state   The state whose name will be displayed.
         * @param position The position where the label should be drawn.
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
     * Handles the visual representation of a transition between states.
     */
    private static class TransitionRepresentation {

        private final int ovalSize;
        private final Graphics2D g2;

        /**
         * Constructor to initialize the transition representation with a specific oval size.
         * @param ovalSize The size of the state ovals.
         */
        public TransitionRepresentation (Graphics2D g2, int ovalSize) {
            this.ovalSize = ovalSize;
            this.g2 = g2;
        }

        /**
         * Draws a transition between two states.
         *
         * @param from    The starting point of the transition.
         * @param to      The ending point of the transition.
         * @param inputSymbols The input symbols that trigger the transition.
         */
        public void draw(Point from, Point to, Set<String> inputSymbols) {
            // Unterscheidung der Pfeilarten
            if (from.equals(to)) {
                // Schleife (loop)
                drawLoop(from, inputSymbols);
            } else if (isStraightLine(from, to)) {
                // Gerader Pfeil
                drawStraightArrow(from, to, inputSymbols);
            } else {
                // Gebogener Pfeil (curve)
                drawCurvedArrow(from, to, inputSymbols);
            }
        }

        public void drawStartArrow(Point to) {
            g2.setColor(Color.BLACK);
            Point from = new Point(to.x - this.ovalSize / 3, to.y);
            to = new Point(to.x - this.ovalSize, to.y);
            drawStraightArrow(from, to, Collections.emptySet());
        }

        /**
         * Prüft, ob die Linie zwischen zwei Punkten eine gerade Linie ist.
         * Der Zielzustand muss entweder genau rechts oder direkt unterhalb des Startzustands liegen.
         *
         * @param from Der Startpunkt.
         * @param to Der Endpunkt.
         * @return true, wenn es eine gerade Linie ist (rechts oder unterhalb), ansonsten false.
         */
        private boolean isStraightLine(Point from, Point to) {
            // Der Zustand liegt genau rechts, wenn y gleich bleibt und x des Endzustands größer ist
            boolean isRight = (from.y == to.y
                    && to.x > from.x
                    && to.x - from.x == 2 * this.ovalSize);
            // Der Zustand liegt genau unterhalb, wenn x gleich bleibt und y des Endzustands größer ist
            boolean isBelow = to.y > from.y;

            System.out.println(to.y + "==" + from.y + ", isBelow=" + isBelow);
            System.out.println(to.x + "-" + from.x + "=" + (to.x - from.x) + ", isRight=" + isRight);

            return isRight || isBelow;
        }

        /**
         * Zeichnet eine gerade Linie zwischen zwei Punkten.
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
         * Zeichnet einen gebogenen Pfeil zwischen zwei Punkten.
         */
        private void drawCurvedArrow(Point from, Point to, Set<String> inputSymbols) {
            g2.setColor(Color.BLACK);

            Point adjustedFrom = adjustPoint(from, to);
            Point adjustedTo = adjustPoint(to, from);

            int offset = (int) (ovalSize * 0.75);

            // Kontrollpunkt anpassen, wenn die Punkte auf derselben Y-Ebene liegen
            Point controlPoint;
            if (from.y == to.y) {
                // Verschiebe den Kontrollpunkt nach oben oder unten, um eine sichtbare Krümmung zu erzeugen
                controlPoint = new Point((from.x + to.x) / 2, from.y - offset); // z.B. nach oben verschieben
            } else {
                // Normale Berechnung des Kontrollpunkts für eine Kurve
                controlPoint = new Point((from.x + to.x) / 2 + offset, (from.y + to.y) / 2);
            }

            // Zeichne die Kurve
            QuadCurve2D curve = new QuadCurve2D.Float();
            curve.setCurve(adjustedFrom.x, adjustedFrom.y, controlPoint.x, controlPoint.y, adjustedTo.x, adjustedTo.y);
            g2.draw(curve);


            double angle = Math.atan2(adjustedTo.y - controlPoint.y, adjustedTo.x - controlPoint.x);
            drawArrowhead(adjustedTo, angle);
            drawArrowLabel(inputSymbols, controlPoint, controlPoint);
        }

        /**
         * Zeichnet eine Schleife (Loop) für einen Zustand.
         */
        private void drawLoop(Point center, Set<String> inputSymbols) {
            g2.setColor(Color.BLACK);

            Point controlPoint = new Point(center.x, center.y - ovalSize + ovalSize/6);

            QuadCurve2D q = new QuadCurve2D.Float();
            q.setCurve(center.x - 5, center.y - ovalSize / 2, controlPoint.x, controlPoint.y, center.x + 5, center.y - ovalSize / 2);
            g2.draw(q);

            double angle = Math.atan2(ovalSize * 2, 5);
            drawArrowhead(new Point(center.x + 5, center.y - ovalSize / 2), angle);
            drawArrowLabel(inputSymbols, controlPoint, controlPoint);
        }

        /**
         * Zeichnet den Pfeilkopf.
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
         * Zeichnet ein Label für den Pfeil.
         */
        private void drawArrowLabel(Set<String> inputSymbols, Point from, Point to) {
            g2.setColor(Color.BLACK);
            String input = String.join(", ", inputSymbols);
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(input);

            int midX = (from.x + to.x) / 2;
            int midY = (from.y + to.y) / 2;

            double offsetFactor = 0.25;  // Faktor, der die Verschiebung definiert (z.B. 25% der Strecke)
            int adjustedX = (int) (midX + offsetFactor * (from.x - to.x));
            int adjustedY = (int) (midY + offsetFactor * (from.y - to.y));

            // Zeichne das Label am verschobenen Punkt
            g2.drawString(input, adjustedX - width / 2, adjustedY + fm.getAscent() / 2);
        }


        /**
         * Passt einen Punkt basierend auf dem Winkel zwischen zwei Punkten und der ovalSize an.
         */
        private Point adjustPoint(Point from, Point to) {
            double angle = Math.atan2(to.y - from.y, to.x - from.x);
            return new Point((int) (from.x + ovalSize / 2 * Math.cos(angle)), (int) (from.y + ovalSize / 2 * Math.sin(angle)));
        }
    }

}
