package src.world;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.*;

import src.assets.ImageLoader;
import src.entities.interactables.Door;
import src.entities.sim.Sim;
import src.main.Consts;
import src.main.KeyHandler;
import src.main.UserInterface;
import src.main.panels.CreateSimPanel;
import src.main.panels.GamePanel;
import src.main.panels.PanelHandler;
import src.main.time.GameTime;

public class World {
    // Attributes
    private int[][] map = new int[64][64];
    private ArrayList<Sim> listOfSim;
    private ArrayList<House> listOfHouse;
    
    // State of the world (is adding a house or selecting a house to visit)
    private boolean isAdding = false;

    // Images of the world
    private BufferedImage[] images;
    
    // Cursor position
    private Cursor cursor;
    
    // Viewable world inside of the window (32 x 32 grid)
    private int viewableGrid = (Consts.TILE_SIZE * 32) - 1;
    private int topLeftX = 26;
    private int topLeftY = 26;

    // Bounds for each quarter
    private int lowerBoundsX, upperBoundsX;
    private int lowerBoundsY, upperBoundsY;

    // Constructor 
    public World() {
        // Attributes
        listOfSim = new ArrayList<>();
        listOfHouse = new ArrayList<>();

        // Load the images of the world
        this.images = ImageLoader.loadWorld();

        // Load the initial state of the map
        for (int y = 0 ; y < 64 ; y++) {
            for (int x = 0 ; x < 64 ; x++) {
                map[y][x] = 0;
            }
        }

        // Initialize the cursor in the center of the grid
        this.cursor = new Cursor(Consts.TILE_SIZE * 16, Consts.TILE_SIZE * 16, this);
    }

    // Getter and setter
    public int getMap(int x, int y) {
        return map[y][x];
    }
    
    public ArrayList<Sim> getListOfSim() {
        return listOfSim;
    }

    public Sim getSim(int index) {
        return listOfSim.get(index);
    }

    public ArrayList<House> getListOfHouse() {
        return listOfHouse;
    }

    public House getHouse(int x, int y){
        for (House house : getListOfHouse()){
            if (house.getX() == x && house.getY() == y){
                return house;
            }
        }
        return null;
    }

