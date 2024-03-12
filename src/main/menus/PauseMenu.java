package src.main.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.main.KeyHandler;
import src.main.panels.GamePanel;
import src.main.panels.AboutPanel;
import src.main.panels.MainMenuPanel;
import src.main.panels.PanelHandler;

public class PauseMenu {
    private static int selectedBox = 0;

    private static BufferedImage[] images = ImageLoader.loadPause();
    private static BufferedImage background = images[0];
    private static BufferedImage about = images[1];
    private static BufferedImage exit = images[2];
    private static BufferedImage aboutHighlighted = images[3];
    private static BufferedImage exitHighlighted = images[4]; 

    private static void boxPressed(){
        if(selectedBox == 0){
            PanelHandler.switchPanel(GamePanel.getInstance(), AboutPanel.getInstance());
            GamePanel.gameState = "Playing: About";
        }
        if(selectedBox == 1){
            // SAVE HERE
            PanelHandler.switchPanel(GamePanel.getInstance(), MainMenuPanel.getInstance());
            GamePanel.gameState = "Main menu";
        }
    }

    public static void update(){
        if(KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)){
            boxPressed();
        }

        int newSelectedBox = selectedBox;
        if(KeyHandler.isKeyPressed(KeyHandler.KEY_S)){
            newSelectedBox++;
        }
        if(KeyHandler.isKeyPressed(KeyHandler.KEY_W)){
            newSelectedBox--;
        }
        if (newSelectedBox >= 0 && newSelectedBox < 2){
            selectedBox = newSelectedBox;
        }
    }
    
    public static void draw(Graphics2D g){
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, 800, 600);

        g.drawImage(background, 233, 60, null); // Background

        g.drawImage(about, 269, 167, null); // Help button
        g.drawImage(exit, 269, 262, null); // Save and Exit button
        
        if(selectedBox == 0){
            g.drawImage(aboutHighlighted, 265, 163, null);
        }
        if(selectedBox == 1){
            g.drawImage(exitHighlighted, 265, 258, null);
        }
    }
}
