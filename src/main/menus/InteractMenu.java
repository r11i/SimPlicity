package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import src.main.Consts;
import src.main.KeyHandler;
import src.entities.sim.Sim;
import src.assets.ImageLoader;
import src.main.UserInterface;
import src.entities.interactables.Bed;
import src.entities.interactables.Television;
import src.entities.interactables.TableAndChair;
import src.entities.interactables.Interactables;
import src.entities.handlers.InteractionHandler;

public class InteractMenu {
    private static BufferedImage[] images = ImageLoader.loadInteractMenu();
    private static BufferedImage interactBox = images[0];
    private static BufferedImage highlight = images[1];
    private static BufferedImage decrease = images[2];
    private static BufferedImage increase = images[3];
    private static BufferedImage decreaseHighlight = images[4];
    private static BufferedImage increaseHighlight = images[5];
    private static BufferedImage counterBox = images[6];

    public static int slotSelected = -1;
    public static int sleepDuration = Consts.ONE_MINUTE * 3;

    public static void update() {
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
            slotSelected--;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
            slotSelected++;
        }
        if (slotSelected < 0) slotSelected = 0;
        else if (slotSelected > 1) slotSelected = 1;

        Sim currentSim = UserInterface.getCurrentSim();
        InteractionHandler currentSimInteract = currentSim.getInteractionHandler();
        Interactables interactedObject = currentSimInteract.getInteractableObject();

        if (interactedObject instanceof Bed) {
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_MINUS)) {
                sleepDuration--;
                if (sleepDuration < Consts.ONE_MINUTE * 3) sleepDuration = Consts.ONE_MINUTE * 3;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_EQUALS)) {
                sleepDuration++;
            }
        }
    }

    public static void draw(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();
        InteractionHandler currentSimInteract = currentSim.getInteractionHandler();
        Interactables interactedObject = currentSimInteract.getInteractableObject();

        Font font = new Font("Inter", Font.PLAIN, 14);
        
        if (interactedObject instanceof Bed) {
            g.drawImage(counterBox, 365, 450, null);

            g.setFont(font);
            g.setColor(Color.BLACK);

            UserInterface.drawCenteredText(g, counterBox, 365, 470, Integer.toString(sleepDuration), font);

            g.drawImage(decrease, 320, 449, null);
            g.drawImage(increase, 437, 449, null);
            
            if (KeyHandler.isKeyDown(KeyHandler.KEY_MINUS)) {
                g.drawImage(decreaseHighlight, 319, 448, null);
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_EQUALS)) {
                g.drawImage(increaseHighlight, 436, 448, null);
            }
        }
        else {
            g.drawImage(highlight, 205 + (slotSelected *  196), 445, null);
            g.drawImage(interactBox, 208, 448, null);
            g.drawImage(interactBox, 404, 448, null);

            font = new Font("Inter", Font.BOLD, 14);
            g.setFont(font);
            g.setColor(Color.WHITE);

            if (interactedObject instanceof Television) {
                UserInterface.drawCenteredText(g, interactBox, 208, 470, "Karaoke", font);
                UserInterface.drawCenteredText(g, interactBox, 404, 470, "Watch TV", font);
            }
            if (interactedObject instanceof TableAndChair) {
                UserInterface.drawCenteredText(g, interactBox, 208, 470, "Eat", font);
                UserInterface.drawCenteredText(g, interactBox, 404, 470, "Read a Book", font);
            }
        }
    }
}
