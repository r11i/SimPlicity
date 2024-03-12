package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.entities.sim.Sim;
import src.main.Consts;
import src.main.UserInterface;
import src.world.Room;

public class Door extends Interactables {
    // Attributes
    private int price = 1500;
    private int duration = 0;
    private Room leadsIntoRoom;

    // Images of the door
    private BufferedImage icon;
    private BufferedImage[] images;

    // CONSTRUCTOR
    public Door(Room room) {
        super (
            "Door",
            "view active actions",
            2,
            4,
            5,
            1,
            1
        );

        setPrice(price);
        setDuration(duration);

        this.leadsIntoRoom = room;
        this.images = ImageLoader.loadDoor();

        if (room != null) {
            setImageIndex(1);
            setPlayAreaY(3);
            setInteraction("visit another room");
        }

        updateBounds();
    }

    public Door(Door door, Room room) {
        super (
            "Door",
            "visit another room",
            0,
            0,
            0,
            1,
            1
        );

        setPrice(price);
        setDuration(duration);

        this.leadsIntoRoom = room;
        this.images = ImageLoader.loadDoor();

        // Update the bounds based on the image index
        switch (door.getImageIndex()) {
            case 0:
                setImageIndex(2);
                setX(door.getX());
                setPlayAreaY(5);
                getBounds().setBounds(getX(), getY() + (Consts.SCALED_TILE - 24), Consts.SCALED_TILE, 24);
                break;
            case 1:
                setImageIndex(3);
                setPlayAreaX(0);
                setY(door.getY());
                getBounds().setBounds(getX(), getY(), 24, Consts.SCALED_TILE);
                break;
            case 2:
                setImageIndex(0);
                setX(door.getX());
                setPlayAreaY(0);
                getBounds().setBounds(getX(), getY(), Consts.SCALED_TILE, 24);
                break;
            case 3:
                setImageIndex(1);
                setPlayAreaX(5);
                setY(door.getY());
                getBounds().setBounds(getX() + (Consts.SCALED_TILE - 24), getY(), 24, Consts.SCALED_TILE);
                break;
            default:
                break;
        }
    }

    public void setLeadsIntoRoom(Room room) {
        leadsIntoRoom = room;
    }

    // IMPLEMENTATION OF ABSTRACT METHODS
    @Override
    public void updateBounds() {
        switch (this.getImageIndex()) {
            case 0:
                setPlayAreaY(0);
                getBounds().setBounds(getX(), getY(), Consts.SCALED_TILE, 24);
                break;
            case 1:
                setPlayAreaX(5);
                getBounds().setBounds(getX() + (Consts.SCALED_TILE - 24), getY(), 24, Consts.SCALED_TILE);
                break;
            case 2:
                setPlayAreaY(5);
                getBounds().setBounds(getX(), getY() + (Consts.SCALED_TILE - 24), Consts.SCALED_TILE, 24);
                break;
            case 3:
                setPlayAreaX(0);
                getBounds().setBounds(getX(), getY(), 24, Consts.SCALED_TILE);
                break;
            default:
                break;
        }
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
        if (leadsIntoRoom == null) {
            UserInterface.viewActiveActions();
            return;
        }
        if (leadsIntoRoom == sim.getCurrentRoom()) {
            return;
        }

        sim.setCurrentRoom(leadsIntoRoom);
        switch (getImageIndex()) {
            case 0:
                sim.setX(this.getX());
                sim.setY(Consts.PLAY_ARENA_Y_DOWN - Consts.TILE_SIZE * 2);
                break;
            case 1:
                sim.setY(this.getY());
                sim.setX(Consts.PLAY_ARENA_X_LEFT + Consts.TILE_SIZE * 2);
                break;
            case 2:
                sim.setX(this.getX());
                sim.setY(Consts.PLAY_ARENA_Y_UP + Consts.TILE_SIZE * 2);
                break;
            case 3:
                sim.setY(this.getY());
                sim.setX(Consts.PLAY_ARENA_X_RIGHT - Consts.TILE_SIZE * 2);
            }
        sim.getInteractionHandler().update();
    }

    // OTHERS
    public void changeDoorDirection(int newX, int newY) {
        if (newX == Consts.PLAY_ARENA_X_RIGHT) {
            ((Door) this).setImageIndex(1);
        }
        if (newX == Consts.PLAY_ARENA_X_LEFT) {
            ((Door) this).setImageIndex(3);
        }
        if (newY == Consts.PLAY_ARENA_Y_UP) {
            ((Door) this).setImageIndex(0);
        }
        if (newY == Consts.PLAY_ARENA_Y_DOWN) {
            ((Door) this).setImageIndex(2);
        }
    }

    public void rotate(int x, int y) {
        if (x == Consts.PLAY_ARENA_X_LEFT && y == Consts.PLAY_ARENA_Y_UP) {
            if (getImageIndex() == 0) {
                setImageIndex(3);
            }
            else {
                setImageIndex(0);
            }
        }
        if (x == Consts.PLAY_ARENA_X_LEFT && y == Consts.PLAY_ARENA_Y_DOWN) {
            if (getImageIndex() == 2) {
                setImageIndex(3);
            }
            else {
                setImageIndex(2);
            }
        }
        if (x == Consts.PLAY_ARENA_X_RIGHT && y == Consts.PLAY_ARENA_Y_UP) {
            if (getImageIndex() == 0) {
                setImageIndex(1);
            }
            else {
                setImageIndex(0);
            }
        }
        if (x == Consts.PLAY_ARENA_X_RIGHT && y == Consts.PLAY_ARENA_Y_DOWN) {
            if (getImageIndex() == 2) {
                setImageIndex(1);
            }
            else {
                setImageIndex(2);
            }
        }
    }
}
