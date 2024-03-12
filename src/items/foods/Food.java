package src.items.foods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.entities.handlers.InteractionHandler;
import src.entities.interactables.Interactables;
import src.entities.interactables.TableAndChair;
import src.entities.interactables.TrashBin;
import src.entities.sim.Sim;
import src.main.Consts;
import src.main.time.GameTime;
import src.world.Room;

public abstract class Food {
    private String name;
    private int hungerPoint;
    private int price;
    private int imageIndex;

    public Food(String name, int hungerPoint, int price, int imageIndex) {
        this.name = name;
        this.hungerPoint = hungerPoint;
        this.price = price;
        this.imageIndex = imageIndex;
    }

    public String getName() {
        return name;
    }

    public int getHungerPoint() {
        return hungerPoint;
    }

    public int getPrice() {
        return price;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void eat(Sim sim) {
        InteractionHandler simInteract = sim.getInteractionHandler();
        Interactables object = simInteract.getInteractableObject();

        if (!(object instanceof TableAndChair)) return;

        TableAndChair tableAndChair = (TableAndChair) object;

        Thread eating = new Thread() {
            @Override
            public void run() {
                int duration = Consts.ONE_SECOND * 30;
                sim.setStatus("Eating");
                
                tableAndChair.changeOccupiedState(sim);
                GameTime.addActivityTimer(sim, "Eating", duration, duration);

                while (GameTime.isAlive(sim, "Eating")) continue;
                
                sim.resetStatus();

                if (!sim.hasAte()) sim.changeHasAteState();
                
                sim.setHunger(sim.getHunger() + hungerPoint);
                tableAndChair.changeOccupiedState(sim);
                tableAndChair.resetImages();

                fillTrashBin(sim);
            }
        };
        eating.start();
    }

    public abstract BufferedImage getIcon();

    private void fillTrashBin(Sim sim) {
        Room currentRoom = sim.getCurrentRoom();
        ArrayList<Interactables> listOfObjects = currentRoom.getListOfObjects();

        for (Interactables object : listOfObjects) {
            if (!(object instanceof TrashBin)) continue;

            TrashBin trashBin = (TrashBin) object;
            if (trashBin.getImageIndex() == 0 || trashBin.getImageIndex() == 2) {
                trashBin.setImageIndex(1);
            }
        }
    }
}
