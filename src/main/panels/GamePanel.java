package src.main.panels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.entities.sim.Sim;
import src.main.Consts;
import src.main.KeyHandler;
import src.main.UserInterface;
import src.main.menus.UpgradeHouseMenu;
import src.main.menus.WorldMenu;
import src.main.time.GameTime;
import src.world.Room;
import src.world.World;

public class GamePanel extends JPanel implements Runnable {
    private static GamePanel gp = new GamePanel();

    public static String gameState;
    public static GameTime time;

    public static World world;

    private GamePanel() {
        setPreferredSize(new Dimension(Consts.WIDTH, Consts.HEIGHT));
        setBackground(new Color(110, 196, 213));
        setDoubleBuffered(true);
        // Create a KeyAdapter and add it as a key listener to the panel
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                KeyHandler.keyPressed(e.getKeyCode());
                
                KeyHandler.keyBinds();

                if (UserInterface.isUpgradingHouse()) {
                    UpgradeHouseMenu.update(e);
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                KeyHandler.keyReleased(e.getKeyCode());
            }
        };
        addKeyListener(keyAdapter);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        new Thread(this).start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        int fps = 0;
        double time = 0;
        final double TARGET_FPS = 60.0;
        final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        double accumulator = 0.0;
        while (true) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1000000000.0;
            lastTime = now;
            accumulator += deltaTime;
            while (accumulator >= OPTIMAL_TIME / 1000000000.0) {
                update();
                accumulator -= OPTIMAL_TIME / 1000000000.0;
            }
            revalidate();
            repaint();
            fps++;
            time += deltaTime;
            if (time > 1.0) {
                System.out.println("Game Window - FPS: " + fps);
                fps = 0;
                time = 0;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // GETTERS
    public static GamePanel getInstance() {
        return gp;
    }
    
    public static boolean isCurrentState(String state) {
        return gameState.equals(state); 
    }

    private void update() {
        try {
            if (isCurrentState("Main Menu")) return;

            if (isCurrentState("Main Menu: About") || isCurrentState("Playing: About")) return;
    
            if (isCurrentState("Starting a new game: Creating a new sim") || isCurrentState("Creating a new sim")) return;

            if (UserInterface.isViewingWorld()) {
                world.update();
            }
            else {
                Sim currentSim = UserInterface.getCurrentSim();
                Room currentRoom = currentSim.getCurrentRoom();
                
                currentSim.update();
                currentRoom.update();
                UserInterface.update();
            }
        }
        catch (NullPointerException e) {}
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        try {
            if (isCurrentState("Main menu")) return;

            if (isCurrentState("Main menu: About") || isCurrentState("Playing: About")) return;
    
            if (isCurrentState("Starting a new game: Creating a new sim") || isCurrentState("Creating a new sim")) return;
    
            if (UserInterface.isViewingWorld()) {
                WorldMenu.draw(g2);
            }
            else {
                Sim currentSim = UserInterface.getCurrentSim();
                Room currentRoom = currentSim.getCurrentRoom();

                drawPlayAreaBorder(g2);
                currentRoom.draw(g2);
                UserInterface.draw(g2);
            }
        }
        catch (NullPointerException e) {}
        
        // To free resources
        g2.dispose();
    }

    private static void drawPlayAreaBorder(Graphics2D g) {
        g.setColor(new Color(61, 30, 45));
        g.fillRect(203, 47, 394, 394);
    }
}