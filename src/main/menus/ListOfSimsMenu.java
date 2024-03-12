package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.assets.ImageLoader;
import src.entities.sim.Sim;
import src.main.KeyHandler;
import src.main.UserInterface;
import src.main.panels.CreateSimPanel;
import src.main.panels.GamePanel;
import src.main.panels.PanelHandler;
import src.main.time.GameTime;
import src.world.World;

public class ListOfSimsMenu {
    private static BufferedImage[] images = ImageLoader.loadListOfSimsMenu();

    private static BufferedImage chooseSimBox = images[0];
    private static BufferedImage simBox = images[1];
    private static BufferedImage currentSimBox = images[2];
    private static BufferedImage createNewSim = images[3];
    private static BufferedImage simBoxHighlight = images[4];
    private static BufferedImage createNewSimHighlight = images[5];
    private static BufferedImage createNewSimHighlightRed = images[6];
    private static BufferedImage healthIcon = images[7];
    private static BufferedImage hungerIcon = images[8];
    private static BufferedImage moodIcon = images[9];
    private static BufferedImage help = images[10];

    public static World world = UserInterface.getWorld();
    public static ArrayList<Sim> listOfSims = world.getListOfSim();
    private static ArrayList<Sim> listOfSelectableSims = getListOfSelectableSims();
    private static int slotSelected = 0;
    private static boolean createSimSlot = false;

    public static void reset() {
        slotSelected = 0;
        createSimSlot = false;
        listOfSelectableSims = getListOfSelectableSims();

        if (listOfSelectableSims.isEmpty()) {
            createSimSlot = true;
        }
    }

    private static ArrayList<Sim> getListOfSelectableSims() {
        ArrayList<Sim> listOfSelectableSims = new ArrayList<>();
        for (Sim sim : listOfSims) {
            if (sim == UserInterface.getCurrentSim()) continue;

            listOfSelectableSims.add(sim);
        }
        return listOfSelectableSims;
    }

    public static void update() {
        // pressing a button
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
            if (createSimSlot) {
                Sim currentSim = UserInterface.getCurrentSim();
                int dayLastAddedSim = currentSim.getDayLastAddedSim();
                int currentDay = GameTime.day;

                if (dayLastAddedSim == currentDay) return;

                GamePanel.gameState = "Creating a new sim";
                CreateSimPanel.init();
                PanelHandler.switchPanel(GamePanel.getInstance(), CreateSimPanel.getInstance());
            }
            else {
                Sim simSelected = listOfSelectableSims.get(slotSelected);
                UserInterface.setCurrentSim(simSelected);
                UserInterface.viewListOfSims();
            }
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
            UserInterface.viewListOfSims();
        }

