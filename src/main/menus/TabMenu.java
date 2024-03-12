package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import src.main.Consts;
import src.main.KeyHandler;
import src.assets.ImageLoader;
import src.main.UserInterface;
import src.entities.sim.actions.*;
import src.entities.sim.Sim;
import src.world.House;
import src.world.Room;

public class TabMenu {
    // Tab Menu Images
    private static BufferedImage[] images = ImageLoader.loadTabMenu();

    // Selection Box Attributes
    private static int selectedBox = 0; // Boxes starting from 0 to 4

    private static void moveSelectedBox(String direction) {
        switch (direction)  {
            case "left":
                selectedBox--;
                if (selectedBox < 0) {
                    selectedBox = 4;
                }
                break;
            case "right":
                selectedBox++;
                if (selectedBox > 4) {
                    selectedBox = 0;
                }
                break;
            default:
                break;
        }
    }

    // TO - DO !!! : Integrate with Store
    private static void boxEntered() {
        UserInterface.tab();
        
        Sim currentSim = UserInterface.getCurrentSim();
        Room currentRoom = currentSim.getCurrentRoom();
        House currentHouse = currentSim.getCurrentHouse();
        Sim currentHouseOwner = currentHouse.getOwner();

        if (currentSim.isBusy()) return;
        if (!currentSim.isStatusCurrently("Idle")) return;

        boolean isSimHouseOwner = currentSim.getName().equals(currentHouseOwner.getName());

        switch (selectedBox) {
            case 0:
                if (!isSimHouseOwner) return;
                NonActiveActions.editRoom(currentRoom);
                break;
            case 1:
                if (!isSimHouseOwner) return;
                UserInterface.upgradeHouse();
                break;
            case 2:
                UpgradeActions.viewStore();
                break;
            case 3:
                UserInterface.viewListOfSims();
                break;
            case 4:
                ActiveActions.visitAnotherSim();
                break;
            default:
                break;
        }
    }
    
    public static void update() {
        if (UserInterface.isTabbed()) {
            // Change selected box based on key input
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
                moveSelectedBox("left");
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
                moveSelectedBox("right");
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
                boxEntered();
            }
        }
    }

    public static void draw(Graphics2D g) {
        g.drawImage(images[0], 204, 488, null);
        g.drawImage(images[1], 284, 488, null);
        g.drawImage(images[2], 364, 488, null);
        g.drawImage(images[3], 444, 488, null);
        g.drawImage(images[4], 524, 488, null);
        
        // Draw selected box
        if (UserInterface.isTabbed()) {
            g.drawImage(images[selectedBox], 199 + (selectedBox * 80), 484, Consts.SCALED_TILE + 15, Consts.SCALED_TILE + 14, null);
            drawSelectedBoxText(g);
        }
    }

    private static void drawSelectedBoxText(Graphics2D g) {
        Font font = new Font("Inter", Font.PLAIN, 12);

        BufferedImage windowArea = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);

        g.setFont(font);
        g.setColor(Color.BLACK);
        switch (selectedBox) {
            case 0:
                UserInterface.drawCenteredText(g, windowArea, 0, 468, "edit your room", font);
                break;
            case 1:
                UserInterface.drawCenteredText(g, windowArea, 0, 468, "upgrade your house", font);
                break;
            case 2:
                UserInterface.drawCenteredText(g, windowArea, 0, 468, "look at the store", font);
                break;
            case 3:
                UserInterface.drawCenteredText(g, windowArea, 0, 468, "view list of sims", font);
                break;
            case 4:
                UserInterface.drawCenteredText(g, windowArea, 0, 468, "visit another sim", font);
                break;
            default:
                g.drawString("lorem ipsum", Consts.CENTER_X - 28, 468);
                break;
        }
    }
}
