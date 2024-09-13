package geje1017.gui;

import geje1017.logic.finiteStateMachine.FSMStructure;
import geje1017.logic.finiteStateMachine.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
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

    /**
     * Constructs a visualizer for the given FSM structure.
     *
     * @param fsm The FSM structure to visualize.
     */
    public FSMVisualizer(FSMStructure fsm) {
        this.fsm = fsm;
        this.statePositions = calculatePositions(); // Positionen werden hier berechnet
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

        // Berechne die Größe jeder Zelle basierend auf ovalSize und padding
        int cellWidth = this.ovalSize * 2;
        int cellHeight = this.ovalSize * 2;

        // Erstelle eine Map, um die Positionen zu speichern
        Map<State, Point> positions = new HashMap<>();

        // Platziere jeden Zustand in einem Raster
        for (int i = 0; i < states.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int x = col * cellWidth + this.ovalSize;
            int y = row * cellHeight + this.ovalSize;
            positions.put(states.get(i), new Point(x, y));
        }

        // Setze die bevorzugte Größe des JPanel basierend auf den Zeilen und Spalten
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
        statePositions.forEach((state, position) -> StateRepresentation.draw(g2, state, position, fsm, ovalSize));

        // Draw transitions
        fsm.getTransitions().forEach((source, targets) -> {
            targets.forEach((target, symbols) -> {
                TransitionRepresentation.draw(g2, statePositions.get(source), statePositions.get(target), symbols, ovalSize);
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
         * @param ovalSize The size of the state oval.
         */
        public static void draw(Graphics2D g2, State state, Point position, FSMStructure fsm, int ovalSize) {
            // Set color based on whether the state is new
            g2.setColor(state.isNew() ? Color.RED : Color.BLACK);

            // Draw the state as an oval
            g2.drawOval(position.x - ovalSize / 2, position.y - ovalSize / 2, ovalSize, ovalSize);

            // Draw the state label inside the oval
            drawStateLabel(g2, state, position, ovalSize);

            // Draw inner oval if the state is a final state
            if (state.isFinalState()) {
                drawInnerOval(g2, position, ovalSize);
            }

            // Draw start arrow if the state is a start state
            if (state.isStartState()) {
                TransitionRepresentation.drawStartArrow(g2, position, new HashSet<>(), ovalSize);
            }
        }

        /**
         * Draws a smaller inner oval for final states.
         *
         * @param g2      The graphics context.
         * @param position The position of the state.
         * @param ovalSize The size of the outer oval.
         */
        private static void drawInnerOval(Graphics2D g2, Point position, int ovalSize) {
            int innerOvalSize = (int) (ovalSize * 0.8);
            g2.drawOval(position.x - innerOvalSize / 2, position.y - innerOvalSize / 2, innerOvalSize, innerOvalSize);
        }

        /**
         * Draws the label (state name) inside the oval.
         *
         * @param g2      The graphics context.
         * @param state   The state whose name will be displayed.
         * @param position The position where the label should be drawn.
         */
        private static void drawStateLabel(Graphics2D g2, State state, Point position, int ovalSize) {
            String stateName = state.toString();

            // Berechne die Schriftgröße als 40-50% der ovalSize
            int fontSize = (int) (ovalSize * 0.2); // Du kannst diesen Wert anpassen, um die Schriftgröße zu optimieren
            Font font = new Font("Arial", Font.PLAIN, fontSize);  // Setze die Schriftart und die berechnete Größe

            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            int stringWidth = fm.stringWidth(stateName);

            // Zeichne den Zustandstext zentriert im Oval
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
         * @param ovalSize The size of the state ovals.
         */
        public static void draw(Graphics2D g2, Point from, Point to, Set<String> inputSymbols, int ovalSize) {

            int xLol = Math.max(from.x, to.x) - Math.min(from.x, to.x);
            int yLol = Math.max(from.y, to.y) - Math.min(from.y, to.y);

            System.out.println(xLol + yLol + " " + xLol + " " + yLol + " " + ovalSize);

            if (from.equals(to)) {
                drawLoop(g2, from, inputSymbols, ovalSize); // Schleife zeichnen, wenn from == to
            }
            else if ((xLol + yLol == 2*ovalSize) ||  (from.y < to.y)) {
                drawStraightArrow(g2, from, to, inputSymbols, ovalSize);
            }
            else {
                drawCurvedArrow(g2, from, to, inputSymbols, ovalSize);
            }
        }

        /**
         * Zeichnet eine gerade Linie zwischen zwei Punkten.
         *
         * @param g2         Der Grafik-Kontext.
         * @param from       Der Startpunkt.
         * @param to         Der Endpunkt.
         * @param inputSymbols Die Eingabesymbole, die den Übergang auslösen.
         * @param ovalSize   Die Größe der Zustands-Ovale.
         */
        private static void drawStraightArrow(Graphics2D g2, Point from, Point to, Set<String> inputSymbols, int ovalSize) {
            g2.setColor(Color.BLACK);

            Point adjustedFrom = adjustPoint(from, to, ovalSize);
            Point adjustedTo = adjustPoint(to, from, ovalSize);

            // Zeichne eine gerade Linie
            g2.drawLine(adjustedFrom.x, adjustedFrom.y, adjustedTo.x, adjustedTo.y);

            // Zeichne den Pfeilkopf am Ende der Linie
            double angle = Math.atan2(adjustedTo.y - adjustedFrom.y, adjustedTo.x - adjustedFrom.x);
            drawArrowhead(g2, adjustedTo, angle);

            // Zeichne das Label (Eingabesymbole) in der Mitte der Linie
            drawArrowLabel(g2, inputSymbols, adjustedFrom, adjustedTo);
        }

        /**
         * Zeichnet einen gebogenen Pfeil von einem Start- zu einem Endpunkt.
         *
         * @param g2         Der Grafik-Kontext.
         * @param from       Der Startpunkt.
         * @param to         Der Endpunkt.
         * @param inputSymbols Die Eingabesymbole, die den Übergang auslösen.
         * @param ovalSize   Die Größe der Zustands-Ovale.
         */
        private static void drawCurvedArrow(Graphics2D g2, Point from, Point to, Set<String> inputSymbols, int ovalSize) {
            g2.setColor(Color.BLACK);

            Point adjustedFrom = adjustPoint(from, to, ovalSize);
            Point adjustedTo = adjustPoint(to, from, ovalSize);

            int offset = (int) (ovalSize * 0.75);

            int midX = (adjustedFrom.x + adjustedTo.x) / 2;
            int midY = (adjustedFrom.y + adjustedTo.y) / 2;
            // Berechne eine Kurve, indem du die Mittelpunkte versetzt
            Point controlPoint = new Point(midX, midY - offset);

            // Zeichne die Kurve
            QuadCurve2D curve = new QuadCurve2D.Float();
            curve.setCurve(adjustedFrom.x, adjustedFrom.y, controlPoint.x, controlPoint.y, adjustedTo.x, adjustedTo.y);
            g2.draw(curve);

            // Draw the arrowhead at the end of the arrow
            double angle = Math.atan2(adjustedTo.y - controlPoint.y, adjustedTo.x - controlPoint.x);
            drawArrowhead(g2, adjustedTo, angle);

            // Draw the label (input symbols) near the arrow
            drawArrowLabel(g2, inputSymbols, controlPoint);
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

            // Faktor zur Verschiebung des Mittelpunkts in Richtung "from"
            double labelOffsetFactor = 0.3;  // 0.5 wäre der genaue Mittelpunkt, kleiner als 0.5 verschiebt in Richtung "from"

            // Berechne den verschobenen Mittelpunkt, um das Label näher an "from" zu platzieren
            int labelX = (int) (from.x + labelOffsetFactor * (to.x - from.x));
            int labelY = (int) (from.y + labelOffsetFactor * (to.y - from.y));

            g2.drawString(input, labelX - width / 2, labelY + fm.getAscent() / 2);
        }

        private static void drawArrowLabel(Graphics2D g2, Set<String> inputSymbols, Point from) {

            String input = String.join(", ", inputSymbols);
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(input);

            // Zeichne den Text zentriert in Bezug auf die berechnete Position
            g2.drawString(input, from.x - width / 2, from.y + fm.getAscent() * 2);
        }


        private static void drawLoop(Graphics2D g2, Point center, Set<String> inputSymbols, int ovalSize) {

            int curveSize = (int) (ovalSize * 0.5);

            int x = center.x;
            int y = center.y + ovalSize / 2;
            int curveY = y + curveSize;
            int offset = 5;

            QuadCurve2D q = new QuadCurve2D.Float();
            q.setCurve(x - offset, y, x, curveY, x + offset, y);
            g2.draw(q);

            double angle = Math.atan2(y - curveY, 0);

            drawArrowhead(g2, new Point(center.x + 5, center.y + ovalSize / 2), angle);
            drawArrowLabel(g2, inputSymbols, center, new Point(center.x, center.y + (int) (ovalSize*2.5)));
        }

        private static Point adjustPoint(Point from, Point to, int ovalSize) {
            double angle = Math.atan2(to.y - from.y, to.x - from.x);
            return new Point((int) (from.x + (double) ovalSize / 2 * Math.cos(angle)), (int) (from.y + (double) ovalSize / 2 * Math.sin(angle)));
        }

        public static void drawStartArrow(Graphics2D g2, Point position, Set<String> inputSymbols, int ovalSize) {
            Point from = new Point(position.x - ovalSize / 2, position.y);
            Point to = new Point(position.x - ovalSize, position.y);
            drawStraightArrow(g2, from, to, inputSymbols, ovalSize);
        }
    }
}
