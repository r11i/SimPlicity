package src.entities.sim.actions;

import src.main.UserInterface;
import src.world.Room;

public class NonActiveActions {
    public static void showInventory() {
        UserInterface.inventory();
    }

    public static void editRoom(Room room) {
        room.selectObject();
    }
}
