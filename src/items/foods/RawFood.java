package src.items.foods;

import java.awt.image.BufferedImage;

import src.items.Item;
import src.assets.ImageLoader;

public class RawFood extends Food implements Item{
    // Types of raw food
    private static String[] names = {
        "Carrot",
        "Chicken",
        "Milk",
        "Meat",
        "Peanuts",
        "Potato",
        "Rice",
        "Spinach"
    };
    private static int[] hungerPoints = {
        2,
        8,
        1,
        15,
        2,
        4,
        5,
        2
    };
    private static int[] prices = {
        3,
        10,
        2,
        12,
        2,
        3,
        5,
        3
    };

    // Images of the raw foods
    private BufferedImage[] icons = new BufferedImage[8];

    // Constructor
    public RawFood (int imageIndex) {
        super (
            names[imageIndex],
            hungerPoints[imageIndex],
            prices[imageIndex],
            imageIndex
        );
        
        // load the icons
        this.icons = ImageLoader.loadRawFood();
    }

    // Implementation of abstract methods
    @Override
    public BufferedImage getIcon() {
        return icons[getImageIndex()];
    }
}
