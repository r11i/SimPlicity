package src.entities.sim;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Font;

import src.main.KeyHandler;
import src.assets.ImageLoader;
import src.main.UserInterface;
import src.entities.interactables.*;
import src.items.foods.Food;
import src.world.House;
import src.world.Room;
import src.items.Item;

public class Inventory {
    // attributes
    private HashMap<Item, Integer> mapOfItems = new HashMap<>();
    private HashMap<Item, Integer> itemsToShow = new HashMap<>();
    private ArrayList<String> itemNames = new ArrayList<>();
    private boolean isOpen = false; // true if the inventory is open, false if closed
    private boolean isObject = true;
    private boolean isChoosingFood = false;

    private int slotSelected = 0;
    
    // interface dimensions
    private int slotSize = 45;
    private int slotRow = 0;
    private int slotCol = 0;
    private int slotX = 621;
    private int slotY = 208;

    // images for the inventory
    private BufferedImage[] images = ImageLoader.loadInventory();
    private BufferedImage inventoryTitle = images[0];
    private BufferedImage inventoryBox = images[1];
    private BufferedImage inventoryCatalogueBox = images[2];
    private BufferedImage categoryBox = images[3];
    private BufferedImage selector = images[4];

    // constructor
    public Inventory() {
        // default objects
        addItem(new Bed(0));
        addItem(new Toilet());
        addItem(new Stove(0));
        addItem(new TableAndChair());
        addItem(new Clock());

        getItemsToShow();
    }

    // getter
    public HashMap<Item, Integer> getMapOfItems()
    {
        return mapOfItems;
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public boolean isChoosingFood()
    {
        return isChoosingFood;
    }

    // setter
    public void changeIsOpen()
    {
        slotSelected = 0;
        slotRow = 0;
        slotCol = 0;

        isOpen = !isOpen;
    }

    public void switchCategory()
    {
        if (isChoosingFood) return;
        
        slotSelected = 0;
        slotRow = 0;
        slotCol = 0;

        isObject = !isObject;
    }

    public void chooseFood() {
        if (isObject) switchCategory();

        isChoosingFood = !isChoosingFood;
    }

    public void addItem(Item item)
    {
        try {
            for (Item i : mapOfItems.keySet())
            {
                if (i.getName().equals(item.getName()))
                {
                    int count = mapOfItems.get(i);
                    mapOfItems.put(i, count + 1);
                    return;
                }
            }
            mapOfItems.put(item, 1);
        }
        catch (ConcurrentModificationException cme) {addItem(item);}
    }

    public void removeItem(Item item)
    {
        try {
            for (Item i : mapOfItems.keySet())
            {
                if (i.getName().equals(item.getName()))
                {
                    int count = mapOfItems.get(item);
                    if (count > 1) {
                        mapOfItems.put(item, count - 1);
                    }
                    else {
                        mapOfItems.remove(item);
                    }
                }
            }
        }
        catch (ConcurrentModificationException cme) {removeItem(item);}
    }

    public void interact() {
        Sim currentSim = UserInterface.getCurrentSim();

        if (!currentSim.isStatusCurrently("Idle")) return;
        if (slotSelected > itemNames.size()) return;

        try {
            String selectedItem = itemNames.get(slotSelected);
    
            for (Item item : mapOfItems.keySet()) {
                if (!item.getName().equals(selectedItem)) continue;
    
                Sim sim = UserInterface.getCurrentSim();
    
                if (item instanceof Interactables) {
                    House currentHouse = sim.getCurrentHouse();
                    Sim currentHouseOwner = currentHouse.getOwner();
    
                    if (!sim.getName().equals(currentHouseOwner.getName())){
                        return;
                    }

                    if (!sim.isStatusCurrently("Idle")) return;
    
                    Room currentRoom = sim.getCurrentRoom();
                    Interactables newObject = (Interactables) item;
    
                    changeIsOpen();
                    currentRoom.addObject(newObject);
                    sim.changeIsBusyState();
                }
    
                if (item instanceof Food) {
                    if (isChoosingFood) {
                        Food food = (Food) item;
                        
                        food.eat(sim);
                    }
                    else {
                        return;
                    }
                }
                
                removeItem(item);
                return;
            }
        }
        catch (IndexOutOfBoundsException iobe) {}
        catch (ConcurrentModificationException cme) {}
    }

    // others
    private void getItemsToShow() {
        itemsToShow = new HashMap<>();
        itemNames = new ArrayList<>();

        try {
            for (Item item : mapOfItems.keySet()) {
                if (isObject){
                    if (item instanceof Interactables) {
                        itemsToShow.put(item, mapOfItems.get(item));
                    }
                }
                else {
                    if ((item instanceof Food)) {
                        itemsToShow.put(item, mapOfItems.get(item));
                    }
                }
            }
    
            for (Item item : itemsToShow.keySet()) {
                itemNames.add(item.getName());
            }
        }
        catch (ConcurrentModificationException cme) {}
    }

    public void update()
    {
        getItemsToShow();

        if (KeyHandler.isKeyPressed(KeyEvent.VK_TAB)) {
            switchCategory();
        }

        int newSlotSelected = slotSelected;
        int newSlotCol = slotCol;
        int newSlotRow = slotRow;
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
            newSlotSelected--;
            newSlotCol--;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
            newSlotSelected += 3;
            newSlotRow++;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
            newSlotSelected++;
            newSlotCol++;
        }
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
            newSlotSelected -= 3;
            newSlotRow--;
        }

