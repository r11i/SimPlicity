package src.entities.handlers;

import java.awt.Rectangle;
import java.util.ArrayList;

import src.entities.*;
import src.entities.interactables.Interactables;
import src.world.Room;

public class InteractionHandler {
    // Attributes
    private Entity entity;
    private Rectangle interactionRange;
    private Room currentRoom;
    private int x;
    private int y;
    private int width;
    private int height;

    // CONSTRUCTOR
    public InteractionHandler(Entity entity, Room currentRoom) {
        this.entity = entity;
        this.currentRoom = currentRoom;
        this.x = entity.getX();
        this.y = entity.getY() + entity.getHeight();
        this.width = entity.getWidth();
        this.height = entity.getHeight() / 3;
        interactionRange = new Rectangle(x, y, width, height);
    }

    // GETTER
    public Entity getEntity() {
        return entity;
    }

    public Rectangle getInteractionRange() {
        return interactionRange;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isObjectInRange() {
        ArrayList<Interactables> listOfObjects = currentRoom.getListOfObjects(); 

        for (Interactables object : listOfObjects) {
            if (getInteractionRange().intersects(object.getBounds())) {
                return true;
            }
        }
        return false;
    }
    
    public Interactables getInteractableObject() {
        ArrayList<Interactables> listOfObjects = currentRoom.getListOfObjects(); 
        
        for (Interactables object : listOfObjects) {
            if (getInteractionRange().intersects(object.getBounds())) {
                return object;
            }
        }
        return null;
    }
    
    // OTHERS
    public void update() {
        switch (entity.getDirection()) {
            case 0:
                moveUp(entity.getX(), entity.getY());
                break;
            case 1:
                moveRight(entity.getX(), entity.getY());
                break;
            case 2:
                moveDown(entity.getX(), entity.getY());
                break;
            case 3:
                moveLeft(entity.getX(), entity.getY());
                break;
            default:
                break;
        }
    }

    public void moveLeft(int newX, int newY) {
        x = newX - (entity.getWidth()) / 3;
        y = newY;
        width = entity.getWidth() / 3;
        height = entity.getHeight();

        interactionRange.setSize(width, height);
        interactionRange.setLocation(x, y);
    }

    public void moveRight(int newX, int newY) {
        x = newX + entity.getWidth();
        y = newY;
        width = entity.getWidth() / 3;
        height = entity.getHeight();

        interactionRange.setSize(width, height);
        interactionRange.setLocation(x, y);
    }

    public void moveUp(int newX, int newY) {
        x = newX;
        y = newY - (entity.getHeight() / 3);
        width = entity.getWidth();
        height = entity.getHeight() / 3;

        interactionRange.setSize(width, height);
        interactionRange.setLocation(x, y);
    }

    public void moveDown(int newX, int newY) {
        x = newX;
        y = newY + entity.getHeight();
        width = entity.getWidth();
        height = entity.getHeight() / 3;

        interactionRange.setSize(width, height);
        interactionRange.setLocation(x, y);
    }
}