    public boolean isAdding() {
        return isAdding;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setMap(int x, int y, int value) {
        map[y][x] = value;
    }

    public void addSim(Sim sim) {
        listOfSim.add(sim);
    }

    public void addHouse(String roomName) {
        int x = cursor.getGridX();
        int y = cursor.getGridY();

        Sim newSim = getSim(listOfSim.size() - 1);
        newSim.setDayLastAddedSim(GameTime.day);

        Room newRoom = new Room(roomName);
        newRoom.getListOfObjects().add(new Door(null));
        newRoom.getListOfObjects().get(0).setInteraction("view active actions");

        House newHouse = new House(x, y, this, newSim, newRoom);

        listOfHouse.add(newHouse);
    }

    public void changeIsAddingState() {
        this.isAdding = !this.isAdding;
    }

    public void reset() {
        cursor.setX(Consts.TILE_SIZE * 16);
        cursor.setY(Consts.TILE_SIZE * 16);
    }

    // Others
    public void update() {
        if (UserInterface.isViewingWorld()) {
            cursor.update();
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
            cursor.enterPressed();
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
            if (GamePanel.isCurrentState("Starting a new game: Placing a new house")) {
                GamePanel.gameState = "Starting a new game: Creating a new sim";
            }
            if (GamePanel.isCurrentState("Placing a new house")) {
                GamePanel.gameState = "Creating a new sim";

                int newSimIndex = listOfSim.size() - 1;
                listOfSim.remove(newSimIndex);
                UserInterface.setCurrentSim(CreateSimPanel.currentSim);

                Sim currentSim = UserInterface.getCurrentSim();
                if (currentSim.isBusy()) currentSim.changeIsBusyState();
            }

            if (!GamePanel.isCurrentState("Playing")) {
                CreateSimPanel.init();
                PanelHandler.switchPanel(GamePanel.getInstance(), CreateSimPanel.getInstance());
                changeIsAddingState();
            }
            UserInterface.viewWorld();
        }
    }

    public void draw(Graphics2D g) {
        // Draw the world in quarters with the size of each quarter of 32 x 32
        drawWorld(g);
        
        drawHouses(g);

        drawCursor(g);
        
        drawOwnerNames(g);

        drawArrows(g);
    }

    private int getCursorInQuarter() {
        int lowerCoords = 0 * Consts.TILE_SIZE;
        int middleCoords = 32 * Consts.TILE_SIZE;
        int upperCoords = 64 * Consts.TILE_SIZE;

        if ((cursor.getX() >= lowerCoords && cursor.getX() < middleCoords) &&
            (cursor.getY() >= lowerCoords && cursor.getY() < middleCoords)) {
            return 1;
        }
        if ((cursor.getX() >= middleCoords && cursor.getX() < upperCoords) &&
            (cursor.getY() >= lowerCoords && cursor.getY() < middleCoords)) {
            return 2;
        }
        if ((cursor.getX() >= middleCoords && cursor.getX() < upperCoords) &&
            (cursor.getY() >= middleCoords && cursor.getY() < upperCoords)) {
            return 3;
        }
        if ((cursor.getX() >= lowerCoords && cursor.getX() < middleCoords) &&
            (cursor.getY() >= middleCoords && cursor.getY() < upperCoords)) {
            return 4;
        }
        return 0;
    }

    private void setUpperAndLowerBounds() {
        if (getCursorInQuarter() == 1) {
            lowerBoundsX = 0; upperBoundsX = 32;
            lowerBoundsY = 0; upperBoundsY = 32;
            topLeftX = 26; topLeftY = 26;
        }
        else if (getCursorInQuarter() == 2) {
            lowerBoundsX = 32; upperBoundsX = 64;
            lowerBoundsY = 0; upperBoundsY = 32;
            topLeftX = 25; topLeftY = 26;
        }
        else if (getCursorInQuarter() == 3) {
            lowerBoundsX = 32; upperBoundsX = 64;
            lowerBoundsY = 32; upperBoundsY = 64;
            topLeftX = 25; topLeftY = 25;
        }
        else if (getCursorInQuarter() == 4) {
            lowerBoundsX = 0; upperBoundsX = 32;
            lowerBoundsY = 32; upperBoundsY = 64;
            topLeftX = 26; topLeftY = 25;
        }
    }

    private void drawWorld(Graphics2D g) {
        setUpperAndLowerBounds();

        for (int y = lowerBoundsY; y < upperBoundsY; y++) {
            for (int x = lowerBoundsX; x < upperBoundsX; x++) {
                int tileX = topLeftX + (x * Consts.TILE_SIZE) % viewableGrid;
                int tileY = topLeftY + (y * Consts.TILE_SIZE) % viewableGrid;
                g.drawImage(images[0], tileX, tileY, null);
            }
        }
    }

    private void drawHouses(Graphics2D g) {
        for (int y = lowerBoundsY; y < upperBoundsY; y++) {
            for (int x = lowerBoundsX; x < upperBoundsX; x++) {
                int tileX = topLeftX + (x * Consts.TILE_SIZE) % viewableGrid;
                int tileY = topLeftY + (y * Consts.TILE_SIZE) % viewableGrid;

                if (getMap(x, y) == 1) {
                    g.drawImage(images[1], tileX, tileY, null);
                }
                
                if (cursor.isAboveHouse() && x == cursor.getGridX() && y == cursor.getGridY()) {
                    if (isAdding) {
                        g.drawImage(images[5], tileX, tileY, null);
                    }
                    else {
                        Sim currentSim = UserInterface.getCurrentSim();
                        House currentHouse = currentSim.getCurrentHouse();
                        boolean isAboveCurrentHouse = (cursor.getGridX() == currentHouse.getX())
                            && (cursor.getGridY() == currentHouse.getY());

                        if (isAboveCurrentHouse) {
                            g.drawImage(images[5], tileX, tileY, null);
                        }
                        else {
                            g.drawImage(images[4], tileX, tileY, null);
                        }
                    }
                }
            }
        }
    }

    private void drawOwnerNames(Graphics2D g) {
        try {
            Font font;
            font = new Font("Inter", Font.PLAIN, 9);
            g.setColor(Color.WHITE);
            g.setFont(font);
    
            int gridX = cursor.getGridX();
            int gridY = cursor.getGridY();
    
            House houseSelected = getHouse(gridX, gridY);
            Sim houseOwner = houseSelected.getOwner();
            Sim currentSim = UserInterface.getCurrentSim();
            House currentHouse = currentSim.getCurrentHouse();
            boolean isAboveCurrentHouse = (cursor.getGridX() == currentHouse.getX())
                && (cursor.getGridY() == currentHouse.getY());
    
            for (int y = lowerBoundsY; y < upperBoundsY; y++) {
                for (int x = lowerBoundsX; x < upperBoundsX; x++) {
                    int tileX = topLeftX + (x * Consts.TILE_SIZE) % viewableGrid;
                    int tileY = topLeftY + (y * Consts.TILE_SIZE) % viewableGrid;
    
                    if (cursor.isAboveHouse() && x == cursor.getGridX() && y == cursor.getGridY()) {
                        if (isAboveCurrentHouse) return;
                        if (isAdding) return;

                        g.drawString(houseOwner.getName() + "'s", tileX - 5, tileY + 26);
                        g.drawString("House", tileX - 5, tileY + 36);
                    }
                }
            }
        }
        catch (NullPointerException npe) {}
    }

    private void drawCursor(Graphics2D g) {
        if (cursor.isAboveHouse()) {
            return;
        }

        int tileX = topLeftX + (cursor.getX() % (viewableGrid));
        int tileY = topLeftY + (cursor.getY() % (viewableGrid));

        // if (getCursorInQuarter() == 1 || getCursorInQuarter() == 3) {
        //     tileX = topLeftX + (cursor.getX() % (viewableGrid - 14));
        //     tileY = topLeftY + (cursor.getY() % (viewableGrid - 14));
        // }

        if (isAdding) {
            g.drawImage(images[3], tileX, tileY, null);
        }
        else {
            g.drawImage(images[2], tileX, tileY, null);
        }
    }

    private void drawArrows(Graphics2D g) {
        // Arrows
        if (getCursorInQuarter() == 1){
            g.drawImage(images[8], topLeftX + (15 * Consts.TILE_SIZE + 8), topLeftY + (30 * Consts.TILE_SIZE), null);
            g.drawImage(images[9], topLeftX + (30 * Consts.TILE_SIZE), topLeftY + (15 * Consts.TILE_SIZE + 8), null);
        }
        if (getCursorInQuarter() == 2){
            g.drawImage(images[7], topLeftX + Consts.TILE_SIZE, topLeftY + (15 * Consts.TILE_SIZE + 8), null);
            g.drawImage(images[8], topLeftX + (15 * Consts.TILE_SIZE + 8), topLeftY + (30 * Consts.TILE_SIZE), null);
        }
        if (getCursorInQuarter() == 3){
            g.drawImage(images[6], topLeftX + (15 * Consts.TILE_SIZE + 8), topLeftY + Consts.TILE_SIZE, null);
            g.drawImage(images[7], topLeftX + Consts.TILE_SIZE, topLeftY + (15 * Consts.TILE_SIZE + 8), null);
        }
        if (getCursorInQuarter() == 4){
            g.drawImage(images[6], topLeftX + (15 * Consts.TILE_SIZE + 8), topLeftY + Consts.TILE_SIZE, null);
            g.drawImage(images[9], topLeftX + (30 * Consts.TILE_SIZE), topLeftY + (15 * Consts.TILE_SIZE + 8), null);
        }
    }
}
