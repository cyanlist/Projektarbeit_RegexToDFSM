package geje1017.gui.customGuiElements;

import geje1017.gui.Frame;
import geje1017.logic.postfix.ExpressionValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Extends {@link JTextField} to include placeholder text functionality.
 * It displays a placeholder text when the field is empty and automatically validates the input as the user types.
 */
public class TextFieldWithPlaceholder extends JTextField {

    private final String placeholder;
    private boolean showingPlaceholder;
    private final geje1017.gui.Frame parentFrame;

    /**
     * Constructs a {@code TextFieldWithPlaceholder} with the specified placeholder text.
     * The text field also performs input validation and updates the parent frame's error label as needed.
     *
     * @param placeholder The placeholder text to display when the text field is empty.
     * @param frame       The parent {@code Frame} where this text field is located.
     */
    public TextFieldWithPlaceholder(String placeholder, Frame frame) {
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        this.setText(placeholder);
        this.setForeground(Color.GRAY);
        this.parentFrame = frame; // Frame-Referenz speichern
        showPlaceholderIfNeeded();
        addPlaceholderListeners();
    }

    /**
     * Adds listeners to handle focus, mouse, and key events to manage the placeholder behavior.
     * The listeners ensure the placeholder is displayed when the text field is empty, and the placeholder is cleared when the user types.
     */
    private void addPlaceholderListeners() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                showPlaceholderIfNeeded();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPlaceholderIfNeeded();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearPlaceholder();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateTextAppearance();
                showPlaceholderIfNeeded();
            }
        });
    }

    /**
     * Clears the placeholder text when the user starts typing, making the field ready for input.
     * It also changes the text color to black to indicate active input.
     */
    private void clearPlaceholder() {
        if (showingPlaceholder) {
            setText("");
            setForeground(Color.BLACK);
            showingPlaceholder = false;
        }
    }

    /**
     * Displays the placeholder text if the text field is empty and has lost focus.
     * It sets the text color to gray and resets the caret position.
     */
    private void showPlaceholderIfNeeded() {
        if (getText().isEmpty()) {
            setText(placeholder);
            setForeground(Color.GRAY);
            showingPlaceholder = true;
            setCaretPosition(0);
        }
    }

    /**
     * Updates the text field's appearance based on the validity of the input.
     * If the input is valid, the text color is set to black. If the input is invalid, it is set to red,
     * and an error message is displayed in the parent frame's error label.
     */
    private void updateTextAppearance() {
        String text = getText();
        if (text != null && !text.isEmpty() && !showingPlaceholder) {
            try {
                ExpressionValidator.validateInfix(text);
                setForeground(Color.BLACK);
                parentFrame.setErrorLabel("");  // Kein Fehler, also leeres Label
            } catch (ExpressionValidator.InvalidExpressionException e) {
                setForeground(Color.RED);
                parentFrame.setErrorLabel(e.getMessage());  // Fehlernachricht ins Label schreiben
            }
        }
    }

    // Getter and setter methods

    @Override
    public String getText() {
        return showingPlaceholder ? "" : super.getText().trim();
    }
}
