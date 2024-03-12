package src.main.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import src.assets.ImageLoader;

public class AboutPanel extends JPanel {
    public static AboutPanel ap = new AboutPanel();

    private static BufferedImage[] images = ImageLoader.loadAboutMenu();
    
    private static int page = 0;

    private AboutPanel() {
        setPreferredSize(new Dimension(800, 600));
        setFocusTraversalKeysEnabled(false);

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_TAB) {
                    page++;
                    if (page > 1) {
                        page = 0;
                    }
                }

                if (keyCode == KeyEvent.VK_ESCAPE) {
                    if (GamePanel.isCurrentState("Main menu: About")) {
                        PanelHandler.switchPanel(AboutPanel.getInstance(), MainMenuPanel.getInstance());
                        GamePanel.gameState = "Main menu";
                    }
                    if (GamePanel.isCurrentState("Playing: About")) {
                        PanelHandler.switchPanel(AboutPanel.getInstance(), GamePanel.getInstance());
                        GamePanel.gameState = "Playing";
                    }
                }
                repaint();
            }
        };
        addKeyListener(keyAdapter);
        setFocusable(true);
    }

    public static AboutPanel getInstance() {
        return ap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(images[page], 0, -15, null);
    }
}
