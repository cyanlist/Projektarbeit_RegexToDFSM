package regToDEA.main.gui;

import javax.swing.*;

public class CollapsiblePanelContainer extends JPanel {

        public CollapsiblePanelContainer() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setVisibility(false);
        }

        public void toggleVisibility() {
            this.setVisibility(!isVisible());
        }

        public void setVisibility(boolean isVisible) {
            this.setVisible(isVisible);
            revalidate();
            repaint();
        }
    }
