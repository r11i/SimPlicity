package src.world;

import java.awt.event.KeyEvent;

import src.main.Consts;
import src.main.KeyHandler;
import src.entities.sim.Sim;
import src.main.UserInterface;
import src.main.panels.CreateSimPanel;
import src.main.panels.GamePanel;
import src.main.time.GameTime;

public class Cursor {
    // Location inside of the world
    private int x;
    private int y;
    private World world;
    private boolean gridMovement = false;

    // Constructor
    public Cursor(int x, int y, World world){
        this.x = x;
        this.y = y;
        this.world = world;
    }

    // Getter and setter
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getGridX() {
        return x / Consts.TILE_SIZE;
    }

    public int getGridY() {
        return y / Consts.TILE_SIZE;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Others
    public boolean isAboveHouse() {
        int x = getGridX();
        int y = getGridY();
        boolean isOccupied = false; // Initialize the status of occupation in newHouse location

        if (world.getMap(x, y) == 1) {
            isOccupied = true;
        }
        return isOccupied;
    }

    private boolean isMovingDiagonally() {
        if ((KeyHandler.isKeyDown(KeyHandler.KEY_W) && KeyHandler.isKeyDown(KeyHandler.KEY_A)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_W) && KeyHandler.isKeyDown(KeyHandler.KEY_D)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_S) && KeyHandler.isKeyDown(KeyHandler.KEY_A)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_S) && KeyHandler.isKeyDown(KeyHandler.KEY_D))) {
            return true;
        }
        return false;
    }

    public void update(){
        int upperX = (Consts.TILE_SIZE * 64) - 14;
        int upperY = (Consts.TILE_SIZE * 64) - 14;
        int newX = x;
        int newY = y;
        int speed = 5;
        int initialSpeed = speed;
        
        if (KeyHandler.isKeyPressed(KeyEvent.VK_SHIFT)) {
            gridMovement = !gridMovement;
        }

        if (gridMovement) {
            upperX = (Consts.TILE_SIZE * 64);
            upperY = (Consts.TILE_SIZE * 64);
            // Move the cursor to the nearest grid
            newX -= (newX % Consts.TILE_SIZE);
            newY -= (newY % Consts.TILE_SIZE);

            if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
                newX -= Consts.TILE_SIZE;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
                newX += Consts.TILE_SIZE;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
                newY -= Consts.TILE_SIZE;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
                newY += Consts.TILE_SIZE;  
            }
        }
        else {
            if (isMovingDiagonally()) {
                speed *= 0.707;
            }
            
            if (KeyHandler.isKeyDown(KeyHandler.KEY_A)) {
                newX -= speed;
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_D)) {
                newX += speed;
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_W)) {
                newY -= speed;
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_S)) {
                newY += speed;  
            }
            speed = initialSpeed;
        }

        if ((newX >= 0 && newX < upperX) && (newY >= 0 && newY < upperY)) {
            x = newX;
            y = newY;
        }
    }

    public void enterPressed() {
        if (world.isAdding()) {
            if (isAboveHouse()) return;
            
            if (!isAboveHouse()) {
                world.addHouse(CreateSimPanel.roomName);
                CreateSimPanel.init();
            }
        }

        if (isAboveHouse()) enterHouse();
    }

    private void enterHouse() {
        int x = getGridX();
        int y = getGridY();
        Sim currentSim;
        House houseToVisit, currentHouse;
        Room roomToVisit;

        try {
            currentSim = UserInterface.getCurrentSim();
            currentHouse = currentSim.getCurrentHouse();
            houseToVisit = world.getHouse(x, y);
            roomToVisit = houseToVisit.getRoomWhenEntered();

            if (currentHouse == houseToVisit) return;
    
            if (isAboveHouse()) {
                if (world.isAdding()) {
                    int newSim = world.getListOfSim().size() - 1;
        
                    currentSim = world.getSim(newSim);
                    if (currentSim.isBusy()) currentSim.changeIsBusyState();
                    
                    UserInterface.setCurrentSim(currentSim);
                    world.changeIsAddingState();
                }
                else {
                    int x1 = currentHouse.getX(); int x2 = houseToVisit.getX();
                    int y1 = currentHouse.getY(); int y2 = houseToVisit.getY();
                    double deltaXsquared = Math.pow((double) (x2 - x1), 2);
                    double deltaYsquared = Math.pow((double) (y2 - y1), 2);
                    int duration = (int) Math.sqrt(deltaXsquared + deltaYsquared);

                    GameTime.decreaseTimeRemaining(duration);

                    for (int i = 1; i <= duration; i++) {
                        if (i % 30 == 0) {
                            currentSim.setMood(currentSim.getMood() + 10);
                            currentSim.setHunger(currentSim.getHunger() - 10);
                        }
                    }
                }

                currentSim.setCurrentHouse(houseToVisit);
                currentSim.setCurrentRoom(roomToVisit);
                UserInterface.viewWorld();
            }

            if (GamePanel.isCurrentState("Placing a new house") || 
                GamePanel.isCurrentState("Starting a new game: Placing a new house")) {
                GamePanel.gameState = "Playing";
            }
        }
        catch (Exception e) {System.out.println("Unexpected Error!");}
    }
}
