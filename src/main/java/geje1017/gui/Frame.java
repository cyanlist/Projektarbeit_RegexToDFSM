package geje1017.gui;

import geje1017.gui.customGuiElements.TextFieldWithPlaceholder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Represents the main window of the application, providing the user interface
 * for inputting regular expressions and displaying the resulting FSMs. It sets up the layout, components,
 * and event listeners, as well as handles interactions between the GUI and the application logic.
 */
public class Frame extends JFrame {

    private static final String DEFAULT_REGEX = "e.g.: (0|\\0)+cd\\e";
    private static final Dimension MIN_SIZE = new Dimension(800, 500);
    private static final Dimension PREF_SIZE = new Dimension(1280, 720);

    public static int SCROLL_PANE_WIDTH;
    private int margin = 100;

    private JPanel mainPanel;
    private JPanel inputPanel;
    public TextFieldWithPlaceholder textField;
    public JLabel errorLabel;
    private JButton convertButton;
    public JPanel resultPanel;
    public JPanel solutionPanel;
    private JScrollPane scrollPane;
    private JButton toggleSolutionButton;

    /**
     * Constructs a new {@code Frame} and initializes the window, its components, and layout.
     */
    public Frame() {
        this.initializeFrame();
        this.initializeComponents();
        this.layoutComponents();
        this.configureScrollBehavior();
        this.setVisible(true);
    }

    /**
     * Initializes the frame's basic properties, such as size, location, and layout.
     */
    private void initializeFrame() {
        this.setMinimumSize(MIN_SIZE);
        this.setPreferredSize(PREF_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.mainPanel = new JPanel(new GridBagLayout());
        this.add(this.mainPanel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int windowWidth = (int) (screenWidth * 0.8);
        int windowHeight = (int) (screenHeight * 0.8);
        this.margin = windowWidth / 8;
        setSize(windowWidth, windowHeight);

        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    /**
     * Initializes the components used in the frame, including input fields, buttons, labels, and panels.
     */
    private void initializeComponents() {
        this.inputPanel = new JPanel(new GridBagLayout());
        this.textField = new TextFieldWithPlaceholder(DEFAULT_REGEX, this);
        this.convertButton = new JButton("Convert");

        this.errorLabel = new JLabel();
        this.errorLabel.setForeground(Color.RED);

        this.scrollPane = new JScrollPane();

        this.inputPanel.setBorder(new EmptyBorder(20, margin, 0, margin));
        scrollPaneForProcess();

    }

    public void defaultScrollPane() {
        JPanel scrollContent = new JPanel();
        this.scrollPane.setViewportView(scrollContent);
    }

    /**
     * Sets up the scroll pane based on whether the input field contains text.
     * If the input field is empty, it shows a blank panel.
     */
    public void scrollPaneForProcess() {
        this.resultPanel = new JPanel();
        this.resultPanel.setLayout(new BoxLayout(this.resultPanel, BoxLayout.Y_AXIS));
        this.solutionPanel = new JPanel();
        this.solutionPanel.setLayout(new BoxLayout(this.solutionPanel, BoxLayout.Y_AXIS));

        this.toggleSolutionButton = new JButton();
        this.toggleSolutionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.toggleSolutionButton.addActionListener(e -> toggleSolutionPath());
        toggleSolutionPath();

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.add(this.resultPanel);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 10)));
        scrollContent.add(this.toggleSolutionButton);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 30)));
        scrollContent.add(this.solutionPanel);

        this.scrollPane.setViewportView(scrollContent);
        this.scrollPane.setBorder(new EmptyBorder(10, margin, 0, margin));
        this.scrollPane.setMaximumSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
        Frame.SCROLL_PANE_WIDTH = scrollPane.getWidth();
    }

    /**
     * Lays out the components in the frame, using {@link GridBagLayout} for positioning.
     */
    private void layoutComponents() {
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.insets = new Insets(0, 5, 0, 5);

        JLabel label = new JLabel("Enter the regular expression to convert into an automaton here:");
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

    /**
     * Configures the scroll behavior for the result and solution panels, adjusting the scroll speed and visibility settings.
     */
    private void configureScrollBehavior() {
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar verticalScrollBar = this.scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(10);
        verticalScrollBar.setBlockIncrement(45);
    }

    /**
     * Toggles the visibility of the solution panel and updates the button text accordingly.
     */
    private void toggleSolutionPath() {
        boolean isSolutionVisible = solutionPanel.isVisible();
        this.solutionPanel.setVisible(!isSolutionVisible);
        this.toggleSolutionButton.setText(isSolutionVisible ? "Show Solution Path" : "Hide Solution Path");
        revalidate();
        repaint();
    }

    // Getter and setter methods

    public void setErrorLabel(String message) {
        errorLabel.setText(message);
        errorLabel.setForeground(message.isEmpty() ? Color.BLACK : Color.RED);
    }

    public String getInputFieldText() {
        return this.textField.getText().trim();
    }
}