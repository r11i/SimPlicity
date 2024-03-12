package src.entities.sim.actions;

import java.util.ArrayList;

import src.main.UserInterface;
import src.main.time.GameTime;
import src.main.Consts;
import src.world.Room;
import src.entities.sim.Sim;
import src.entities.interactables.Door;
import src.entities.interactables.Interactables;

public class UpgradeActions {
    public static void addRoom(String name) {
        Sim currentSim = UserInterface.getCurrentSim();
        Room currentRoom = currentSim.getCurrentRoom();

        Thread addNewRoomThread = new Thread() {
            @Override
            public void run() {
                int buildDuration = Consts.ONE_MINUTE * 18;

                Room newRoom = new Room(name);
                Door newDoor = new Door(currentRoom);
                
                currentRoom.addObject(newDoor);
                while (true) {
                    synchronized (currentRoom) {
                        if (!currentRoom.isEditingRoom()) {
                            break;
                        }
                    }
                }

                GameTime.addActivityTimer(currentSim, "Build Room", buildDuration, buildDuration);

                ArrayList<Interactables> listOfObjects = currentRoom.getListOfObjects();
                if (listOfObjects.contains(newDoor)) {
                    currentSim.setMoney(currentSim.getMoney() - newDoor.getPrice());
                }

                while (GameTime.isAlive(currentSim, "Build Room")) continue;

                newDoor.setLeadsIntoRoom(newRoom);
                newRoom.getListOfObjects().add(new Door(newDoor, currentRoom));
            }
        };
        addNewRoomThread.start();
    }

    public static void viewStore() {
        UserInterface.viewStore();
    }
}
