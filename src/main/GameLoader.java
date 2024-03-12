package src.main;

import java.awt.Color;

import src.assets.ImageLoader;
import src.main.time.GameTime;
import src.main.menus.ListOfSimsMenu;
import src.main.panels.CreateSimPanel;
import src.main.panels.GamePanel;
import src.entities.sim.Sim;
import src.world.World;

public class GameLoader {
    public static void startNewGame() {
        GameTime.init(1, Consts.ONE_MINUTE * 12);
        World world = GamePanel.world = new World();
        
        String simName = CreateSimPanel.simName;
        int selectedColor = CreateSimPanel.selectedColor;
        
        // Create the new sim
        Color shirtColor = ImageLoader.setColor(selectedColor);
        Sim newSim = new Sim(simName, shirtColor);
        
        // Add the new sim to the world
        world.addSim(newSim);
        
        // Actually start the game by changing the state into adding a house
        UserInterface.init(world);
        ListOfSimsMenu.world = world;
        ListOfSimsMenu.listOfSims = world.getListOfSim();
    }

    public static void addSim() {
        World world = GamePanel.world;

        String simName = CreateSimPanel.simName;
        int selectedColor = CreateSimPanel.selectedColor;
        
        // Create the sim
        Color shirtColor = ImageLoader.setColor(selectedColor);
        Sim newSim = new Sim(simName, shirtColor);
        CreateSimPanel.currentSim = UserInterface.getCurrentSim();
        UserInterface.setCurrentSim(newSim);
        
        // Add the sim to the world and change the state of game into adding a house
        world.addSim(newSim);
        world.changeIsAddingState();
        UserInterface.viewWorld();
        UserInterface.viewListOfSims();
    }

    public static void loadGame() {

        // LOAD GAME HERE
        
    }
}