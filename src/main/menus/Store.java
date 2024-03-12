package src.main.menus;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import src.assets.ImageLoader;
import src.main.KeyHandler;
import src.items.Item;
import src.entities.sim.Sim;
import src.items.foods.Food;
import src.main.UserInterface;
import src.main.time.GameTime;
import src.items.foods.RawFood;
import src.entities.interactables.*;

public class Store {
    private static BufferedImage[] images = ImageLoader.loadStore();
    private static BufferedImage titleBox = images[8];
    private static BufferedImage storeBox = images[7];
    private static BufferedImage catalogueBox = images[0];
    private static BufferedImage categoryBox = images[1];
    private static BufferedImage counterBox = images[2];
    private static BufferedImage simNameBox = images[5];
    private static BufferedImage simMoneyBox = images[6];
    private static BufferedImage decreaseButton = images[3];
    private static BufferedImage increaseButton = images[4];
    private static BufferedImage decreaseHighlight = images[9];
    private static BufferedImage increaseHighlight = images[10];
    private static BufferedImage selector = images[11];

    // Attributes
    private static ArrayList<Item> listOfItem = init();
    private static ArrayList<Item> itemsToShow;
    private static boolean isOpen = false;
    private static boolean isObject = true;
    private static boolean isChoosing = true;
    private static boolean isSelecting = false;
    private static boolean isBuySuccess = true;
    private static int slotSize = 64;
    private static int slotCol = 0;
    private static int slotRow = 0;
    private static int slotSelected = 0;
    private static int itemQuantity = 1;

    // CONSTRUCTOR
    public Store() {}
    
    public static boolean getIsOpen() {
        return isOpen;
    }

    private static void changeIsObject() {
        isObject = !isObject;
    }
    
