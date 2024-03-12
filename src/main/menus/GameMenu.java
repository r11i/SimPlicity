package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import src.main.KeyHandler;
import src.assets.ImageLoader;
import src.main.UserInterface;
import src.main.time.GameTime;
import src.main.time.ActivityTimer;
import src.entities.handlers.InteractionHandler;
import src.entities.interactables.Interactables;
import src.entities.sim.Sim;
import src.world.House;
import src.world.Room;

public class GameMenu {
    // Load the images for the menu
    private static BufferedImage[] images = ImageLoader.loadGameMenu();
    private static BufferedImage doubleInfoBox = images[0];
    private static BufferedImage moneyBox = images[1];
    private static BufferedImage simInfoBox = images[2];
    private static BufferedImage dayBox = images[3];
    private static BufferedImage healthIcon = images[4];
    private static BufferedImage hungerIcon = images[5];
    private static BufferedImage moodIcon = images[6];
    private static BufferedImage helpBox = images[7];
    private static BufferedImage help = images[8];

    // Attributes to make showing time easier
    public static int workDuration = 0;
    public static int exerciseDration = 0;

    private static ArrayList<ActivityTimer> listOfActiveActivities;
    private static ArrayList<ActivityTimer> listOfCurrentSimActiveActivities;
    private static int numPages = 0;
    private static int slideSelected = 0;

    private static void updateSimActiveActivities() {
        try {
            Sim currentSim = UserInterface.getCurrentSim();
            listOfActiveActivities = GameTime.getListOfActiveActivities();
            listOfCurrentSimActiveActivities = new ArrayList<>();

            for (ActivityTimer activityTimer : listOfActiveActivities) {
                Sim activeSim = activityTimer.getSim();

                boolean simIsActive = currentSim.getName().equals(activeSim.getName());
                if (simIsActive) {
                    listOfCurrentSimActiveActivities.add(activityTimer);
                }
            }

            numPages = listOfCurrentSimActiveActivities.size() / 3;
        }
        catch (ConcurrentModificationException cme) {}
    }

    public static void updateTimeRemainingTab() {
        updateSimActiveActivities();
        int newSlideSelected = slideSelected;
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
            newSlideSelected--;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
            newSlideSelected++;
        }

