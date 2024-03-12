package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.main.Consts;
import src.main.KeyHandler;
import src.entities.sim.Sim;
import src.main.UserInterface;
import src.entities.sim.Inventory;
import src.main.menus.InteractMenu;
import src.main.time.GameTime;

public class TableAndChair extends Interactables {
    private BufferedImage icon = ImageLoader.loadTableAndChairIcon();
    private BufferedImage[] images = ImageLoader.loadTableAndChair();

    private int price = 50;
    private int readDuration = Consts.ONE_SECOND * 30;
    private String activityStatus = "Reading a Book";

    public TableAndChair() {
        super (
            "Table and Chair",
            "eat or read a book",
            0,
            1,
            2,
            1,
            3
        );

        setPrice(price);

        getBounds().setSize((Consts.SCALED_TILE * 3) - 48, (Consts.SCALED_TILE * 3) - 48);
        updateBounds();
    }

    public TableAndChair(int x, int y) {
        super (
            "Table and Chair",
            "eat or read a book",
            0,
            1,
            x,
            y,
            3
        );

        setPrice(price);

        getBounds().setSize(16, 16);
        updateBounds();
    }

    @Override
    public void updateBounds() {
        getBounds().setLocation(getX() + 8, getY() + 12);
    }

    public void resetImages() {
        images = ImageLoader.loadTableAndChair();
    }

    public void changeOccupiedState(Sim sim) {
        if (sim.isStatusCurrently("Idle")) {
            setImageIndex(0);
        }
        if (sim.isStatusCurrently("Eating")) {
            images[1] = ImageLoader.changeSimColor(images[1], sim);
            images[2] = ImageLoader.changeSimColor(images[2], sim);

            Thread eatingAnimation = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            setImageIndex(1);
                            Thread.sleep(Consts.THREAD_ONE_SECOND / 2);
                            setImageIndex(2);
                            Thread.sleep(Consts.THREAD_ONE_SECOND / 2);

                            if (sim.isStatusCurrently("Idle")) break;
                        }
                        catch (InterruptedException ie) {}
                    }
                    setImageIndex(0);
                }
            };
            eatingAnimation.start();
        }
        if (sim.isStatusCurrently("Reading a Book")) {
            setImageIndex(3);
        }

        this.occupied = !this.occupied;
    }

    public void setImages(BufferedImage[] images) {
        this.images = images;
    }

    public void setImageAtIndex(int index, BufferedImage image) {
        images[index] = image;
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
    public void interact(Sim sim) {
        Thread choosingInteractionThread = new Thread() {
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

                if (InteractMenu.slotSelected == 0) eat(sim);
                else if (InteractMenu.slotSelected == 1) readABook(sim);
                InteractMenu.slotSelected = 0;
            }
        };
        choosingInteractionThread.start();
    }

    private void eat(Sim sim) {
        Inventory simInventory = sim.getInventory();

        sim.changeIsBusyState();
        simInventory.changeIsOpen();
        simInventory.chooseFood();

        Thread chooseFood = new Thread() {
            @Override
            public void run() {
                boolean running = true;
                while (running) {
                    if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
                        sim.changeIsBusyState();
                        simInventory.changeIsOpen();
                        simInventory.chooseFood();
                        running = false;;
                    }

                    if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
                        sim.changeIsBusyState();
                        simInventory.interact();
                        simInventory.changeIsOpen();
                        simInventory.chooseFood();
                        running = false;
                    }
                }
            }
        };
        chooseFood.start();
    }
    
    private void readABook(Sim sim) {
        Thread readingABook = new Thread() {
            @Override
            public void run() {
                sim.setStatus(activityStatus);
                changeOccupiedState(sim);

                images[getImageIndex()] = ImageLoader.changeSimColor(images[getImageIndex()], sim);
                
                GameTime.addActivityTimer(sim, activityStatus, readDuration, readDuration);

                while (GameTime.isAlive(sim, activityStatus)) continue;

                sim.resetStatus();
                changeOccupiedState(sim);
                sim.setMood(sim.getMood() + 10);
                sim.setHunger(sim.getHunger() - 10);

                // Reset the images
                images = ImageLoader.loadTableAndChair();
            }
        };
        readingABook.start();
    }
}
