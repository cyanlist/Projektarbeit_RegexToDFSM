package geje1017.gui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Frame extends JFrame {

    private static final String DEFAULT_REGEX = "i.e.: (a|1+)z*";
    private static final Dimension MIN_SIZE = new Dimension(800, 500);
    private static final Dimension PREF_SIZE = new Dimension(1280, 720);

    public static Dimension TRY;

    private static int margin = 100;

    private JPanel mainPanel;
    private JPanel inputPanel;
    public TextFieldWithPlaceholder textField;
    public JLabel errorLabel;
    private JButton convertButton;
    public JPanel resultPanel;
    public JPanel solutionPanel;

    public JPanel defaultPanel;

    private JScrollPane scrollPane;
    private JButton toggleSolutionButton;

    public Frame() {
        this.initializeFrame();
        this.initializeComponents();
        this.layoutComponents();
        this.configureScrollBehavior();
        this.setVisible(true);
    }

    private void initializeFrame() {
        this.setMinimumSize(MIN_SIZE);
        this.setPreferredSize(PREF_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 400);
        this.setLocationRelativeTo(null);
        this.mainPanel = new JPanel(new GridBagLayout());
        add(this.mainPanel);

        margin = this.getWidth() / 8;
    }

    private void initializeComponents() {
        this.inputPanel = new JPanel(new GridBagLayout());
        this.textField = new TextFieldWithPlaceholder(DEFAULT_REGEX, this);
        this.convertButton = new JButton("Convert");

        this.errorLabel = new JLabel();
        this.errorLabel.setForeground(Color.RED);

        this.scrollPane = new JScrollPane();

        this.inputPanel.setBorder(new EmptyBorder(20, margin, 0, margin));
        setupScrollPane();
    }

    public void setupScrollPane() {
        if (!textField.getText().trim().isEmpty()) {

            this.resultPanel = new JPanel();
            this.resultPanel.setLayout(new BoxLayout(this.resultPanel, BoxLayout.Y_AXIS));

            this.solutionPanel = new JPanel();
            this.solutionPanel.setLayout(new BoxLayout(this.solutionPanel, BoxLayout.Y_AXIS));
            this.solutionPanel.setVisible(false);

            this.toggleSolutionButton = new JButton("Show Solution Path");
            this.toggleSolutionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.toggleSolutionButton.addActionListener(e -> toggleSolutionPath());

            JPanel scrollContent = new JPanel();
            scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
            scrollContent.add(this.resultPanel);
            scrollContent.add(Box.createRigidArea(new Dimension(0, 10)));
            scrollContent.add(this.toggleSolutionButton);
            scrollContent.add(Box.createRigidArea(new Dimension(0, 10)));
            scrollContent.add(this.solutionPanel);

            this.scrollPane.setViewportView(scrollContent);
        }
        else {
            JPanel scrollContent = new JPanel();
            this.scrollPane.setViewportView(scrollContent);
        }



        System.out.println("scrollPane: " + scrollPane.getWidth());
        this.scrollPane.setBorder(new EmptyBorder(0, margin, 20, margin));
        TRY = scrollPane.getSize();
        System.out.println("Try: " + TRY.width);
    }

    private void layoutComponents() {
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.insets = new Insets(0, 5, 0, 5);

        JLabel label = new JLabel("Enter your regular expression here:");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        inputGbc.gridx = 0;
        inputGbc.gridy = 0;
        inputGbc.gridwidth = 2;
        inputGbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(label, inputGbc);

        inputGbc.gridx = 0;
        inputGbc.gridy = 1;
        inputGbc.gridwidth = 1;
        inputGbc.weightx = 1;
        inputGbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(textField, inputGbc);

        inputGbc.gridx = 1;
        inputGbc.gridy = 1;
        inputGbc.gridwidth = 1;
        inputGbc.weightx = 0;
        inputGbc.fill = GridBagConstraints.NONE;
        inputPanel.add(convertButton, inputGbc);

        inputGbc.gridx = 0;
        inputGbc.gridy = 2;
        inputGbc.gridwidth = 2;
        // inputGbc.weightx = 1;
        inputGbc.fill = GridBagConstraints.WEST;
        inputPanel.add(errorLabel, inputGbc);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(inputPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.mainPanel.add(this.scrollPane, gbc);

        this.convertButton.addActionListener(new Controller(this));
        this.textField.addKeyListener(new Controller(this));

        Dimension buttonSize = this.convertButton.getPreferredSize();
        this.textField.setPreferredSize(new Dimension(this.textField.getPreferredSize().width, buttonSize.height));
    }

    private void configureScrollBehavior() {
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar verticalScrollBar = this.scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(10);
        verticalScrollBar.setBlockIncrement(45);
    }

    private void toggleSolutionPath() {
        boolean isSolutionVisible = solutionPanel.isVisible();
        this.solutionPanel.setVisible(!isSolutionVisible);
        this.toggleSolutionButton.setText(isSolutionVisible ? "Show Solution Path" : "Hide Solution Path");
        revalidate();
        repaint();
    }

    public void setErrorLabel(String message) {
        errorLabel.setText(message);
        errorLabel.setForeground(message.isEmpty() ? Color.BLACK : Color.RED);
    }

    public String getInputFieldText() {
        return this.textField.getText().trim();
    }
}