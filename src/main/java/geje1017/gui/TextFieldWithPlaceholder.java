package geje1017.gui;

import geje1017.logic.postfix.ExpressionValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextFieldWithPlaceholder extends JTextField {

    private final String placeholder;
    private boolean showingPlaceholder;

    public TextFieldWithPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        this.setText(placeholder);
        this.setForeground(Color.GRAY);
        showPlaceholderIfNeeded();
        addPlaceholderListeners();
    }

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

    private void clearPlaceholder() {
        if (showingPlaceholder) {
            setText("");
            setForeground(Color.BLACK);
            showingPlaceholder = false;
        }
    }

    private void showPlaceholderIfNeeded() {
        if (getText().isEmpty()) {
            setText(placeholder);
            setForeground(Color.GRAY);
            showingPlaceholder = true;
            setCaretPosition(0);
        }
    }

    private void updateTextAppearance() {
        String text = getText();
        if (text != null && !text.isEmpty() && !showingPlaceholder) {
            try {
                ExpressionValidator.validateInfix(text);
                setForeground(Color.BLACK);
            } catch (ExpressionValidator.InvalidExpressionException e) {
                setForeground(Color.RED);
            }
        }
    }


    @Override
    public String getText() {
        return showingPlaceholder ? "" : super.getText().trim();
    }
}
