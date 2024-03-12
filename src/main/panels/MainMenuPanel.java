package src.main.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.KeyAdapter;
import javax.swing.JPanel;

import src.assets.ImageLoader;

public class MainMenuPanel extends JPanel {
    public static MainMenuPanel mmp = new MainMenuPanel();

    private int selectedBox = 0; // 0 to 4

    private BufferedImage[] images = ImageLoader.loadMainMenu();

    public MainMenuPanel() {
        setPreferredSize(new Dimension(800, 600));
        setFocusTraversalKeysEnabled(false);

        GamePanel.gameState = "Main menu";

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                // Check if the Enter key was pressed
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (selectedBox == 0) {
                        GamePanel.gameState = "Starting a new game: Creating a new sim";
                        
                        CreateSimPanel.init();
                        PanelHandler.switchPanel(MainMenuPanel.getInstance(), CreateSimPanel.getInstance());
                    }
                    if (selectedBox == 1) {

                        // NOTHING SINCE LOAD HASN'T BEEN IMPLEMENTED
                        
                    }
                    if (selectedBox == 2) {
                        GamePanel.gameState = "Main menu: About";
                        PanelHandler.switchPanel(MainMenuPanel.getInstance(), AboutPanel.getInstance());
                    }
                    if (selectedBox == 3) {
                        System.exit(0);
                    }
                }
                
                int newBox = selectedBox;

                if (keyCode == KeyEvent.VK_W) {
                    newBox -= 2;
                }
                if (keyCode == KeyEvent.VK_A) {
                    newBox--;
                }
                if (keyCode == KeyEvent.VK_S) {
                    newBox += 2;
                }
                if (keyCode == KeyEvent.VK_D) {
                    newBox++;
                }

                if (newBox >= 0 && newBox <= 3) {
                    selectedBox = newBox;
                }
                repaint();
            }
        };
        addKeyListener(keyAdapter);
        setFocusable(true);
    }

    public static MainMenuPanel getInstance() {
        return mmp;
    }

    public static void init() {
        mmp = new MainMenuPanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(images[0], 0, 0, null); // background
        g2.drawImage(images[1], 201, 102, null); // title
        
        // Draw boxes
        g2.drawImage(images[2], 132, 304, null); // start
        g2.drawImage(images[3], 417, 304, null); // load
        g2.drawImage(images[4], 132, 392, null); // about
        g2.drawImage(images[5], 417, 392, null); // exit
        
        // Draw highlighted boxes
        if (selectedBox == 0) g2.drawImage(images[6], 123, 296, null); // start highlighted
        if (selectedBox == 1) g2.drawImage(images[7], 408, 297, null); // load highlighted
        if (selectedBox == 2) g2.drawImage(images[8], 123, 385, null); // about highlighted
        if (selectedBox == 3) g2.drawImage(images[9], 408, 384, null); // exit highlighted

        g2.dispose();
    }
}

