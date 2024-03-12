package src.main.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import javax.swing.JPanel;

import src.assets.ImageLoader;
import src.entities.sim.Sim;
import src.main.GameLoader;
import src.main.KeyHandler;
import src.main.UserInterface;
import src.world.World;

public class CreateSimPanel extends JPanel {
    public static CreateSimPanel csp = new CreateSimPanel();
    
    public static String[] textFields = { "", "" };
    public static String simName;
    public static String roomName;
    public static int selectedColor = 2;
    private static int selectedField = 0; // 0 to 3
    private static boolean help = false;
    private static boolean nameTaken = false;

    public static Sim currentSim;

    private BufferedImage[] images = ImageLoader.loadCreateSimMenu();

    private CreateSimPanel() {
        setPreferredSize(new Dimension(800, 600));
        setFocusTraversalKeysEnabled(false);

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                simName = textFields[0];
                roomName = textFields[1];

                // Check if the Enter key was pressed on the done button
                if (keyCode == KeyEvent.VK_ENTER && selectedField == 3) {
                    if (textFields[0].equals("")|| textFields[1].equals("")) return;

                    if (GamePanel.isCurrentState("Starting a new game: Creating a new sim")) {
                        GamePanel.gameState = "Starting a new game: Placing a new house";
                        GameLoader.startNewGame();
                    }
                    if (GamePanel.isCurrentState("Creating a new sim")) {
                        World world = UserInterface.getWorld();
                        ArrayList<Sim> listOfSim = world.getListOfSim();
                        
                        for (Sim sim : listOfSim) {
                            if (textFields[0].equals(sim.getName())) {
                                nameTaken = true;
                                break;
                            }
                        }
                        if (nameTaken) return;

                        GamePanel.gameState = "Placing a new house";
                        GameLoader.addSim();
                    }
                    
                    PanelHandler.switchPanel(CreateSimPanel.getInstance(), GamePanel.getInstance());
                }

                if (keyCode == KeyEvent.VK_ESCAPE) {
                    if (GamePanel.isCurrentState("Starting a new game: Creating a new sim")) {
                        GamePanel.gameState = "Main menu";
                        PanelHandler.switchPanel(CreateSimPanel.getInstance(), MainMenuPanel.getInstance());
                    }
                    if (GamePanel.isCurrentState("Creating a new sim")) {
                        GamePanel.gameState = "Playing";
                        PanelHandler.switchPanel(CreateSimPanel.getInstance(), GamePanel.getInstance());
                    }
                }

                if (keyCode == KeyEvent.VK_SLASH) {
                    help = !help;
                }
                
                // names text feild
                if (selectedField < 2) {
                    textFields[selectedField] = KeyHandler.receiveStringInput(e, textFields[selectedField]);
                    simName = textFields[0];
                    roomName = textFields[1];
                }
                // color selector
                if (selectedField  == 2) {
                    if (keyCode == KeyEvent.VK_D) selectedColor++;
                    if (keyCode == KeyEvent.VK_A) selectedColor--;
                    if (selectedColor > 7) selectedColor = 0;
                    if (selectedColor < 0) selectedColor = 7;
                }

                // Check if the Tab key was pressed
                if (keyCode == KeyEvent.VK_TAB) {
                    // Move to the next field or the Done button
                    selectedField++;
                    if (selectedField > 3) {
                        selectedField = 0;
                    }
                    nameTaken = false;
                }
                repaint();
            }
        };
        addKeyListener(keyAdapter);
        setFocusable(true);
    }

    public static CreateSimPanel getInstance() {
        return csp;
    }

    public static void init() {
        csp = new CreateSimPanel();
        reset();
    }

    public static void reset() {
        textFields[0] = "";
        textFields[1] = "";
        selectedField = 0;
        selectedColor = 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // background
        g.setColor(new Color(110, 196, 213));
        g.fillRect(0, 0, 800, 600);

        if (help) {
            g.drawImage(images[11], 0, 0, null);
        }

        drawBoxes(g2);

        drawSelector(g2);

        drawTexts(g2);

        g.dispose();
    }

    private void drawBoxes(Graphics2D g) {
        BufferedImage newSim = ImageLoader.simColorSelector(selectedColor);

        g.drawImage(images[0], 239, 60, null); // background box
        g.drawImage(images[2], 264, 108, null); // sim preview box
        g.drawImage(images[3], 270, 290, null); // name input box
        g.drawImage(images[3], 270, 331, null); // room input box
        g.drawImage(images[4], 318, 391, null); // color picker
        g.drawImage(images[6], 334, 436, null); // done button

        g.drawImage(images[1], 225, 60, null); // title box
        g.drawImage(newSim, 336, 130, 128, 128, null); // sim preview image
    }

    private void drawSelector(Graphics2D g) {
        if (selectedField == 0) g.drawImage(images[7], 267, 287, null); // sim name
        if (selectedField == 1) g.drawImage(images[7], 267, 328, null); // room name
        if (selectedField == 2) g.drawImage(images[8], 315, 388, null); // color selector
        if (selectedField == 3) {
            if (textFields[0].equals("") || textFields[1].equals("") || nameTaken) {
                g.drawImage(images[10], 337, 435, null); // done button
            }
            else {
                g.drawImage(images[9], 337, 435, null); // done button
            }
        }
        g.drawImage(images[5], 324 + (selectedColor * 19), 410, null); // color picker cursor
    }

    private void drawTexts(Graphics2D g) {
        g.setFont(new Font("Inter", Font.BOLD, 12));
        g.setColor(Color.WHITE);
        g.drawString("Create New Sim", 352, 82);

        g.setFont(new Font("Inter", Font.PLAIN, 12));
        g.setColor(new Color(110, 54, 81));

        if (textFields[0].equals("")) {
            g.drawString("Enter sim name...", 282, 311);
        }
        else {
            g.drawString(textFields[0], 282, 311);
        }

        if (textFields[1].equals("")) {
            g.drawString("Enter room name...", 282, 352);
        }
        else{
            g.drawString(textFields[1], 282, 352);
        }

        g.setFont(new Font("Inter", Font.BOLD, 9));
        g.setColor(new Color(69, 34, 46));
        g.drawString("Choose your sim's outfit color", 331, 385);

        g.setFont(new Font("Inter", Font.PLAIN, 9));
        g.drawString("press", 358, 484);
        g.setFont(new Font("Inter", Font.BOLD, 9));
        g.drawString("esc", 386, 484);
        g.setFont(new Font("Inter", Font.PLAIN, 9));
        g.drawString("to cancel", 405, 484);
    }
}
