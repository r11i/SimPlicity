package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.main.Consts;
import src.main.time.GameTime;
import src.entities.sim.Sim;

public class Shower extends Interactables {
    // Attributes
    private int price = 65;
    private int duration = Consts.ONE_SECOND * 40;
    private String activityStatus = "Showering";

    // Images of the shower
    private BufferedImage icon;
    private BufferedImage[] images;

    // CONSTRUCTOR
    public Shower() {
        super (
            "Shower",
            "take a shower",
            0,
            3,
            1,
            1,
            2
        );

        setPrice(price);
        setDuration(duration);

        // Load the icon and image of the shower
        icon = ImageLoader.loadShowerIcon();
        images = ImageLoader.loadShower();
    }

    public Shower(int x, int y) {
        super (
            "Shower",
            "take a shower",
            0,
            x,
            y,
            1,
            2
        );

        setPrice(price);
        setDuration(duration);

        // Load the icon and image of the shower
        icon = ImageLoader.loadShowerIcon();
        images = ImageLoader.loadShower();
    }

    @Override
    public BufferedImage getIcon() {
        return icon;
    }

    @Override
    public BufferedImage getImage() {
        return images[getImageIndex()];
    }

    @Override
    public void interact (Sim sim){
        Thread showering = new Thread() {
            @Override
            public void run() {
                changeOccupiedState();
                sim.setStatus(activityStatus);
                
                images[getImageIndex()] = ImageLoader.changeSimColor(images[getImageIndex()], sim);

                GameTime.addActivityTimer(sim, activityStatus, duration, duration);

                while (GameTime.isAlive(sim, activityStatus)) continue;

                changeOccupiedState();
                sim.resetStatus();
                sim.setHealth(sim.getHealth() + 10); // increase sim's health
                sim.setMood(sim.getMood() + 15); // increase sim's mood

                // Reset the images
                images = ImageLoader.loadShower();
            }
        };
        showering.start();
    }
    
}