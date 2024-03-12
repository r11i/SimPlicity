package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.entities.sim.Profession;
import src.entities.sim.Sim;
import src.main.Consts;
import src.main.KeyHandler;
import src.main.UserInterface;

public class ChangeProfessionMenu {
    // Load the images of the menu
    private static BufferedImage[] images = ImageLoader.loadChangeProfessionMenu();
    private static BufferedImage titleBox = images[0];
    private static BufferedImage mainBox = images[1];
    private static BufferedImage barista = images[2];
    private static BufferedImage chef = images[3];
    private static BufferedImage clown = images[4];
    private static BufferedImage dentist = images[5];
    private static BufferedImage doctor = images[6];
    private static BufferedImage model = images[7];
    private static BufferedImage police = images[8];
    private static BufferedImage programmer = images[9];
    private static BufferedImage security = images[10];
    private static BufferedImage highlight = images[11];
    private static BufferedImage notAbleToChange = images[12];
    private static BufferedImage simNameBox = images[13];
    private static BufferedImage simProfessionBox = images[14];
    private static BufferedImage help = images[15];

    // To select a profession and position them inside of the window
    private static int slotSelected = 0;
    private static int slotWidth = 203;
    private static int slotHeight = 72;
    private static int slotRow = 0;
    private static int slotCol = 0;
    private static int slotX = 71;
    private static int slotY = 167;

    // Get list of professions
    private static String[] professionNames = Profession.names;
    private static int[] professionSalaries = Profession.salaries;

    private static Sim currentSim = UserInterface.getCurrentSim();
    private static Profession currentProfession = currentSim.getProfession();
    private static Profession newProfession = new Profession(slotSelected);
    private static boolean isTheSameProfession = currentProfession.getName().equals(newProfession.getName());
    private static boolean ableToChange = currentSim.getDurationWorked() >= Consts.ONE_MINUTE * 12;
    private static boolean sufficientFunds;

