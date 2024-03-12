package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.main.KeyHandler;
import src.entities.sim.Sim;
import src.main.UserInterface;

public class Clock extends Interactables {
    // Attributes
    private int price = 10;

    // Images of the shower
    private BufferedImage icon;
    private BufferedImage image;

    // CONSTRUCTOR
    public Clock() {
        super (
            "Clock",
            "look at the time",
            0,
            3,
            1,
            1,
            1
        );

        setPrice(price);

        // Load the icon and image of the shower
        icon = ImageLoader.loadClockIcon();
        image = ImageLoader.loadClock();
    }

    public Clock(int x, int y) {
        super (
            "Clock",
            "look at the time",
            0,
            x,
            y,
            1,
            1
        );

        setPrice(price);

        // Load the icon and image of the shower
        icon = ImageLoader.loadClockIcon();
        image = ImageLoader.loadClock();
    }

    @Override
    public BufferedImage getIcon() {
        return icon;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void interact (Sim sim){
        UserInterface.viewTime();
        sim.changeIsBusyState();

        Thread viewingTimeThread = new Thread() {
            @Override
            public void run() {
                while (UserInterface.isViewingTime()) {
                    if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
                        UserInterface.viewTime();
                        sim.changeIsBusyState();
                    }
                }
            }
        };
        viewingTimeThread.start();
    }
}