        // moving the cursor
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
            slotSelected--;   
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
            slotSelected++;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_TAB)) {
            if (listOfSelectableSims.isEmpty()) return;
            
            createSimSlot = !createSimSlot;
        }

        if (slotSelected < 0) slotSelected = listOfSelectableSims.size() - 1;
        if (slotSelected > listOfSelectableSims.size() - 1) slotSelected = 0;
    }

    public static void draw(Graphics2D g) {
        g.setColor(new Color(110, 196, 213));
        g.fillRect(0, 0, 800, 600);

        if (UserInterface.isHelped()) {
            g.drawImage(help, 0, 0, 800, 570, null);
        }

        drawInfoBoxes(g);

        drawListOfSims(g);

        drawIcons(g);

        drawTexts(g);

        drawSimInfoBarValues(g);
    }

    private static void drawInfoBoxes(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();
        g.drawImage(chooseSimBox, 343, 42, null);

        g.drawImage(currentSimBox, 103, 70, null);

        drawSelector(g);

        g.drawImage(createNewSim, 109, 411, null);

        BufferedImage simPreviewImage = ImageLoader.showSimPreview(currentSim);
        g.drawImage(simPreviewImage, 185, 114, 56, 56, null);
    }

    private static void drawSelector(Graphics2D g) {
        if (createSimSlot) {
            Sim currentSim = UserInterface.getCurrentSim();
            int dayLastAddedSim = currentSim.getDayLastAddedSim();
            int currentDay = GameTime.day;

            if (dayLastAddedSim == currentDay) {
                g.drawImage(createNewSimHighlightRed, 106, 408, null);
            }
            else {
                g.drawImage(createNewSimHighlight, 106, 408, null);
            }
            return;
        }

        if (!listOfSelectableSims.isEmpty()) {
            g.drawImage(simBoxHighlight, 364, 110 + (slotSelected * 84), null);
        }
    }

    private static void drawListOfSims(Graphics2D g) {
        Font font = new Font("Inter", Font.BOLD, 16);
        g.setFont(font);

        int i = 0;
        for (Sim sim : listOfSelectableSims) {
            g.drawImage(simBox, 367, 113 + (84 * i), null);

            BufferedImage simPreviewImage = ImageLoader.showSimPreview(sim);
            g.drawImage(simPreviewImage, 375, 125 + (84 * i), 56, 56, null);

            font = new Font("Inter", Font.BOLD, 16);
            g.setColor(Color.WHITE);
            g.setFont(font);
            UserInterface.drawCenteredText(g, simBox, 367, 142 + (84 * i), 30, sim.getName(), font);

            font = new Font("Inter", Font.BOLD, 10);
            g.setFont(font);
            g.setColor(new Color(61, 30, 45));
            UserInterface.drawCenteredText(g, simBox, 367, 173 + (84 * i), 30, sim.getStatus(), font);
            i++;
        }
    }

    private static void drawValue(Graphics2D g, int value, int offsetX, int offsetY) {
        if (value < 100) {
            g.drawString("" + value, offsetX, 303 + (36 * offsetY));
        }
        else {
            g.drawString("" + value, offsetX - 5, 303 + (36 * offsetY));
        }
    }

    private static void drawTexts(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();

        Font font = new Font("Inter", Font.BOLD, 14);
        
        g.setColor(Color.WHITE);
        g.setFont(font);
        UserInterface.drawCenteredText(g, currentSimBox, 103, 190, currentSim.getName(), font);

        font = new Font("Inter", Font.BOLD, 10);
        g.setColor(new Color(61, 30, 45)); 
        g.setFont(font);
        UserInterface.drawCenteredText(g, currentSimBox, 103, 213, currentSim.getStatus(), font);

        g.setColor(Color.WHITE);
        UserInterface.drawCenteredText(g, currentSimBox, 103, 245, "$ " + currentSim.getMoney(), font);

        g.setColor(new Color(61, 30, 45));
        g.drawString("Health", 161, 284);
        g.drawString("Hunger", 161, 320);
        g.drawString("Mood", 161, 356);

        font = new Font("Inter", Font.PLAIN, 8);
        g.setFont(font);
        drawValue(g, currentSim.getHealth(), 289, 0);
        drawValue(g, currentSim.getHunger(), 289, 1);
        drawValue(g, currentSim.getMood(), 289, 2);

        int dayLastAddedSim = currentSim.getDayLastAddedSim();
        int currentDay = GameTime.day;

        if (createSimSlot && dayLastAddedSim == currentDay) {
            UserInterface.drawCenteredText(g, currentSimBox, 103, 460, "you have to wait for tomorrow", font);
            UserInterface.drawCenteredText(g, currentSimBox, 103, 470, "to create a new sim", font);
        }
    }

    private static void drawIcons(Graphics2D g) {
        g.drawImage(healthIcon, 130, 268, null); // health icon
        g.drawImage(hungerIcon, 130, 303, null); // hunger icon
        g.drawImage(moodIcon, 130, 339, null); // mood icon
    }

    private static void drawBarValue(Graphics2D g, int x, int y, int barWidth, int value, int maxValue) {
        float valuePercentage = (float) value / (float) maxValue;
        int valueBar = (int) (barWidth * valuePercentage);

        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, barWidth, 6, 4, 4);
        
        g.setColor(new Color(61, 30, 45));
        g.fillRoundRect(x, y, valueBar, 6, 4, 4);
    }

    private static void drawSimInfoBarValues(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();

        drawBarValue(g, 163, 288, 135, currentSim.getHealth(), 100);
        drawBarValue(g, 163, 324, 135, currentSim.getHunger(), 100);
        drawBarValue(g, 163, 360, 135, currentSim.getMood(), 100);
    }
}