        if (newSlotSelected < itemNames.size() && (newSlotCol >= 0 && newSlotCol < 3) && (newSlotRow >= 0 && newSlotRow < 4)) {
            slotSelected = newSlotSelected;
            slotCol = newSlotCol;
            slotRow = newSlotRow;
        }
        
        if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
            if (slotSelected < itemNames.size()) {
                if (isChoosingFood) return;
                interact();
            }
        }
    }

    public void draw(Graphics2D g)
    {
        if (isOpen) {
            drawFrame(g);
            
            drawTexts(g);

            drawSelector(g);
            
            drawItems(g);
        }
        drawTitle(g);
    }

    private void drawFrame(Graphics2D g) {
        g.drawImage(inventoryBox, 605, 174, null); // inventory box
        g.drawImage(inventoryCatalogueBox, 614, 201, null); // inventory catalogue box
        g.drawImage(categoryBox, 615, 404, null); // interactables box
        g.drawImage(categoryBox, 700, 404, null); // foods box
    }

    private void drawTexts(Graphics2D g) {
        int categoryHighlightedWidth = categoryBox.getWidth() + 4;
        int categoryHighlightedHeight = categoryBox.getHeight() + 3;
        Font font;
        
        g.setColor(Color.WHITE);
        if (isObject) {
            g.drawImage(categoryBox, 614, 403, categoryHighlightedWidth, categoryHighlightedHeight, null); // if interactables
            
            font = new Font("Inter", Font.BOLD, 9);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 616, 419, "Interactables", font);

            font = new Font("Inter", Font.BOLD, 8);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 700, 418, "Foods", font);
        }
        else {
            g.drawImage(categoryBox, 699, 403, categoryHighlightedWidth, categoryHighlightedHeight, null); // if food

            font = new Font("Inter", Font.BOLD, 8);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 616, 418, "Interactables", font);

            font = new Font("Inter", Font.BOLD, 9);
            g.setFont(font);
            UserInterface.drawCenteredText(g, categoryBox, 702, 419, "Foods", font);
        }

        g.setColor(Color.WHITE);
        font = new Font("Inter", Font.BOLD, 9);
        g.setFont(font);

        String itemName = "";
        try {
            itemName = itemNames.get(slotSelected);
            UserInterface.drawCenteredText(g, inventoryBox, 605, 193, itemName, font);
        }
        catch (IndexOutOfBoundsException iobe) {}
    }

    private void drawSelector(Graphics2D g) {
        g.drawImage(selector, slotX + (slotSize * slotCol) + (slotCol * 7), slotY + (slotSize * slotRow), null); // selector
    }

    private void drawItems(Graphics2D g)
    {
        try {
            int x = slotX + 2, y = slotY + 2; // Starting coordinates
            int cols = 3; int rows = 4;
            int i = 0; int j = 0;
    
            for (Item item : itemsToShow.keySet()) {
                if ((i % cols == cols) && (j == rows)) return;

                BufferedImage itemIcon = item.getIcon(); // Get the item image
    
                g.drawImage(itemIcon, x, y, null); // Draw the image
    
                g.setColor(Color.WHITE);
                Font font = new Font("Inter", Font.BOLD, 11);

                if (mapOfItems.get(item) > 1 && mapOfItems.get(item) < 10) {
                    g.setFont(font);
                    g.drawString(Integer.toString(mapOfItems.get(item)), x + 35, y + 41);
                }

                if (mapOfItems.get(item) >= 10) {
                    g.setFont(font);
                    g.drawString(Integer.toString(mapOfItems.get(item)), x + 30, y + 41);
                }
    
                x += slotSize + 8; // Move to the next column
                if (i % cols == cols - 1) { // If we've filled up a row
                    x = slotX + 2; // Reset to the first column
                    y += slotSize + 1; // Move to the next row
                    j++;
                }
                i++;
            }
        }
        catch (NullPointerException npe) {}
        catch (ConcurrentModificationException cme) {}
    }

    private void drawTitle(Graphics2D g) {
        Font font = new Font("Inter", Font.BOLD, 14);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawImage(inventoryTitle, 599, 146, null); // inventory title
        UserInterface.drawCenteredText(g, inventoryTitle, 599, 167, "Inventory", font);
    }
}