package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.entities.sim.Sim;
import src.main.Consts;
import src.main.UserInterface;
import src.world.House;
import src.world.World;

public class WorldMenu {
    private static BufferedImage[] images = ImageLoader.loadWorldMenu();
    private static BufferedImage simPreviewBox = images[0];
    private static BufferedImage houseInfoBox = images[1];
    private static BufferedImage helpBox = images[2];
    private static BufferedImage help = images[3];

    private static World world;
    private static Sim currentSim;
    private static House currentHouse ;

    public static void draw(Graphics2D g) {
        world = UserInterface.getWorld();

        if (UserInterface.isHelped()) {
            g.drawImage(help, 0, 0, 800, 570, null);
        }

        drawInfoBoxes(g);

        drawTexts(g);

        world.draw(g);
    }

    private static void drawInfoBoxes(Graphics2D g) {
        currentSim = UserInterface.getCurrentSim();
        currentHouse = currentSim.getCurrentHouse();

        // world border
        g.setColor(new Color(61, 30, 45));
        g.fillRect(23, 23, Consts.TILE_SIZE * 32 + 6, Consts.TILE_SIZE * 32 + 6);

        g.drawImage(simPreviewBox, 588, 23, null);

        BufferedImage simPreviewImage = ImageLoader.showSimPreview(currentSim);
        g.drawImage(simPreviewImage, 654, 33, 56, 56, null);

        if (currentHouse != null) {
            g.drawImage(houseInfoBox, 588, 136, null);
        }

        g.drawImage(helpBox, 584, 546, null);
    }

    private static void drawTexts(Graphics2D g) {
        currentSim = UserInterface.getCurrentSim();
        currentHouse = currentSim.getCurrentHouse();

        // sim preview and name box
        Font font = new Font("Inter", Font.BOLD, 14);
        g.setFont(font);
        g.setColor(Color.WHITE);
        UserInterface.drawCenteredText(g, simPreviewBox, 588, 110, currentSim.getName(), font);
        
        // house name box
        if (currentHouse != null) {
            font = new Font("Inter", Font.BOLD, 11);
            g.setFont(font);
            UserInterface.drawCenteredText(g, houseInfoBox, 588, 159, 12, currentHouse.getName(), font);

            // press escape to return
            font = new Font("Inter", Font.PLAIN, 10);
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawString("press esc to return to current house", 596, 200);
        }
        else {
            // press escape to return
            font = new Font("Inter", Font.PLAIN, 10);
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawString("press esc to cancel", 637, 140);
        }
    }
}
