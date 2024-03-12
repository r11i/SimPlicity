package src.entities.handlers;

import java.awt.Rectangle;
import java.util.ArrayList;

import src.entities.*;
import src.entities.interactables.Door;
import src.entities.interactables.Interactables;
import src.entities.sim.Sim;
import src.main.Consts;
import src.world.Room;

public class CollisionHandler {
    private Entity entity;
    private Room currentRoom;
    
    public CollisionHandler(Entity entity, Room room) {
        this.entity = entity;
        this.currentRoom = room;
    }

    public boolean isOutsidePlayArea(int x, int y) {
        if (entity instanceof Sim) {
            return (x < Consts.PLAY_ARENA_X_LEFT) || (x > Consts.PLAY_ARENA_X_RIGHT) ||
                    (y < Consts.PLAY_ARENA_Y_UP) || (y > Consts.PLAY_ARENA_Y_DOWN);
        }
        else {
            return (x < Consts.PLAY_ARENA_X_LEFT) || (x + entity.getWidth() - Consts.SCALED_TILE > Consts.PLAY_ARENA_X_RIGHT) ||
                    (y < Consts.PLAY_ARENA_Y_UP) || (y + entity.getHeight() - (Consts.SCALED_TILE) > Consts.PLAY_ARENA_Y_DOWN);
        }
    }

    public boolean isAtAreaBorder(int x, int y) {
        if (((x >= Consts.PLAY_ARENA_X_LEFT && x <= Consts.PLAY_ARENA_X_RIGHT) && y == Consts.PLAY_ARENA_Y_UP) ||
            ((x >= Consts.PLAY_ARENA_X_LEFT && x <= Consts.PLAY_ARENA_X_RIGHT) && y == Consts.PLAY_ARENA_Y_DOWN) ||
            ((y >= Consts.PLAY_ARENA_Y_UP && y <= Consts.PLAY_ARENA_Y_DOWN) && x == Consts.PLAY_ARENA_X_LEFT) ||
            ((y >= Consts.PLAY_ARENA_Y_UP && y <= Consts.PLAY_ARENA_Y_DOWN) && x == Consts.PLAY_ARENA_X_RIGHT)) {
            return true;
        }
        return false;
    }
    
    public boolean isCollision(int x, int y) {
        Rectangle newEntity;
        
        if (entity instanceof Sim) {
            newEntity = new Rectangle(x + 8, y + 15, entity.getWidth() - 16, entity.getHeight() - 16);  
            // Values subtracted to adjust the Sim collision box according to the image  
        }
        else {
            newEntity = new Rectangle(x, y, entity.getWidth(), entity.getHeight());
        }

        ArrayList<Interactables> listOfObjects = currentRoom.getListOfObjects();         
        for (Interactables object : listOfObjects) {
            if (newEntity.intersects(object.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollidingWithSim(int x, int y, ArrayList<Sim> listOfSims) {
        Rectangle newEntity;
        Rectangle Sim;

        newEntity = new Rectangle(x, y, entity.getWidth(), entity.getHeight());
        for (Sim sim : listOfSims) {
            Sim = new Rectangle(sim.getX() + 8, sim.getY() + 15, sim.getWidth() - 16, sim.getHeight() - 16);  
            if (newEntity.intersects(Sim)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWallOccupied(Door door) {
        for (Interactables object : currentRoom.getListOfObjects()) {
            if (object instanceof Door) {
                if (door.getImageIndex() == object.getImageIndex()) {
                    return true;
                }
            }
        }
        return false;
    }
}


