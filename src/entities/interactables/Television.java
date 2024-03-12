package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.main.KeyHandler;
import src.main.Consts;
import src.entities.sim.Sim;
import src.main.UserInterface;
import src.main.menus.InteractMenu;
import src.main.time.GameTime;

public class Television extends Interactables{
    // Attributes
    private int price = 100;
    private int watchDuration = Consts.ONE_SECOND * 40;
    private int karaokeDuration = Consts.ONE_SECOND * 30;
    private String activityStatus = "Karaoke-ing";

    // Images of the television
    private BufferedImage icon;
    private BufferedImage[] images;

    public Television() {
        super (
            "Television",
            "start karaoke-ing or watch the TV",
            0,
            2,
            2,
            2,
            1
        );

        setPrice(price);
        setDuration(watchDuration);

        this.icon = ImageLoader.loadTelevisionIcon();
        this.images = ImageLoader.loadTelevision();
    }

    public Television(int x, int y) {
        super (
            "Television",
            "start karaoke-ing or watch the TV",
            0,
            x,
            y,
            2,
            1
        );

        setPrice(price);
        setDuration(watchDuration);

        this.icon = ImageLoader.loadTelevisionIcon();
        this.images = ImageLoader.loadTelevision();
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
    public void interact(Sim sim){
        Thread choosingInteract = new Thread() {
            @Override
            public void run() {
                UserInterface.viewInteractions();

                while (UserInterface.isViewingInteractions()) {
                    if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
                        UserInterface.viewInteractions();
                        break;
                    }
                    if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
                        UserInterface.viewInteractions();
                        InteractMenu.slotSelected = -1;
                        return;
                    }
                }

                if (InteractMenu.slotSelected == 0) karaoke(sim);
                else if (InteractMenu.slotSelected == 1) watch(sim);
                InteractMenu.slotSelected = 0;
            }
        };
        choosingInteract.start();
    }

    private void karaoke(Sim sim) {
        Thread karaoke = new Thread() {
            @Override
            public void run() {
                images[2] = ImageLoader.changeSimColor(images[2], sim);
                images[3] = ImageLoader.changeSimColor(images[3], sim);

                sim.setStatus(activityStatus);
                changeOccupiedState();
                
                GameTime.addActivityTimer(sim, activityStatus, karaokeDuration, karaokeDuration);

                while (GameTime.isAlive(sim, activityStatus)) {
                    try {
                        setImageIndex(2);
                        Thread.sleep(Consts.THREAD_ONE_SECOND);
                        setImageIndex(3);
                        Thread.sleep(Consts.THREAD_ONE_SECOND);
                    }
                    catch (InterruptedException ie) {}
                }
                setImageIndex(0);

                changeOccupiedState();
                sim.setMood(sim.getMood() + 10);
                sim.setHunger(sim.getHunger() - 10);
                sim.resetStatus();

                // Reset the images
                images = ImageLoader.loadTelevision();
            }
        };
        karaoke.start();  
    }

    private void watch(Sim sim) {
        Thread watch = new Thread(){
            @Override
            public void run(){
                activityStatus = "Watching The TV";

                sim.setStatus(activityStatus);
                changeOccupiedState();
                setImageIndex(1);
                images[getImageIndex()] = ImageLoader.changeSimColor(images[getImageIndex()], sim);

                GameTime.addActivityTimer(sim, activityStatus, watchDuration, watchDuration);

                while (GameTime.isAlive(sim, activityStatus)) continue;

                setImageIndex(0);
                changeOccupiedState();
                sim.resetStatus();
                sim.setMood(sim.getMood() + 10);
                sim.setHealth(sim.getHealth() - 10);
                sim.setHunger(sim.getHunger() - 5);

                // Reset the images
                images = ImageLoader.loadTelevision();
            }
        };
        watch.start();
    }
}
