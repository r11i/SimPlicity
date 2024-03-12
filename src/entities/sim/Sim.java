package src.entities.sim;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

import src.assets.ImageLoader;
import src.entities.Entity;
import src.entities.handlers.CollisionHandler;
import src.entities.handlers.InteractionHandler;
import src.main.Consts;
import src.world.Room;
import src.world.House;

public class Sim extends Entity{
    // Attributes
    private String name;
    private int health;
    private int hunger;
    private int mood;
    private int money;
    private String status;
    private Profession profession;
    private Inventory inventory;

    // Supporting Attributes
    private boolean isBusy;
    private boolean hasAte;
    private int durationWorked;
    private int timeSlept;
    private int timeNotSlept;
    private int timeNotTakenLeak;
    private static int dayLastAddedSim = 1;

    // Attributes to indentify room and house easier
    private Room currentRoom;
    private House currentHouse;
    
    // Image of the sim
    private Color shirtColor;
    private BufferedImage[] images = new BufferedImage[12];

    // Collisions and interactions
    private CollisionHandler collisionHandler;
    private InteractionHandler interactionHandler;

    // CONSTRUCTOR
    public Sim(String name, Color shirtColor) {
        // Attributes
        super (
            4,
            4,
            1,
            1
        );
        
        this.name = name;
        this.health = 80;
        this.hunger = 80;
        this.mood = 80;
        this.money = 100;
        this.status = "Idle";
        this.profession = new Profession(); 
        this.inventory = new Inventory();
        this.isBusy = false;
        this.hasAte = false;
        this.durationWorked = 0;
        this.timeNotSlept = 0;
        this.timeNotTakenLeak = 0;

        // Load the image of the sim
        this.shirtColor = shirtColor;
        images = ImageLoader.loadSim(this);

        // Collisions and interactions
        collisionHandler = new CollisionHandler(this, currentRoom);
        interactionHandler = new InteractionHandler(this, currentRoom);
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getHunger() {
        return hunger;
    }

    public int getMood() {
        return mood;
    }
    
    public int getMoney() {
        return money;
    }

    public String getStatus() {
        return status;
    }
    
    public Profession getProfession() {
        return profession;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public boolean hasAte() {
        return hasAte;
    }

    public int getDurationWorked() {
        return durationWorked;
    }

    public int getTimeSlept() {
        return timeSlept;
    }

    public int getTimeNotSlept() {
        return timeNotSlept;
    }

    public int getTimeNotTakenLeak() {
        return timeNotTakenLeak;
    }

    public int getDayLastAddedSim() {
        return dayLastAddedSim;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public House getCurrentHouse() {
        return currentHouse;
    }

    public boolean isStatusCurrently(String status) {
        return this.status.equals(status);
    }

    public Color getShirtColor() {
        return shirtColor;
    }

    public InteractionHandler getInteractionHandler() {
        return interactionHandler;
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    // SETTERS
    public void setHealth(int health) {
        this.health = health;
        if (this.health > 100) this.health = 100;
        if (this.health < 0) {
            this.health = 0;
            this.hunger = 0;
            this.mood = 0;
        }
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
        if (this.hunger > 100) this.hunger = 100;
        if (this.hunger < 0) {
            this.health = 0;
            this.hunger = 0;
            this.mood = 0;
        }
    }

    public void setMood(int mood) {
        this.mood = mood;
        if (this.mood > 100) this.mood = 100;
        if (this.mood < 0) {
            this.health = 0;
            this.hunger = 0;
            this.mood = 0;
        }
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void resetStatus() {
        setStatus("Idle");
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void changeIsBusyState() {
        this.isBusy = !this.isBusy;
    }

    public void changeHasAteState() {
        this.hasAte = !this.hasAte;
    }

    public void setDurationWorked(int durationWorked) {
        this.durationWorked = durationWorked;
    }

    public void setTimeSlept(int timeSlept) {
        this.timeSlept = timeSlept;
    }

    public void setTimeNotSlept(int timeNotSlept) {
        this.timeNotSlept = timeNotSlept;

        if (this.timeNotSlept % Consts.ONE_MINUTE * 10 == 0) {
            this.health -= 5;
            this.mood -= 5;
        }
    }

    public void setTimeNotTakenLeak(int timeNotTakenLeak) {
        if (!this.hasAte) return;

        this.timeNotTakenLeak = timeNotTakenLeak;

        if (this.timeNotTakenLeak % Consts.ONE_MINUTE * 4 == 0) {
            this.health -= 5;
            this.mood -= 5;
        }
    }

    public void setDayLastAddedSim(int day) {
        dayLastAddedSim = day;
    }

    public void setCurrentHouse(House house) {
        this.currentHouse = house;
    }

    public void setCurrentRoom(Room room) {
        if (currentRoom != null) {
            this.currentRoom.removeSim(this);
        }
        
        this.currentRoom = room;
        this.currentRoom.addSim(this);
        collisionHandler = new CollisionHandler(this, room);
        interactionHandler = new InteractionHandler(this, room);
    }

    // OTHERS
    public void update() {
        if (!isStatusCurrently("Idle") || currentRoom.isEditingRoom()) {
            return;
        }
        
        if (isMoving() && !isBusy) {
            move(collisionHandler, interactionHandler);
        }
    }
    
    public void draw(Graphics2D g) {
        // Draw the appropriate image based on the direction the sim is facing
        int imageIndex = getDirection();
        if (!isBusy && isMoving() && !currentRoom.isEditingRoom()) {
            imageIndex += (int) ((getDirection() + (System.currentTimeMillis() / 250) % 2) + 4);
        }

        if (isStatusCurrently("Idle")) {
            g.drawImage(images[imageIndex], getX(), getY(), null);
        }
    }

    // ONLY FOR DEBUGGING
    public void drawSimStanding(Graphics2D g) {
        g.drawImage(images[2], getX(), getY(), null);
    }

    public void drawCollisionBox(Graphics2D g) {
        // Draw the sim collision area as a red rectangle
        g.setColor(new Color(255, 0, 0, 64)); // Transparent red color
        g.fillRect(getX() + 8, getY() + 15, getWidth() - 16, getHeight() - 16);
    }

    public void drawInteractionRange(Graphics2D g) {
        // Draw the interaction range as a yellow rectangle
        g.setColor(new Color(255, 255, 0, 128)); // Transparent yellow color
        g.fillRect(interactionHandler.getX(), interactionHandler.getY(), interactionHandler.getWidth(), interactionHandler.getHeight());
    }
}