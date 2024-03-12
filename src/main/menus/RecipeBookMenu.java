package src.main.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import src.assets.ImageLoader;
import src.entities.sim.Inventory;
import src.entities.sim.Sim;
import src.items.Item;
import src.items.foods.BakedFood;
import src.items.foods.RawFood;
import src.main.KeyHandler;
import src.main.UserInterface;

public class RecipeBookMenu {
    // initialize available baked foods
    public static ArrayList<BakedFood> listOfBakedFoods = init();

    // images
    private static BufferedImage[] images = ImageLoader.loadRecipeBook();
    private static BufferedImage title = images[0];
    private static BufferedImage recipeBookBox = images[1];
    private static BufferedImage recipeBookCatalogueBox = images[2];
    private static BufferedImage warningBox = images[3];
    private static BufferedImage selector = images[4];
    
    // interface dimensions
    private static int slotSize = 45;
    private static int slotRow = 0;
    private static int slotCol = 0;
    private static int slotX = 621;
    private static int slotY = 207;
    public static int slotSelected = 0;

    // attributes
    private static boolean ableToCook = true;

    public static void ableToCook() {
        ableToCook = !ableToCook;
    }

    public static boolean isAbleToCook() {
        return ableToCook;
    }

    public static boolean isAllIngredientAvailable(BakedFood bakedFood) {
        Sim currentSim = UserInterface.getCurrentSim();
        Inventory currentSimInventory = currentSim.getInventory();
        
        // check available ingredients first
        String[] ingredients = bakedFood.getIngredients();
        boolean isAllIngredientAvailable = false;

        int length = ingredients.length;
        int availableIngredients = 0;

        for (int i = 0; i < length; i++) { 
            for (Item rawFood : currentSimInventory.getMapOfItems().keySet()) {

                if (!(rawFood instanceof RawFood)) continue;

                if (ingredients[i].equals(rawFood.getName())) {
                    availableIngredients++;
                }
            }
        }

        if (availableIngredients >= length) {
            isAllIngredientAvailable = true;
        }

        return isAllIngredientAvailable;
    }

    public static void update() {
        if (!isAbleToCook()) return;
        
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

        if (newSlotSelected < listOfBakedFoods.size() && (newSlotCol >= 0 && newSlotCol < 3) && (newSlotRow >= 0 && newSlotRow < 2)) {
            slotSelected = newSlotSelected;
            slotCol = newSlotCol;
            slotRow = newSlotRow;
        }
    }
    public static void draw(Graphics2D g) {
        drawRecipeBook(g);

        if (!ableToCook) {
            drawWarning(g);
        }
    }

    private static void drawWarning(Graphics2D g) {
        g.drawImage(warningBox, 275, 180, null);

        Font font = new Font("Inter", Font.BOLD, 14);
        g.setFont(font);
        g.setColor(Color.WHITE);

        UserInterface.drawCenteredText(g, warningBox, 275, 200, "Warning", font);
        UserInterface.drawCenteredText(g, warningBox, 275, 240, "Ingredients not available", font);
        UserInterface.drawCenteredText(g, warningBox, 275, 255, "press esc to continue", font);

    }
    private static void drawRecipeBook(Graphics2D g) { 
        // draw the background
        g.drawImage(recipeBookBox, 605, 174, null);

        // draw the box
        g.drawImage(recipeBookCatalogueBox, 614, 201, null);

        // draw image of available baked food
        drawItems(g);

        // draw the food name
        g.setColor(Color.WHITE);
        Font font = new Font("Inter", Font.BOLD, 7);
        g.setFont(font);
        BakedFood foodName = listOfBakedFoods.get(slotSelected);
        UserInterface.drawCenteredText(g, recipeBookBox, 605, 193, foodName.getName(), font);

        // draw the selector
        g.drawImage(selector, slotX + (slotSize * slotCol) + (slotCol * 7), slotY + (slotSize * slotRow) + (slotRow * 5), null); // selector

        // draw the frame
        g.drawImage(title, 599, 146, null);
        font = new Font("Inter", Font.BOLD, 14);
        g.setFont(font);

        UserInterface.drawCenteredText(g, title, 599, 167, "Recipe Book", font);
    }

    private static void drawItems(Graphics2D g)
    {
        try {
            int x = slotX + 2, y = slotY + 2; // Starting coordinates
            int cols = 3; int rows = 2;
            int i = 0; int j = 0;
    
            for (BakedFood bakedFood : listOfBakedFoods) {
                if ((i % cols == cols) && (j == rows)) return;

                BufferedImage itemIcon = bakedFood.getIcon(); // Get the item image
    
                g.drawImage(itemIcon, x, y, null); // Draw the image
    
                x += slotSize + 8; // Move to the next column
                if (i % cols == cols - 1) { // If we've filled up a row
                    x = slotX + 2; // Reset to the first column
                    y += slotSize + 6; // Move to the next row
                    j++;
                }
                i++;
            }
        }
        catch (NullPointerException npe) {}
        catch (ConcurrentModificationException cme) {}
    }

    private static ArrayList<BakedFood> init() {
        ArrayList<BakedFood> listOfBakedFoods = new ArrayList<>();

        listOfBakedFoods.add(new BakedFood(0));
        listOfBakedFoods.add(new BakedFood(1));
        listOfBakedFoods.add(new BakedFood(2));
        listOfBakedFoods.add(new BakedFood(3));
        listOfBakedFoods.add(new BakedFood(4));

        return listOfBakedFoods;
    }
}
