package src.main.panels;

import javax.swing.JPanel;

public class PanelHandler {
    public static void switchPanel(JPanel initialPanel, JPanel newPanel) {
        JPanel parent = (JPanel) initialPanel.getParent();
        parent.removeAll();
        parent.add(newPanel);
        parent.revalidate();
        parent.repaint();
        newPanel.requestFocusInWindow();
    }
}
