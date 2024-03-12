package src.items.foods;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.items.Item;

public class BakedFood extends Food implements Item{
    // Types of baked food
    private static String[] names = {
        "Almond Milk",
        "Chicken and Rice",
        "Curry and Rice",
        "Cut Vegetables",
        "Steak"
    };
    private static int[] hungerPoints = {
        5,
        16,
        30,
        5,
        22
    };
    private static String[][] ingredients = {
        {"Milk", "Potato"},
        {"Rice", "Chicken"},
        {"Rice", "Potato", "Carrot", "Meat"},
        {"Carrot", "Spinach"},
        {"Potato", "Meat"}
    };

    // Attributes
    private String[] ingredient;

    // Images of the raw foods
    private BufferedImage[] icons = new BufferedImage[5];

    // Constructor
    public BakedFood (int imageIndex) {
        super (
            names[imageIndex],
            hungerPoints[imageIndex],
            0,
            imageIndex
        );

        this.ingredient = ingredients[imageIndex];
        // load the icons
        this.icons = ImageLoader.loadBakedFood();
    }

    // Getters
    public String[] getIngredients() {
        return ingredient;
    }

    public BufferedImage getIcon() {
        return icons[getImageIndex()];
    }
}