        if (newSlideSelected >= 0 && newSlideSelected <= numPages) {
            slideSelected = newSlideSelected;
        }
    }

    public static void draw(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();
        InteractionHandler simInteract = currentSim.getInteractionHandler();
        boolean objectInRange = simInteract.isObjectInRange();

        if (UserInterface.isHelped() && !UserInterface.isTabbed() &&
            !UserInterface.isUpgradingHouse()) {
            g.drawImage(help, 0, 0, 800, 580, null);
        }

        // boxes
        drawBoxes(g);

        // icons
        drawIcons(g);

        // game info and values
        drawTexts(g);
        drawSimInfoBarValues(g);

        // press ? for help
        pressQuestionMarkForHelp(g);

        // press esc to pause the game
        if (!UserInterface.isTabbed() && !UserInterface.isViewingTime() &&
            !UserInterface.isHelped() && !objectInRange) {
            pressEscapeToPause(g);
        }

        if (UserInterface.isViewingTime()) {
            pressEscapeToReturnPlaying(g);
        }

        // press f to interact
        if (!UserInterface.isTabbed() && !UserInterface.isViewingTime() && objectInRange) {
            drawObjectToInteract(g);
        }

        // working and exercising tab
        if (currentSim.isStatusCurrently("Working")) {
            drawWorkingTab(g);
        }
        if (currentSim.isStatusCurrently("Exercising")) {
            drawExercisingTab(g);
        }

        if (UserInterface.isViewingTime()) {
            drawTimeRemainingTab(g);
        }

        // ONLY FOR DEBUGGING
        if (!UserInterface.isHelped() & UserInterface.isDebug()) {
            drawDebug(g, 100);
        }
    }

    // MAIN GAME MENU
    private static void drawValue(Graphics2D g, int value, int offsetX, int offsetY) {
        if (value < 100) {
            g.drawString("" + value, offsetX, 186 + (36 * offsetY));
        }
        else {
            g.drawString("" + value, offsetX - 5, 186 + (36 * offsetY));
        }
    }

    private static void drawBoxes(Graphics2D g) {
        g.drawImage(doubleInfoBox, 3, 49, null); // double sim info box
        g.drawImage(moneyBox, 7, 105, null); // money box
        g.drawImage(simInfoBox, 7, 141, null); // sim info box
        g.drawImage(dayBox, 603, 47, null); // day box
        g.drawImage(doubleInfoBox, 599, 87, null); // double house info box
        g.drawImage(helpBox, 305, 0, null); // help box
    }

    private static void drawIcons(Graphics2D g) {
        g.drawImage(healthIcon, 17, 151, null); // health icon
        g.drawImage(hungerIcon, 17, 186, null); // hunger icon
        g.drawImage(moodIcon, 17, 222, null); // mood icon
    }

    private static void drawTexts(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();
        House currentHouse = currentSim.getCurrentHouse();
        Room currentRoom = currentSim.getCurrentRoom();

        Font font = new Font("Inter", Font.BOLD, 14);
        
        g.setColor(Color.WHITE);
        g.setFont(font);
        UserInterface.drawCenteredText(g, doubleInfoBox, 6, 70, currentSim.getName(), font);
        UserInterface.drawCenteredText(g, dayBox, 603, 69, "Day " + GameTime.day, font);
        UserInterface.drawCenteredText(g, doubleInfoBox, 599, 108, currentHouse.getName(), font);
        
        font = new Font("Inter", Font.BOLD, 12);
        g.setFont(font);
        UserInterface.drawCenteredText(g, moneyBox, 7, 125, "$ " + currentSim.getMoney(), font);
        g.drawString("Health", 51, 167);
        g.drawString("Hunger", 51, 203);
        g.drawString("Mood", 51, 239);

        g.setColor(new Color(61, 30, 45)); 
        g.setFont(new Font("Inter", Font.BOLD, 10));
        UserInterface.drawCenteredText(g, doubleInfoBox, 6, 93, currentSim.getStatus(), font);
        UserInterface.drawCenteredText(g, doubleInfoBox, 603, 130, currentRoom.getName(), font);

        g.setColor(Color.WHITE);
        font = new Font("Inter", Font.PLAIN, 8);
        g.setFont(font);
        drawValue(g, currentSim.getHealth(), 175, 0);
        drawValue(g, currentSim.getHunger(), 175, 1);
        drawValue(g, currentSim.getMood(), 175, 2);
    }

    private static void drawObjectToInteract(Graphics2D g) {
        BufferedImage windowArea = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Sim currentSim = UserInterface.getCurrentSim();
        InteractionHandler simInteract = currentSim.getInteractionHandler();

        if (currentSim.isStatusCurrently("Idle") && simInteract.isObjectInRange() &&
            !UserInterface.isTabbed() && !UserInterface.isViewingInteractions()) {
            Interactables object = simInteract.getInteractableObject();
            Font font = new Font("Inter", Font.PLAIN, 12);
            g.setFont(font);
            g.setColor(Color.BLACK);
            UserInterface.drawCenteredText(g, windowArea, 0, 468, "press f to " + object.getInteraction(), font);
        }
    }

    private static void drawBarValue(Graphics2D g, int x, int y, int barWidth, int value, int maxValue) {
        float valuePercentage = (float) value / (float) maxValue;
        int valueBar = (int) (barWidth * valuePercentage);

        g.setColor(new Color(61, 30, 45));
        g.fillRoundRect(x, y, barWidth, 6, 4, 4);

        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, valueBar, 6, 4, 4);
    }

    private static void drawSimInfoBarValues(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();

        drawBarValue(g, 51, 171, 135, currentSim.getHealth(), 100);
        drawBarValue(g, 51, 207, 135, currentSim.getHunger(), 100);
        drawBarValue(g, 51, 243, 135, currentSim.getMood(), 100);
    }

    private static void pressQuestionMarkForHelp(Graphics2D g) {
        g.setFont(new Font("Inter", Font.PLAIN, 10));
        g.drawString("press", 361, 14);
        g.drawString("for help", 400, 14);
        g.setFont(new Font("Inter", Font.BOLD, 10));
        g.drawString("?", 391, 14);
    }

    private static void pressEscapeToPause(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Inter", Font.PLAIN, 12));
        g.drawString("press", 317, 468);
        g.drawString("to pause the game", 376, 468);
        g.setFont(new Font("Inter", Font.BOLD, 12));
        g.drawString("esc", 352, 468);
    }

    private static void pressEscapeToReturnPlaying(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Inter", Font.PLAIN, 12));
        g.drawString("press", 317, 468);
        g.drawString("to return playing", 376, 468);
        g.setFont(new Font("Inter", Font.BOLD, 12));
        g.drawString("esc", 352, 468);
    }

    private static void drawTimeRemainingValue(Graphics2D g, int value, int offsetX, int offsetY) {
        g.setColor(Color.WHITE);
        Font font = new Font("Inter", Font.PLAIN, 8);
        g.setFont(font);

        if (value > 999) {
            g.drawString("" + value, offsetX - 10, 349 + (39 * offsetY));
        }
        else if (value < 100) {
            g.drawString("" + value, offsetX, 349 + (39 * offsetY));
        }
        else {
            g.drawString("" + value, offsetX - 5, 349 + (39 * offsetY));
        }
    }

    // TABS
    private static void drawWorkingTab(Graphics2D g) {
        BufferedImage[] images = ImageLoader.loadWorkingTab();
        BufferedImage workingBox = images[0];
        BufferedImage iconWork = images[1];

        g.drawImage(workingBox, 3, 273, null);
        g.drawImage(iconWork, 17, 312, null);

        g.setColor(Color.WHITE);
        Font font = new Font("Inter", Font.BOLD, 12);
        g.setFont(font);

        UserInterface.drawCenteredText(g, workingBox, 4, 293, "Working", font);

        Sim currentSim = UserInterface.getCurrentSim();
        ActivityTimer activityTimer = GameTime.getActivityTimer(currentSim, "Working");
        int timeRemaining = activityTimer.getTimeRemaining();

        g.drawString("Working", 51, 330);
        drawBarValue(g, 51, 334, 135, timeRemaining, workDuration);
        drawTimeRemainingValue(g, timeRemaining, 175, 0);
    }

    private static void drawExercisingTab(Graphics2D g) {
        BufferedImage[] images = ImageLoader.loadExercisingTab();
        BufferedImage exercisingBox = images[0];
        BufferedImage iconExercise = images[1];

        g.drawImage(exercisingBox, 3, 273, null);
        g.drawImage(iconExercise, 17, 312, null);

        g.setColor(Color.WHITE);
        Font font = new Font("Inter", Font.BOLD, 12);
        g.setFont(font);

        UserInterface.drawCenteredText(g, exercisingBox, 4, 293, "Exercising", font);

        Sim currentSim = UserInterface.getCurrentSim();
        ActivityTimer activityTimer = GameTime.getActivityTimer(currentSim, "Exercising");
        int timeRemaining = activityTimer.getTimeRemaining();

        g.drawString("Exercising", 51, 330);
        drawBarValue(g, 51, 334, 135, timeRemaining, exerciseDration);
        drawTimeRemainingValue(g, timeRemaining, 175, 0);
    }

    // TO - DO!!! : Actually draw list of times
    private static void drawTimeRemainingTab(Graphics2D g) {
        BufferedImage[] images = ImageLoader.loadTimeRemainingTab();
        BufferedImage timeRemainingBox = images[0];
        BufferedImage iconTime = images[1];
        BufferedImage iconBuild = images[2];
        BufferedImage iconBuyItem = images[3];

        g.drawImage(timeRemainingBox, 7, 273, null);
        
        g.setColor(Color.WHITE);
        Font font = new Font("Inter", Font.BOLD, 12);
        g.setFont(font);

        UserInterface.drawCenteredText(g, timeRemainingBox, 7, 292, "Time Remaining", font);

        if (slideSelected == 0) {
            int timeRemaining = GameTime.timeRemaining;

            g.drawImage(iconTime, 17, 313, null);
            g.drawString("Time", 51, 329);
            drawBarValue(g, 51, 333, 135, timeRemaining, GameTime.initialTimeRemaining);
            drawTimeRemainingValue(g, timeRemaining, 175, 0);
        }

        try {
            int i = 1;
            for (ActivityTimer activityTimer : listOfCurrentSimActiveActivities) {
                if (i % 3 == 0) continue;

                String activity = activityTimer.getActivity();
                int activityTimeRemaining = activityTimer.getTimeRemaining();
                int activityDuration = activityTimer.getDuration();

                if (activity.equals("Build Room")) {
                    g.drawImage(iconBuild, 17, 312 + (i * 39), null);
                }
                if (activity.equals("Delivering Item(s)")) {
                    g.drawImage(iconBuyItem, 17, 312 + (i * 39), null);
                }

                g.setColor(Color.WHITE);
                font = new Font("Inter", Font.BOLD, 12);
                g.setFont(font);

                g.drawString(activity, 51, 329 + (i * 39));
                drawBarValue(g, 51, 333 + (i * 39), 135, activityTimeRemaining, activityDuration);
                drawTimeRemainingValue(g, activityTimeRemaining, 175, i);
                i++;
            }
        }
        catch (ConcurrentModificationException cme) {}
    }

    public static void drawDebug(Graphics2D g, int offset) {
        Sim currentSim = UserInterface.getCurrentSim();

        g.setColor(Color.BLACK);
        Font font = new Font("Inter", Font.PLAIN, 10);
        g.setFont(font);

        g.drawString("timeRemaining: " + GameTime.timeRemaining, 33, 364 + offset);
        g.drawString("x: " + currentSim.getX(), 33, 374 + offset);
        g.drawString("y: " + currentSim.getY(), 33, 384 + offset);
        g.drawString("InRange: " + currentSim.getInteractionHandler().isObjectInRange(), 73, 374 + offset);
        g.drawString("isWalking: " + currentSim.isMoving(), 73, 384 + offset);
        g.drawString("isEditingRoom: " + currentSim.getCurrentRoom().isEditingRoom(), 33, 398 + offset);
        g.drawString("isBusy: " + currentSim.isBusy(), 33, 408 + offset);
        g.drawString("isEditingRoom: " + currentSim.getCurrentRoom().isEditingRoom(), 33, 398 + offset);
        g.drawString("isBusy: " + currentSim.isBusy(), 33, 408 + offset);
        g.drawString("Profession: " + currentSim.getProfession().getName(), 33, 418 + offset);
        g.drawString("durationWorked: " + currentSim.getDurationWorked(), 33, 428 + offset);
        g.drawString("timeNotSlept: " + currentSim.getTimeNotSlept(), 33, 438 + offset);
        g.drawString("timeNotTakenLeak: " + currentSim.getTimeNotTakenLeak(), 33, 448 + offset);
        g.drawString("hasAte: " + currentSim.hasAte(), 33, 458 + offset);
    }
}