    // update the state of the store
    public static void update() {
        getItemsToShow();

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
            // Restore the initial state for the next purchase

            if (!isBuySuccess) {
                isChoosing = true;
                isSelecting = false;
                isBuySuccess = true;
                return;
            } 

            if (isChoosing) {
                UserInterface.viewStore();
                isChoosing = true;
                isSelecting = false;
                isBuySuccess = true;
                isObject = true;
                slotCol = 0;
                slotRow = 0;
                slotSelected = 0;
                itemQuantity = 1;
                return;
            }

            if (isSelecting) {
                isChoosing = true;
                isSelecting = false;
                itemQuantity = 1;
                return;
            }
            
             
        } 
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
            if (!isBuySuccess) {
                isChoosing = true;
                isSelecting = false;
                isBuySuccess = true;
                return;
            }  
            if (isChoosing) {
                isChoosing = false;
                isSelecting = true;
                itemQuantity = 1;
                return;
            }
            if (isSelecting) {
                isChoosing = false;
                isSelecting = false;
                interact();
            }  
        }

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_EQUALS)) {
            if (isSelecting) {
                itemQuantity++;
            } 
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_MINUS)) {
            if (isSelecting) {
                if (itemQuantity > 1) {
                    itemQuantity--;
                }
            }
        }

        if (isSelecting) return;

        int newSlotSelected = slotSelected;
        int newSlotCol = slotCol;
        int newSlotRow = slotRow;
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
            newSlotSelected--;
            newSlotCol--;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
            newSlotSelected += 5;
            newSlotRow++;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
            newSlotSelected++;
            newSlotCol++;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
            newSlotSelected -= 5;
            newSlotRow--;
        }

        if (newSlotSelected < itemsToShow.size() && (newSlotCol >= 0 && newSlotCol < 5) && (newSlotRow >= 0 && newSlotRow < 4)) {
            slotSelected = newSlotSelected;
            slotCol = newSlotCol;
            slotRow = newSlotRow;
        }

        if (KeyHandler.isKeyPressed(KeyHandler.KEY_TAB)) {
            if (isChoosing) {
                changeIsObject();
                slotRow = 0;
                slotCol = 0;
                slotSelected = 0;
            }
        }
    }

    public static void interact() {
        Thread buying = new Thread(new Runnable() {
            @Override
            public void run() {
                isChoosing = true;
                isSelecting = false;
                isBuySuccess = true;
                isObject = true;
                
                int selectedItemQuantity = itemQuantity;
                int timeUpperBound = 150;
                int timeLowerBound = 30;
                int deliveryTime = (int) (Math.random()*(timeUpperBound-timeLowerBound) + timeLowerBound);
                
                Sim currentSim = UserInterface.getCurrentSim();
                Item selectedItem = itemsToShow.get(slotSelected);
                
                if (currentSim.getMoney() < selectedItem.getPrice() * itemQuantity) isBuySuccess = false;
                if (!isBuySuccess) return;

                currentSim.setMoney(currentSim.getMoney() - selectedItem.getPrice() * itemQuantity);
                
                UserInterface.viewStore();

                String activityStatus = selectedItem.getName() + "(s) Pack";

                GameTime.addActivityTimer(currentSim, activityStatus, deliveryTime, deliveryTime);

                while (GameTime.isAlive(currentSim, activityStatus)) continue;

                for (int i = 0; i < selectedItemQuantity; i++) {
                    currentSim.getInventory().addItem(selectedItem);
                }
                slotSelected = 0;
            }
        });
        buying.start(); 
    }

    // draw the ui
    public static void draw(Graphics2D g) {
        drawFrame(g);
        drawItems(g);
        drawSimInfo(g);
        drawQuantity(g);
        drawCursor(g);
    }

    private static void drawFrame(Graphics2D g) {
        g.setColor(Color.decode("#6EC4D5"));
        g.fillRect(0, 0, 800, 600);

        g.drawImage(storeBox, 150, 52, null);
        g.drawImage(titleBox, 294, 27, null);
        g.drawImage(catalogueBox, 187, 136, null);
        g.drawImage(simNameBox, 261, 509, null);
        g.drawImage(simMoneyBox, 447, 509, null);

        Font font = new Font("Inter", Font.PLAIN, 10);
        g.setFont(font);
        g.setColor(Color.BLACK);
        UserInterface.drawCenteredText(g, storeBox, 150, 555, "press esc to return", font);
        
        if (!isBuySuccess) {
            font = new Font("Inter", Font.PLAIN, 10);
            g.setFont(font);
            g.setColor(Color.BLACK);

            UserInterface.drawCenteredText(g, storeBox, 150, 453, "you don't have enough money", font);
        }
        else {
            if (isChoosing) {
                font = new Font("Inter", Font.BOLD, 14);
                g.setColor(Color.WHITE);
                g.setFont(font);
    
                Item selectedItem = itemsToShow.get(slotSelected);
                UserInterface.drawCenteredText(g, storeBox, 150, 443, selectedItem.getName(), font);
                UserInterface.drawCenteredText(g, storeBox, 150, 463, "$ " + selectedItem.getPrice(), font);
            }
            if (isSelecting) {
                font = new Font("Inter", Font.BOLD, 14);
                g.setColor(Color.WHITE);
                g.setFont(font);
    
                Item selectedItem = itemsToShow.get(slotSelected);
                UserInterface.drawCenteredText(g, storeBox, 143, 435, "$ " + selectedItem.getPrice() * itemQuantity, font);
    
                g.drawImage(decreaseButton, 331, 441, null);
                g.drawImage(increaseButton, 445, 441, null);
                g.drawImage(counterBox, 369, 442, null);
    
                if (KeyHandler.isKeyDown(KeyHandler.KEY_MINUS)) {
                    g.drawImage(decreaseHighlight, 326, 440, null);
                }
                if (KeyHandler.isKeyDown(KeyHandler.KEY_EQUALS)) {
                    g.drawImage(increaseHighlight, 440, 440, null);
                }
            }
        }

        int categoryHighlightedWidth = categoryBox.getWidth() + 4;
        int categoryHighlightedHeight = categoryBox.getHeight() + 3;
        if (isObject) {
            g.drawImage(categoryBox, 188, 90, categoryHighlightedWidth, categoryHighlightedHeight, null);
            g.drawImage(categoryBox, 407, 92, null);
            
            g.setColor(Color.WHITE);
            
            font = new Font("Inter", Font.BOLD, 16);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 188, 114, "Interactables", font);

            font = new Font("Inter", Font.BOLD, 14);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 407, 114, "Foods", font);
        }
        else {
            g.drawImage(categoryBox, 188, 92, null);
            g.drawImage(categoryBox, 403, 90, categoryHighlightedWidth, categoryHighlightedHeight, null);
            
            g.setColor(Color.WHITE);
            font = new Font("Inter", Font.BOLD, 14);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 188, 114, "Interactables", font);

            font = new Font("Inter", Font.BOLD, 17);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 407, 114, "Foods", font);
        }

        font = new Font("Inter", Font.BOLD, 20);
        g.setFont(font);
        UserInterface.drawCenteredText(g, storeBox, 150, 57, "Store", font);
    }

    private static void drawItems(Graphics2D g)
    {
        try {
            int x = 203, y = 151; // Starting coordinates
            int cols = 5; int rows = 3;
            int i = 0; int j = 0;
    
            for (Item item : itemsToShow) {
                if ((i % cols == cols) && (j == rows)) return;

                BufferedImage itemIcon = item.getIcon(); // Get the item image
    
                g.drawImage(itemIcon, x, y, 72, 72, null); // Draw the image
    
                x += slotSize + 18; // Move to the next column
                if (i % cols == cols - 1) { // If we've filled up a row
                    x = 203; // Reset to the first column
                    y += slotSize + 24; // Move to the next row
                    j++;
                }
                i++;
            }
        }
        catch (NullPointerException npe) {}
        catch (ConcurrentModificationException cme) {}
    }

    private static void drawSimInfo(Graphics2D g) {
        Sim currentSim = UserInterface.getCurrentSim();
        Font font = new Font("Inter", Font.BOLD, 14);
        g.setFont(font);
        UserInterface.drawCenteredText(g, simNameBox, 261, 530, currentSim.getName(), font);

        g.setColor(Color.decode("#3D1E2D"));
        UserInterface.drawCenteredText(g, simMoneyBox, 447, 530, "$ " + Integer.toString(currentSim.getMoney()), font);
    }
    
    private static void drawQuantity(Graphics2D g) {
        if (isSelecting) {
            Font font = new Font("Inter", Font.BOLD, 14);
            g.setFont(font);

            UserInterface.drawCenteredText(g, counterBox, 369, 462, Integer.toString(itemQuantity), font);
        }
    }

    private static void drawCursor(Graphics2D g) {
        g.drawImage(selector, 203 + (slotSize * slotCol) + (18 * slotCol), 153 + (slotSize * slotRow) + (24 * slotRow), 72, 72, null);
    }

    private static ArrayList<Item> init() {
        ArrayList<Item> listOfItem = new ArrayList<>();
        
        listOfItem.add(new Aquarium());
        listOfItem.add(new Bed(0));
        listOfItem.add(new Bed(1));
        listOfItem.add(new Bed(2));
        listOfItem.add(new Clock());
        listOfItem.add(new Shower());
        listOfItem.add(new Stove(0));
        listOfItem.add(new Stove(1));
        listOfItem.add(new TableAndChair());
        listOfItem.add(new Television());
        listOfItem.add(new Toilet());
        listOfItem.add(new TrashBin());
        listOfItem.add(new RawFood(0));
        listOfItem.add(new RawFood(1));
        listOfItem.add(new RawFood(2));
        listOfItem.add(new RawFood(3));
        listOfItem.add(new RawFood(4));
        listOfItem.add(new RawFood(5));
        listOfItem.add(new RawFood(6));
        listOfItem.add(new RawFood(7));

        return listOfItem;
    }

    private static void getItemsToShow() {
        itemsToShow = new ArrayList<>();

        try {
            for (Item item : listOfItem) {
                if (isObject){
                    if (item instanceof Interactables) {
                        itemsToShow.add(item);
                    }
                }
                else {
                    if (item instanceof Food) {
                        itemsToShow.add(item);
                    }
                }
            }
        }
        catch (ConcurrentModificationException cme) {}
    }
}   