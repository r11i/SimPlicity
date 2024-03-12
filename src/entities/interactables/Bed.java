package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.main.Consts;
import src.main.KeyHandler;
import src.main.UserInterface;
import src.main.menus.InteractMenu;
import src.assets.ImageLoader;
import src.main.time.GameTime;
import src.entities.sim.Sim;

public class Bed extends Interactables{
    // Types of beds
    private static String[] names = {
        "Single Bed",
        "Queen Size Bed",
        "King Size Bed"
    };
    private static int[] width = {
        4,
        4,
        5
    };
    private static int[] height = {
        1,
        2,
        2
    };
    private static int[] prices = {
        50,
        100,
        150
    };

    // Attributes
    private int duration = Consts.ONE_MINUTE * 3;
    private String activityStatus = "Sleeping";

    // Image of the beds
    private BufferedImage[] icons;
    private BufferedImage[] images;

    // CONSTRUCTOR
    public Bed(int imageIndex) {
        super (
            names[imageIndex],
            "sleep",
            imageIndex,
            1,
            2,
            width[imageIndex],
            height[imageIndex]
        );
        if (imageIndex == 1) {
            setPlayAreaY(1);
        }
        else if (imageIndex == 2) {
            setPlayAreaX(0);
            setPlayAreaY(2);
        }

        setPrice(prices[imageIndex]);
        setDuration(duration);

        // Load the image of the beds
        icons = ImageLoader.loadBedsIcons();
        images = ImageLoader.loadBeds();
    }

    public Bed(int x, int y, int imageIndex) {
        super (
            names[imageIndex],
            "sleep",
            imageIndex,
            x,
            y,
            width[imageIndex],
            height[imageIndex]
        );

        setPrice(prices[imageIndex]);
        setDuration(duration);

        // Load the image of the beds
        icons = ImageLoader.loadBedsIcons();
        images = ImageLoader.loadBeds();
    }

    @Override
    public void changeOccupiedState() {
        if (!isOccupied()) {
            setImageIndex(getImageIndex() + 3);
        }
        else {
            setImageIndex(getImageIndex() - 3);
        }

        this.occupied = !this.occupied;
    }

    // IMPLEMENTATION OF ABSTRACT METHODS
    @Override
    public BufferedImage getIcon() {
        return icons[getImageIndex() % 3];
    }

    @Override
    public BufferedImage getImage() {
        return images[getImageIndex()];
    }

    @Override
    public void interact(Sim sim) {
        Thread interacting = new Thread() {
            @Override
            public void run() {
                UserInterface.viewInteractions();
                
                while (UserInterface.isViewingInteractions()) {
                    if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
                        UserInterface.viewInteractions();
                        duration = InteractMenu.sleepDuration;
                        break;
                    }
                    if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
                        UserInterface.viewInteractions();
                        InteractMenu.slotSelected = -1;
                        InteractMenu.sleepDuration = Consts.ONE_MINUTE * 3;
                        return;
                    }
                }
                InteractMenu.slotSelected = 0;

                changeOccupiedState();
                sim.setStatus(activityStatus);
                
                images[getImageIndex()] = ImageLoader.changeSimColor(images[getImageIndex()], sim);
                
                GameTime.addActivityTimer(sim, activityStatus, duration, duration);

                while (GameTime.isAlive(sim, activityStatus)) continue;
                
                changeOccupiedState();

                sim.resetStatus();
                sim.setTimeNotSlept(0);
                sim.setHealth(sim.getHealth() + 30);
                sim.setMood(sim.getMood() + 20);

                // Reset the images
                images = ImageLoader.loadBeds();
            }
        };
        interacting.start();
    }
}