package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.main.Consts;
import src.main.time.GameTime;
import src.entities.sim.Sim;

public class Aquarium extends Interactables {
    // Attributes
    private int price = 50;
    private int duration = Consts.ONE_SECOND * 10;
    private String activityStatus = "Feeding The Fish";
    private Thread animateNotOccupiedThread;
    private Thread animateOccupiedThread;

    // Images of the aquarium
    private BufferedImage icon;
    private BufferedImage[] images;

    // CONSTRUCTOR
    public Aquarium () {
        super (
            "Aquarium",
            "feed the fish",
            0,
            2,
            1,
            2,
            1
        );

        setPrice(price);
        setDuration(duration);
        getBounds().setSize(64, 64);

        // Load the icon and image of the aquarium
        icon = ImageLoader.loadAquariumIcon();
        images = ImageLoader.loadAquarium();

        animateNotOccupied();
    }

    public Aquarium (int x, int y) {
        super (
            "Aquarium",
            "feed the fish",
            0,
            x,
            y,
            2,
            1
        );

        setPrice(price);
        setDuration(duration);
        getBounds().setSize(64, 64);

        // Load the icon and image of the aquarium
        icon = ImageLoader.loadAquariumIcon();
        images = ImageLoader.loadAquarium();

        animateNotOccupied();
    }

    private void animateNotOccupied() {
        animateNotOccupiedThread = new Thread() {
            @Override
            public void run() {
                while (!isOccupied()) {
                    try {
                        setImageIndex(0);
                        Thread.sleep(Consts.THREAD_ONE_SECOND);
                        setImageIndex(1);
                        Thread.sleep(Consts.THREAD_ONE_SECOND);
                    }
                    catch (InterruptedException ie) {}
                }
            }
        };
        animateNotOccupiedThread.start();
    }

    private void animateOccupied(Sim sim) {
        animateOccupiedThread = new Thread() {
            @Override
            public void run() {
                images[2] = ImageLoader.changeSimColor(images[2], sim);
                images[3] = ImageLoader.changeSimColor(images[3], sim);
                while (isOccupied()) {
                    try {
                        setImageIndex(2);
                        Thread.sleep(Consts.THREAD_ONE_SECOND);
                        setImageIndex(3);
                        Thread.sleep(Consts.THREAD_ONE_SECOND);
                    }
                    catch (InterruptedException ie) {}
                }
            }
        };
        animateOccupiedThread.start();
    }

    @Override
    public void changeOccupiedState() {
        this.occupied = !this.occupied;
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
        Thread feedingfish = new Thread() {
            @Override
            public void run() {
                changeOccupiedState();
                sim.setStatus(activityStatus);

                animateNotOccupiedThread.interrupt();
                animateOccupied(sim);
                
                GameTime.addActivityTimer(sim, activityStatus, duration, duration);

                while (GameTime.isAlive(sim, activityStatus)) continue;

                changeOccupiedState();
                animateNotOccupied();
                sim.resetStatus();
                sim.setMood(sim.getMood() + 5); // increase sim's mood

                // reset the images
                images = ImageLoader.loadAquarium();
            }
        };
        feedingfish.start();
    }
}