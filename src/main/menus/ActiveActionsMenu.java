package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.entities.sim.Sim;
import src.entities.sim.actions.ActiveActions;
import src.main.Consts;
import src.main.KeyHandler;
import src.main.UserInterface;

public class ActiveActionsMenu {
    // Selection Box Attributes
    private static int selectedBox = 0; // Boxes starting from 0 to 4
    private static int workDuration = 120;
    private static int exerciseDuration = 20;

    // Images for the menu
    private static BufferedImage[] images = ImageLoader.loadActiveActionsMenu();
    private static BufferedImage changeProfessionIcon = images[0];
    private static BufferedImage goToWorkIcon = images[1];
    private static BufferedImage goExerciseIcon = images[2];
    private static BufferedImage visitAnotherSim = images[3];
    private static BufferedImage button = images[4];
    private static BufferedImage counterBox = images[5];
    private static BufferedImage decrease = images[6];
    private static BufferedImage increase = images[7];
    private static BufferedImage decreaseHighlight = images[8];
    private static BufferedImage increaseHighlight = images[9];
    private static BufferedImage background = images[10];
    private static BufferedImage help = images[11];
    
    // Methods
    private static void boxEntered() {
        Sim sim = UserInterface.getCurrentSim();
        switch (selectedBox) {
            case 0:
                UserInterface.viewProfessions();
                UserInterface.viewActiveActions();
                break;
            case 1:
                ActiveActions.work(sim, workDuration);
                GameMenu.workDuration = workDuration;
                UserInterface.viewActiveActions();
                break;
            case 2:
                ActiveActions.exercise(sim, exerciseDuration);
                GameMenu.exerciseDration = exerciseDuration;
                UserInterface.viewActiveActions();
                break;
            case 3:
                ActiveActions.visitAnotherSim();
                UserInterface.viewActiveActions();
                break;
            default:
                break;
        }
    }

    private static void decreaseWorkDuration() {
        workDuration -= 120;
        if (workDuration < 120) {
            workDuration = 120;
        }
    }

    private static void increaseWorkDuration() {
        workDuration += 120;
        if (workDuration > 240) {
            workDuration = 240;
        }
    }

    private static void decreaseExerciseDuration() {
        exerciseDuration -= 20;
        if (exerciseDuration < 20) {
            exerciseDuration = 20;
        }
    }

    private static void increaseExerciseDuration() {
        exerciseDuration += 20;
        if (exerciseDuration > 370) {
            exerciseDuration = 370;
        }
    }

    public static void resetValues() {
        workDuration = 120;
        exerciseDuration = 20;
    }
    
    public static void update() {
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
            UserInterface.viewActiveActions();
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
            boxEntered();
        }

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_MINUS)) {
            if (selectedBox == 1) {
                decreaseWorkDuration();
            }
            if (selectedBox == 2) {
                decreaseExerciseDuration();
            }
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_EQUALS)) {
            if (selectedBox == 1) {
                increaseWorkDuration();
            }
            if (selectedBox == 2) {
                increaseExerciseDuration();
            }
        }

        int newSelectedBox = selectedBox;

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)){
            newSelectedBox--;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)){
            newSelectedBox++;
        }

        selectedBox = newSelectedBox;
        if (newSelectedBox == -1) {
            selectedBox = 3;
        }
        if (newSelectedBox == 4) {
            selectedBox = 0;
        }
    }

    public static void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);

        if (UserInterface.isHelped()) {
            g.drawImage(help, 0, 0, 800, 600, null);
        }

        drawIconsAndButtons(g);

        drawSelectedBoxText(g);
    }

    private static void drawIconsAndButtons(Graphics2D g) {
        g.drawImage(changeProfessionIcon, 245, 265, null);
        g.drawImage(goToWorkIcon, 325, 265, null);
        g.drawImage(goExerciseIcon, 405, 265, null);
        g.drawImage(visitAnotherSim, 485, 265, null);

        g.drawImage(button, 242, 358, null);
        
        g.drawImage(images[selectedBox], 240 + (selectedBox * 80), 261, Consts.SCALED_TILE + 15, Consts.SCALED_TILE + 15, null);

        if (selectedBox == 1 || selectedBox == 2) {
            g.drawImage(counterBox, 366, 208, null);
            g.drawImage(decrease, 323, 206, null);
            g.drawImage(increase, 437, 206, null);      

            if (KeyHandler.isKeyDown(KeyHandler.KEY_MINUS)) {
                g.drawImage(decreaseHighlight, 322, 205, null);
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_EQUALS)) {
                g.drawImage(increaseHighlight, 436, 205, null);
            }
        }
    }

    private static void drawSelectedBoxText(Graphics2D g) {
        Font font = new Font("Inter", Font.PLAIN, 14);

        g.setColor(Color.BLACK);
        g.setFont(font);

        if (selectedBox == 0) {
            g.setColor(Color.WHITE);
            UserInterface.drawCenteredText(g, button, 242, 378, "change professions", font);
        }
        if (selectedBox == 1) {
            UserInterface.drawCenteredText(g, background, 0, 188, "work duration", font);
            UserInterface.drawCenteredText(g, counterBox, 366, 228, Integer.toString(workDuration), font);
            
            g.setColor(Color.WHITE);
            UserInterface.drawCenteredText(g, button, 242, 378, "go to work", font);
        }
        if (selectedBox == 2) {
            UserInterface.drawCenteredText(g, background, 0, 188, "exercise duration", font);
            UserInterface.drawCenteredText(g, counterBox, 366, 228, Integer.toString(exerciseDuration), font);

            g.setColor(Color.WHITE);
            UserInterface.drawCenteredText(g, button, 242, 378, "start exercising", font);
        }
        if (selectedBox == 3) {
            g.setColor(Color.WHITE);
            UserInterface.drawCenteredText(g, button, 242, 378, "visit another sim", font);
        }
    }
}
