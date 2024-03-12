package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.entities.sim.Sim;
import src.entities.sim.actions.UpgradeActions;
import src.main.KeyHandler;
import src.main.UserInterface;

public class UpgradeHouseMenu {
    private static BufferedImage[] images = ImageLoader.loadUpgradeHouse();
    private static BufferedImage roomNameBox = images[0];
    private static BufferedImage priceBox = images[1];
    private static BufferedImage addBox = images[2];
    private static BufferedImage priceBoxNotSufficient = images[3];

    private static String roomName = "";

    public static void reset() {
        roomName = "";
    }

    public static void update(KeyEvent e) {
        if (!roomName.equals("") && KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
            Sim currentSim = UserInterface.getCurrentSim();
            boolean fundsSufficient = currentSim.getMoney() >= 1500;

            if (fundsSufficient) {
                UpgradeActions.addRoom(roomName);
                UserInterface.upgradeHouse();
            }
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
            UserInterface.upgradeHouse();   
        }

        roomName = KeyHandler.receiveStringInput(e, roomName);
    }

    public static void draw(Graphics2D g) {
        drawBoxes(g);

        drawTexts(g);
    }

    private static void drawBoxes(Graphics2D g) {
        g.drawImage(roomNameBox, 203, 449, null);

        Sim currentSim = UserInterface.getCurrentSim();
        boolean fundsSufficient = currentSim.getMoney() >= 1500;
        if (fundsSufficient) {
            g.drawImage(priceBox, 454, 454, null);
        }
        else {
            g.drawImage(priceBoxNotSufficient, 454, 454, null);
        }

        if (!roomName.equals("")) {
            if (fundsSufficient) {
                g.drawImage(addBox, 523, 449, null);
            }
        }        
    }

    private static void drawTexts(Graphics2D g) {
        Font font = new Font("Inter", Font.BOLD, 14);
        g.setFont(font);

        Sim currentSim = UserInterface.getCurrentSim();
        boolean fundsSufficient = currentSim.getMoney() >= 1500;
        if (fundsSufficient) {
            g.setColor(new Color(243, 201, 154));
            UserInterface.drawCenteredText(g, priceBox, 453, 469, "$ 1500", font);
        }
        else {
            g.setColor(new Color(61, 30, 45));
            UserInterface.drawCenteredText(g, priceBox, 453, 469, "$ 1500", font);
        }

        font = new Font("Inter", Font.PLAIN, 14);
        g.setFont(font);
        g.setColor(new Color(61, 30, 45));

        if (roomName.equals("")) {
            g.drawString("Enter room name...", 212, 469);
        }
        else {
            if (fundsSufficient) {
                g.setColor(Color.WHITE);
                UserInterface.drawCenteredText(g, addBox, 523, 469, "Add", font);
            }
            g.setColor(new Color(61, 30, 45));
            g.drawString(roomName, 212, 469);
        }
    }
}