    public static void update() {
        newProfession = new Profession(slotSelected);

        isTheSameProfession = currentProfession.getName().equals(newProfession.getName());
        sufficientFunds = currentSim.getMoney() >= (newProfession.getSalary() / 2);
        ableToChange = currentSim.getDurationWorked() >= Consts.ONE_MINUTE * 12;

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
            UserInterface.viewProfessions();
        }
        if (sufficientFunds && ableToChange && !isTheSameProfession && KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
            int currentSimMoney = currentSim.getMoney();
            
            currentSim.setMoney(currentSimMoney - (newProfession.getSalary() / 2));
            currentSim.setProfession(newProfession);
            currentSim.setDurationWorked(0);
        }

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
            if (slotCol > 0) {
                slotSelected--;
                slotCol--;
            }
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
            if (slotRow < 2) {
                slotSelected += 3;
                slotRow++;
            } 
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
            if (slotCol < 2) {
                slotSelected++;
                slotCol++;
            }
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
            if (slotRow > 0) {
                slotSelected -= 3;
                slotRow--;
            }
        }
    }

    public static void draw(Graphics2D g) {
        g.setColor(new Color(110, 196, 213));
        g.fillRect(0, 0, 800, 600);

        if (UserInterface.isHelped()) {
            g.drawImage(help, 0, 0, 800, 570, null);
        }

        g.drawImage(mainBox, 41, 113, null);

        drawSelector(g);

        drawProfessionBoxes(g);

        drawProfessionsInfo(g);

        drawSimInfo(g);

        pressEscapeToCancel(g);

        warning(g);
    }

    private static void drawSelector(Graphics2D g) {
        if (ableToChange && sufficientFunds) {
            g.drawImage(highlight, slotX + (slotWidth * slotCol) + (slotCol * 20), slotY + (slotHeight * slotRow) + (slotRow * 18), null);
        }
        else {
            g.drawImage(notAbleToChange, slotX + (slotWidth * slotCol) + (slotCol * 20), slotY + (slotHeight * slotRow) + (slotRow * 18), null);
        }
    }

    private static void drawProfessionBoxes(Graphics2D g) {
        // first column
        g.drawImage(barista, 75, 171, null);
        g.drawImage(chef, 298, 171, null);
        g.drawImage(clown, 521, 171, null);
        // second column
        g.drawImage(dentist, 75, 261, null);
        g.drawImage(doctor, 298, 261, null);
        g.drawImage(model, 521, 261, null);
        // third column
        g.drawImage(police, 75, 351, null);
        g.drawImage(programmer, 298, 351, null);
        g.drawImage(security, 521, 351, null);

        g.drawImage(simProfessionBox, 414, 502, null);
        g.drawImage(simNameBox, 228, 502, null);

        g.drawImage(titleBox, 236, 91, null);

        Font font;
        font = new Font("Inter", Font.BOLD, 20);
        g.setColor(Color.WHITE);
        g.setFont(font);

        UserInterface.drawCenteredText(g, titleBox, 236, 121, "Select a Profession", font);
    }

    private static void drawProfessionsInfo(Graphics2D g) {
        Font font = new Font("Inter", Font.BOLD, 14);
        g.setFont(font);

        for (int i = 2; i < 11; i++) {
            if (i >= 2 && i < 5) {
                UserInterface.drawCenteredText(g, images[i], 88 + ((slotWidth + 20) * ((i - 2) % 3)), 199, 20, professionNames[i - 2], font);
            }
            if (i >= 5 && i < 8) {
                UserInterface.drawCenteredText(g, images[i], 88 + ((slotWidth + 20) * ((i - 2) % 3)), 289, 20, professionNames[i - 2], font);
            }
            if (i >= 8 && i < 11) {
                UserInterface.drawCenteredText(g, images[i], 88 + ((slotWidth + 20) * ((i - 2) % 3)), 379, 20, professionNames[i - 2], font);
            }
        }

        font = new Font("Inter", Font.PLAIN, 10);
        g.setColor(new Color(61, 30, 45));
        g.setFont(font);
        for (int i = 2; i < 11; i++) {
            if (i >= 2 && i < 5) {
                UserInterface.drawCenteredText(g, images[i], 88 + ((slotWidth + 20) * ((i - 2) % 3)), 230, 20, "Salary: $" + professionSalaries[i - 2], font);
            }
            if (i >= 5 && i < 8) {
                UserInterface.drawCenteredText(g, images[i], 88 + ((slotWidth + 20) * ((i - 2) % 3)), 320, 20, "Salary: $" + professionSalaries[i - 2], font);
            }
            if (i >= 8 && i < 11) {
                UserInterface.drawCenteredText(g, images[i], 88 + ((slotWidth + 20) * ((i - 2) % 3)), 410, 20, "Salary: $" + professionSalaries[i - 2], font);
            }
        }
    }

    private static void drawSimInfo(Graphics2D g) {
        Font font = new Font("Inter", Font.BOLD, 14);
        g.setFont(font);
        
        Profession currentSimProfession = currentSim.getProfession();
        
        g.setColor(Color.WHITE);
        UserInterface.drawCenteredText(g, simNameBox, 228, 523, currentSim.getName(), font);
        g.setColor(new Color(61, 30, 45));
        UserInterface.drawCenteredText(g, simNameBox, 398, 523, currentSimProfession.getName(), font);

        if (!ableToChange) {
            int durationLeft = 720 - currentSim.getDurationWorked();

            g.setFont(new Font("Inter", Font.PLAIN, 9));
            UserInterface.drawCenteredText(g, mainBox, 135, 489, "you still need to work for " + durationLeft + " more seconds to change your profession", font);
        }
    }

    private static void pressEscapeToCancel(Graphics2D g) {
        g.setFont(new Font("Inter", Font.PLAIN, 9));
        g.drawString("press", 358, 548);
        g.setFont(new Font("Inter", Font.BOLD, 9));
        g.drawString("esc", 386, 548);
        g.setFont(new Font("Inter", Font.PLAIN, 9));
        g.drawString("to cancel", 405, 548);
    }

    private static void warning(Graphics2D g) {
        g.setColor(Color.BLACK);
        Font font = new Font("Inter", Font.PLAIN, 9);        
        g.setFont(font);
        UserInterface.drawCenteredText(g, titleBox, 236, 80, "you have to pay half of your new profession salary to change professions", font);
    }
}
