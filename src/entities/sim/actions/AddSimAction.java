package src.entities.sim.actions;

import src.main.panels.CreateSimPanel;
import src.main.panels.GamePanel;
import src.main.panels.PanelHandler;
import src.world.World;

public class AddSimAction {
    public static void viewListOfSims(World world) {
        
    }

    public static void addSim() {
        GamePanel.gameState = "Creating a new sim";
        
        CreateSimPanel.init();
        PanelHandler.switchPanel(GamePanel.getInstance(), CreateSimPanel.getInstance());
    }
}
