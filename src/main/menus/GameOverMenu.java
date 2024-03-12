package src.main.menus;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import src.main.KeyHandler;
import src.assets.ImageLoader;
import src.main.UserInterface;
import src.main.panels.GamePanel;
import src.main.panels.MainMenuPanel;
import src.main.panels.PanelHandler;
import src.entities.sim.Sim;
import src.world.World;

public class GameOverMenu {
    private static BufferedImage[] images = ImageLoader.loadGameOverMenu();
    private static BufferedImage gameOverBox = images[0];
    private static BufferedImage escBox = images[1];
    private static BufferedImage continueableTitleBox = images[2];

    // Attributes
    private static String deathMessage = "";

    public static void setDeathMessage(String deathMessage) {
        GameOverMenu.deathMessage = deathMessage;
    }

    public static void update() {
        World world = UserInterface.getWorld();
        ArrayList<Sim> listOfSims = world.getListOfSim();

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
            if (listOfSims.isEmpty()) {
                PanelHandler.switchPanel(GamePanel.getInstance(), MainMenuPanel.getInstance());
            }
            else {
                UserInterface.showGameOver();
                UserInterface.viewListOfSims();
            }
        }
    }

    public static void draw(Graphics2D g) {
        World world = UserInterface.getWorld();
        ArrayList<Sim> listOfSims = world.getListOfSim();

        g.setColor(new Color(110, 196, 213));
        g.fillRect(0, 0, 800, 600);

        g.drawImage(gameOverBox, 71, 104, null);
        g.drawImage(escBox, 309, 546, null);

        if (!listOfSims.isEmpty()) {
            g.drawImage(continueableTitleBox, 237, 104, null);
        }

        Font font = new Font("Inter", Font.PLAIN, 12);
        g.setFont(font);
        g.setColor(new Color(61, 30, 45));

        UserInterface.drawCenteredText(g, gameOverBox, 71, 434, deathMessage, font);
    }
}
