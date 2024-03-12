package src.entities.interactables;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

import src.entities.Entity;
import src.entities.sim.Sim;
import src.items.Item;

public abstract class Interactables extends Entity implements Item {
    // Attributes
    private String name;
    private String interaction;
    private int price = 0;
    private int duration = 0;
    protected boolean occupied;
    private Rectangle bounds;
    private int imageIndex;

    // CONSTRUCTOR
    public Interactables(String name, String interaction, int imageIndex, int x, int y, int width, int height) {
        super (
            x,
            y,
            width,
            height
        );
        
        this.name = name;
        this.interaction = interaction;
        this.occupied = false;
        this.bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        this.imageIndex = imageIndex;
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public String getInteraction() {
        return interaction;
    }

    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }
    
    public boolean isOccupied() {
        return occupied;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public int getImageIndex() {
        return imageIndex;
    }
    
    // SETTERS
    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void changeOccupiedState() {
        if (!isOccupied()) {
            setImageIndex(getImageIndex() + 1);
        }
        else {
            setImageIndex(getImageIndex() - 1);
        }

        this.occupied = !this.occupied;
    }
    
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public void updateBounds() {
        this.bounds.setLocation(getX(), getY());
    }

    // OTHER
    public <T extends Interactables> void draw(Graphics2D g, T interactables) {
        g.drawImage(interactables.getImage(), interactables.getX(), interactables.getY(), null);
    }
    
    public abstract BufferedImage getIcon();
    public abstract BufferedImage getImage();
    public abstract void interact(Sim sim);

    // ONLY FOR DEBUGGING
    public void drawCollisionBox(Graphics2D g) {
        g.setColor(new Color(255, 0, 0, 64)); // Transparent red color
        g.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    // public void drawTimer(Graphics2D g) {
    //     g.setColor(Color.BLACK);
    //     Font font = new Font("Arial", Font.BOLD, 16);
    //     g.setFont(font);
        
    //     g.drawString("Duration: " + time.getDecrements(), 605, 60);
    // }
}