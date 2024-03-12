package src.entities;

import src.entities.handlers.*;
import src.entities.sim.Sim;
import src.entities.interactables.Door;
import src.main.Consts;
import src.main.KeyHandler;

public abstract class Entity {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed = 5;
    private int direction = 2; // 0 = up, 1 = right, 2 = down, 3 = left

    // CONSTRUCTOR
    public Entity(int x, int y, int width, int height) {
        this.x = Consts.PLAY_ARENA_X_LEFT + (Consts.SCALED_TILE * x); // relative to the play area
        this.y = Consts.PLAY_ARENA_Y_UP + (Consts.SCALED_TILE * y); // relative to the play area
        this.width = Consts.SCALED_TILE * width;
        this.height = Consts.SCALED_TILE * height;
    }

    // GETTERS
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

    public int getDirection() {
        return direction;
    }

    // SETTERS
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPlayAreaX(int x) {
        this.x = Consts.PLAY_ARENA_X_LEFT + (Consts.SCALED_TILE * x);
    }

    public void setPlayAreaY(int y) {
        this.y = Consts.PLAY_ARENA_Y_UP + (Consts.SCALED_TILE * y);
    }

    // OTHERS
    public boolean isMoving() {
        if (KeyHandler.isKeyDown(KeyHandler.KEY_A) || KeyHandler.isKeyDown(KeyHandler.KEY_D) || 
            KeyHandler.isKeyDown(KeyHandler.KEY_W) || KeyHandler.isKeyDown(KeyHandler.KEY_S)) {
            return true;
        }
        return false;
    }

    public boolean isMovingDiagonally() {
        if ((KeyHandler.isKeyDown(KeyHandler.KEY_W) && KeyHandler.isKeyDown(KeyHandler.KEY_A)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_W) && KeyHandler.isKeyDown(KeyHandler.KEY_D)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_S) && KeyHandler.isKeyDown(KeyHandler.KEY_A)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_S) && KeyHandler.isKeyDown(KeyHandler.KEY_D))) {
            return true;
        }
        return false;
    }

    public void checkCollision(CollisionHandler collisionHandler, int newX, int newY) {
        if (this instanceof Sim) {
            if (collisionHandler.isCollision(newX, newY) || collisionHandler.isOutsidePlayArea(newX, newY)) {
                return;
            }
        }
        else if (this instanceof Door) {
            if (!collisionHandler.isAtAreaBorder(newX, newY)) {
                return;
            }
        }
        else {
            if (collisionHandler.isOutsidePlayArea(newX, newY)) {
                return;
            }
        }

        x = newX;
        y = newY;
        
        if (this instanceof Door) {
            Door door = (Door) this;
            door.changeDoorDirection(newX, newY);
        }
    }

    // FOR MOVING THE SIM
    public void move(CollisionHandler collisionHandler, InteractionHandler interactionHandler) {
        // Update the sim position when moving
        int newX = x;
        int newY = y;
        int initialSpeed = speed;

        if (isMovingDiagonally()) {
            speed *= 0.707;
        }

        if (isMoving()) {
            if (KeyHandler.isKeyDown(KeyHandler.KEY_W)) {
                newY -= speed;
                direction = 0;
                interactionHandler.moveUp(newX, newY);
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_D)) {
                newX += speed;
                direction = 1;
                interactionHandler.moveRight(newX, newY);
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_S)) {
                newY += speed;
                direction = 2;
                interactionHandler.moveDown(newX, newY);
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_A)) {
                newX -= speed;
                direction = 3;
                interactionHandler.moveLeft(newX, newY);
            }
            checkCollision(collisionHandler, newX, newY);
        }
        speed = initialSpeed;
    }

    // FOR MOVING OBJECTS
    public void move(CollisionHandler collisionHandler) {
        // Update the object position when moving
        int newX = x;
        int newY = y;
        speed = Consts.SCALED_TILE; // Move object by one tile

        if (isMoving()) {
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
                newX -= speed;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
                newX += speed;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
                newY -= speed;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
                newY += speed;
            }
            checkCollision(collisionHandler, newX, newY);
        }
    }
}
