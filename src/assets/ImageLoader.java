package src.assets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

import src.entities.sim.Sim;
import src.main.Consts;

public class ImageLoader {
    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(Consts.SCALED_TILE * width, Consts.SCALED_TILE * height, image.getType());
        Graphics2D g = scaledImage.createGraphics();

        g.drawImage(image, 0, 0, Consts.SCALED_TILE * width, Consts.SCALED_TILE * height, null);
        g.dispose();
        return scaledImage;
    }

    public static BufferedImage readImage(String folder, String fileName, int width, int height, boolean scaled) {
        BufferedImage image;

        try {
            image = ImageIO.read(new File("./src/assets/" + folder + "/" + fileName + ".png"));
            if (scaled) {
                image = scaleImage(image, width, height);
            }
            return image;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage rotate90Clockwise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rotated = new BufferedImage(height, width, image.getType());
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotated.setRGB(height - 1 - y, x, image.getRGB(x, y));
            }
        }
        return rotated;
    }

    public static BufferedImage flipHorizontally(BufferedImage image) {
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-image.getWidth(null), 0);
        AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return operation.filter(image, null);
    }

    public static BufferedImage[] loadMainMenu() {
        BufferedImage[] images = new BufferedImage[10];

        images[0] = readImage("panels/main_menu_panel", "background", 1, 1, false);
        images[1] = readImage("panels/main_menu_panel", "game_title", 1, 1, false);
        images[2] = readImage("panels/main_menu_panel", "start_button", 1, 1, false);
        images[3] = readImage("panels/main_menu_panel", "load_button", 1, 1, false);
        images[4] = readImage("panels/main_menu_panel", "about_button", 1, 1, false);
        images[5] = readImage("panels/main_menu_panel", "exit_button", 1, 1, false);
        images[6] = readImage("panels/main_menu_panel", "start_highlight", 1, 1, false);
        images[7] = readImage("panels/main_menu_panel", "load_highlight_red", 1, 1, false);
        images[8] = readImage("panels/main_menu_panel", "about_highlight", 1, 1, false);
        images[9] = readImage("panels/main_menu_panel", "exit_highlight", 1, 1, false);
    
        return images;
    }

    public static BufferedImage[] loadAboutMenu() {
        BufferedImage[] images = new BufferedImage[2];

        images[0] = readImage("panels/about_panel", "page_1", 1, 1, false);
        images[1] = readImage("panels/about_panel", "page_2", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadCreateSimMenu() {
        BufferedImage[] images = new BufferedImage[12];

        images[0] = readImage("panels/create_sim_panel", "create_sim_box", 1, 1, false);
        images[1] = readImage("panels/create_sim_panel", "title_box", 1, 1, false);
        images[2] = readImage("panels/create_sim_panel", "sim_preview_box", 1, 1, false);
        images[3] = readImage("panels/create_sim_panel", "input_box", 1, 1, false);
        images[4] = readImage("panels/create_sim_panel", "color_slider", 1, 1, false);
        images[5] = readImage("panels/create_sim_panel", "cursor", 1, 1, false);
        images[6] = readImage("panels/create_sim_panel", "button_done", 1, 1, false);
        images[7] = readImage("panels/create_sim_panel", "input_box_highlight", 1, 1, false);
        images[8] = readImage("panels/create_sim_panel", "color_slider_highlight", 1, 1, false);
        images[9] = readImage("panels/create_sim_panel", "button_done_highlight", 1, 1, false);
        images[10] = readImage("panels/create_sim_panel", "button_done_red_highlight", 1, 1, false);
        images[11] = readImage("panels/create_sim_panel", "create_new_sim_help", 1, 1, false);

        return images;
    }
    
    public static BufferedImage[] loadSim(Sim sim) {
        BufferedImage[] images = new BufferedImage[12];

        images[0] = readImage("entities/sim", "sim_up", 1, 1, true);
        images[1] = readImage("entities/sim", "sim_right", 1, 1, true);
        images[2] = readImage("entities/sim", "sim_down", 1, 1, true);
        images[3] = readImage("entities/sim", "sim_left", 1, 1, true);
        images[4] = readImage("entities/sim", "sim_walk_up_1", 1, 1, true);
        images[5] = readImage("entities/sim", "sim_walk_up_2", 1, 1, true);
        images[6] = readImage("entities/sim", "sim_walk_right_1", 1, 1, true);
        images[7] = readImage("entities/sim", "sim_walk_right_2", 1, 1, true);
        images[8] = readImage("entities/sim", "sim_walk_down_1", 1, 1, true);
        images[9] = readImage("entities/sim", "sim_walk_down_2", 1, 1, true);
        images[10] = readImage("entities/sim", "sim_walk_left_1", 1, 1, true);
        images[11] = readImage("entities/sim", "sim_walk_left_2", 1, 1, true);

        for (int i = 0; i < images.length; i++) {
            images[i] = changeSimColor(images[i], sim);
        }

        return images;
    }

    public static BufferedImage[] loadWorldMenu() {
        BufferedImage[] images = new BufferedImage[4];

        images[0] = readImage("menus/world_menu", "sim_preview_box", 1, 1, false);
        images[1] = readImage("menus/world_menu", "house_info_box", 1, 1, false);
        images[2] = readImage("menus/world_menu", "help_box", 1, 1, false);
        images[3] = readImage("menus/world_menu", "world_help", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadGameMenu() {
        BufferedImage[] images = new BufferedImage[9];

        images[0] = readImage("menus/game_menu/game_info", "double_info_box", 1, 1, false);
        images[1] = readImage("menus/game_menu/sim_info", "money_box", 1, 1, false);
        images[2] = readImage("menus/game_menu/sim_info", "sim_info_box", 1, 1, false);
        images[3] = readImage("menus/game_menu/game_info", "day_box", 1, 1, false);
        images[4] = readImage("menus/game_menu/sim_info", "icon_health", 1, 1, false);
        images[5] = readImage("menus/game_menu/sim_info", "icon_hunger", 1, 1, false);
        images[6] = readImage("menus/game_menu/sim_info", "icon_mood", 1, 1, false);
        images[7] = readImage("menus/game_menu/game_info", "help_box", 1, 1, false);
        images[8] = readImage("menus/game_menu/game_info", "game_info_help", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadWorkingTab() {
        BufferedImage[] images = new BufferedImage[2];

        images[0] = readImage("menus/game_menu/working_tab", "working_box", 1, 1, false);
        images[1] = readImage("menus/game_menu/working_tab", "icon_work", 1, 1, false);
        
        return images;
    }

    public static BufferedImage[] loadExercisingTab() {
        BufferedImage[] images = new BufferedImage[2];

        images[0] = readImage("menus/game_menu/exercising_tab", "exercising_box", 1, 1, false);
        images[1] = readImage("menus/game_menu/exercising_tab", "icon_exercise", 1, 1, false);
        
        return images;
    }

    public static BufferedImage[] loadTimeRemainingTab() {
        BufferedImage[] images = new BufferedImage[4];

        images[0] = readImage("menus/game_menu/time_remaining_tab", "time_remaining_box", 1, 1, false);
        images[1] = readImage("menus/game_menu/time_remaining_tab", "icon_time", 1, 1, false);
        images[2] = readImage("menus/game_menu/time_remaining_tab", "icon_build_room", 1, 1, false);
        images[3] = readImage("menus/game_menu/time_remaining_tab", "icon_buy_item", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadTabMenu() {
        BufferedImage[] images = new BufferedImage[5];

        images[0] = readImage("menus/game_menu/tab_menu", "icon_edit_room", 1, 1, false);
        images[1] = readImage("menus/game_menu/tab_menu", "icon_upgrade_house", 1, 1, false);
        images[2] = readImage("menus/game_menu/tab_menu", "icon_store", 1, 1, false);
        images[3] = readImage("menus/game_menu/tab_menu", "icon_view_sims", 1, 1, false);
        images[4] = readImage("menus/game_menu/tab_menu", "icon_visit_sim", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadUpgradeHouse() {
        BufferedImage[] images = new BufferedImage[4];

        images[0] = readImage("menus/game_menu/upgrade_house_menu", "room_name_box", 1, 1, false);
        images[1] = readImage("menus/game_menu/upgrade_house_menu", "price_box", 1, 1, false);
        images[2] = readImage("menus/game_menu/upgrade_house_menu", "add_box", 1, 1, false);
        images[3] = readImage("menus/game_menu/upgrade_house_menu", "price_box_not_sufficient", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadInventory() {
        BufferedImage[] images = new BufferedImage[5];

        images[0] = readImage("menus/game_menu/inventory", "inventory_title", 1, 1, false);
        images[1] = readImage("menus/game_menu/inventory", "inventory_box", 1, 1, false);
        images[2] = readImage("menus/game_menu/inventory", "inventory_catalogue_box", 1, 1, false);
        images[3] = readImage("menus/game_menu/inventory", "category_box", 1, 1, false);
        images[4] = readImage("menus/game_menu/inventory", "selector", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadPause(){
        BufferedImage[] images = new BufferedImage[5];

        images[0] = readImage("menus/pause_menu", "background", 1, 1, false);
        images[1] = readImage("menus/pause_menu", "about", 1, 1, false);
        images[2] = readImage("menus/pause_menu", "exit", 1, 1, false);
        images[3] = readImage("menus/pause_menu", "about_highlight", 1, 1, false);
        images[4] = readImage("menus/pause_menu", "exit_highlight", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadActiveActionsMenu() {
        BufferedImage[] images = new BufferedImage[12];

        images[0] = readImage("menus/active_actions_menu", "icon_change_profession", 1, 1, false);
        images[1] = readImage("menus/active_actions_menu", "icon_go_to_work", 1, 1, false);
        images[2] = readImage("menus/active_actions_menu", "icon_go_exercise", 1, 1, false);
        images[3] = readImage("menus/active_actions_menu", "icon_visit_another_sim", 1, 1, false);
        images[4] = readImage("menus/active_actions_menu", "button", 1, 1, false);
        images[5] = readImage("menus/active_actions_menu", "counter_box", 1, 1, false);
        images[6] = readImage("menus/active_actions_menu", "decrease", 1, 1, false);
        images[7] = readImage("menus/active_actions_menu", "increase", 1, 1, false);
        images[8] = readImage("menus/active_actions_menu", "decrease_highlight", 1, 1, false);
        images[9] = readImage("menus/active_actions_menu", "increase_highlight", 1, 1, false);
        images[10] = readImage("menus/active_actions_menu", "background", 1, 1, false);
        images[11] = readImage("menus/active_actions_menu", "active_actions_help", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadChangeProfessionMenu() {
        BufferedImage[] images = new BufferedImage[16];

        images[0] = readImage("menus/change_profession_menu", "title_box", 1, 1, false);
        images[1] = readImage("menus/change_profession_menu", "main_box", 1, 1, false);
        images[2] = readImage("menus/change_profession_menu", "barista", 1, 1, false);
        images[3] = readImage("menus/change_profession_menu", "chef", 1, 1, false);
        images[4] = readImage("menus/change_profession_menu", "clown", 1, 1, false);
        images[5] = readImage("menus/change_profession_menu", "dentist", 1, 1, false);
        images[6] = readImage("menus/change_profession_menu", "doctor", 1, 1, false);
        images[7] = readImage("menus/change_profession_menu", "model", 1, 1, false);
        images[8] = readImage("menus/change_profession_menu", "police", 1, 1, false);
        images[9] = readImage("menus/change_profession_menu", "programmer", 1, 1, false);
        images[10] = readImage("menus/change_profession_menu", "security", 1, 1, false);
        images[11] = readImage("menus/change_profession_menu", "highlight", 1, 1, false);
        images[12] = readImage("menus/change_profession_menu", "not_able_to_change", 1, 1, false);
        images[13] = readImage("menus/change_profession_menu", "sim_name_box", 1, 1, false);
        images[14] = readImage("menus/change_profession_menu", "sim_profession_box", 1, 1, false);
        images[15] = readImage("menus/change_profession_menu", "change_profession_help", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadListOfSimsMenu() {
        BufferedImage[] images = new BufferedImage[11];

        images[0] = readImage("menus/list_of_sims_menu", "choose_sim_box", 1, 1, false);
        images[1] = readImage("menus/list_of_sims_menu", "sim_box", 1, 1, false);
        images[2] = readImage("menus/list_of_sims_menu", "current_sim_box", 1, 1, false);
        images[3] = readImage("menus/list_of_sims_menu", "create_new_sim", 1, 1, false);
        images[4] = readImage("menus/list_of_sims_menu", "sim_box_highlight", 1, 1, false);
        images[5] = readImage("menus/list_of_sims_menu", "create_new_sim_highlight", 1, 1, false);
        images[6] = readImage("menus/list_of_sims_menu", "create_new_sim_highlight_red", 1, 1, false);
        images[7] = readImage("menus/list_of_sims_menu", "icon_health", 1, 1, false);
        images[8] = readImage("menus/list_of_sims_menu", "icon_hunger", 1, 1, false);
        images[9] = readImage("menus/list_of_sims_menu", "icon_mood", 1, 1, false);
        images[10] = readImage("menus/list_of_sims_menu", "view_sims_help", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadInteractMenu() {
        BufferedImage[] images = new BufferedImage[7];

        images[0] = readImage("menus/game_menu/interact_menu", "interact_box", 1, 1, false);
        images[1] = readImage("menus/game_menu/interact_menu", "highlight", 1, 1, false);
        images[2] = readImage("menus/game_menu/interact_menu", "decrease", 1, 1, false);
        images[3] = readImage("menus/game_menu/interact_menu", "increase", 1, 1, false);
        images[4] = readImage("menus/game_menu/interact_menu", "decrease_highlight", 1, 1, false);
        images[5] = readImage("menus/game_menu/interact_menu", "increase_highlight", 1, 1, false);
        images[6] = readImage("menus/game_menu/interact_menu", "counter_box", 1, 1, false);
        
        return images;
    }

    public static BufferedImage[] loadStore() {
        BufferedImage[] images = new BufferedImage[12];

        images[0] = readImage("menus/store_menu", "catalogue_box", 1, 1, false);
        images[1] = readImage("menus/store_menu", "category_box", 1, 1, false);
        images[2] = readImage("menus/store_menu", "counter_box", 1, 1, false);
        images[3] = readImage("menus/store_menu", "decrease_button", 1, 1, false);
        images[4] = readImage("menus/store_menu", "increase_button", 1, 1, false);
        images[5] = readImage("menus/store_menu", "sim_name_box", 1, 1, false);
        images[6] = readImage("menus/store_menu", "sim_money_box", 1, 1, false);
        images[7] = readImage("menus/store_menu", "store_box", 1, 1, false);
        images[8] = readImage("menus/store_menu", "title_box", 1, 1, false);
        images[9] = readImage("menus/store_menu", "decrease_highlight", 1, 1, false);
        images[10] = readImage("menus/store_menu", "increase_highlight", 1, 1, false);
        images[11] = readImage("menus/store_menu", "selector", 1, 1, false);
        
        return images;
    }

    public static BufferedImage[] loadRecipeBook() {
        BufferedImage[] images = new BufferedImage[5];

        images[0] = readImage("menus/game_menu/recipe_book", "recipe_book_title", 1, 1, false);
        images[1] = readImage("menus/game_menu/recipe_book", "recipe_book_box", 1, 1, false);
        images[2] = readImage("menus/game_menu/recipe_book", "recipe_book_catalogue_box", 1, 1, false);
        images[3] = readImage("menus/game_menu/recipe_book", "warning_box", 1, 1, false);
        images[4] = readImage("menus/game_menu/recipe_book", "selector", 1, 1, false);

        return images;
    }

    public static BufferedImage[] loadGameOverMenu() {
        BufferedImage[] images = new BufferedImage[3];

        images[0] = readImage("menus/game_over_menu", "game_over", 1, 1, false);
        images[1] = readImage("menus/game_over_menu", "esc_box", 1, 1, false);
        images[2] = readImage("menus/game_over_menu", "title_box_continueable", 1, 1, false);

        return images;
    }

    public static BufferedImage loadTile(String tile) {
        BufferedImage image = readImage("tiles", tile, 1, 1, true);
        
        return image;
    }

    public static BufferedImage[] loadDoor() {
        BufferedImage[] images = new BufferedImage[4];
        
        images[0] = readImage("entities/interactables/door", "door", 1, 1, true);
        images[1] = rotate90Clockwise(images[0]);
        images[2] = rotate90Clockwise(images[1]);
        images[3] = rotate90Clockwise(images[2]);

        return images;
    }

    public static BufferedImage[] loadWorld() {
        BufferedImage[] images = new BufferedImage[10];

        images[0] = readImage("tiles", "grass", 1, 1, false);
        images[1] = readImage("tiles/house", "house", 1, 1, false);
        images[2] = readImage("tiles", "cursor", 1, 1, false);
        images[3] = readImage("tiles/house", "unadded_house", 1, 1, false);
        images[4] = readImage("tiles/house", "selected_house", 1, 1, false);
        images[5] = readImage("tiles/house", "selected_house_occupied", 1, 1, false);
        images[6] = readImage("tiles/quarter_arrow", "up", 1, 1, false);
        images[7] = readImage("tiles/quarter_arrow", "left", 1, 1, false);
        images[8] = readImage("tiles/quarter_arrow", "down", 1, 1, false);
        images[9] = readImage("tiles/quarter_arrow", "right", 1, 1, false);
        
        return images;
    }

    public static BufferedImage[] loadAquarium() {
        BufferedImage[] images = new BufferedImage[4];
        
        images[0] = readImage("entities/interactables/aquarium", "aquarium_idle_1", 1, 1, true);
        images[1] = readImage("entities/interactables/aquarium", "aquarium_idle_2", 1, 1, true);
        images[2] = readImage("entities/interactables/aquarium", "aquarium_occupied_1", 2, 1, true);
        images[3] = readImage("entities/interactables/aquarium", "aquarium_occupied_2", 2, 1, true);
    
        return images;
    }

    public static BufferedImage loadAquariumIcon() {
        BufferedImage icon = readImage("item_icons/interactables/", "aquarium", 1, 1, false);
    
        return icon;
    }

    public static BufferedImage[] loadBeds() {
        BufferedImage[] images = new BufferedImage[6];

        images[0] = readImage("entities/interactables/beds", "bed_single_idle", 4, 1, true);
        images[1] = readImage("entities/interactables/beds", "bed_queen_idle", 4, 2, true);
        images[2] = readImage("entities/interactables/beds", "bed_king_idle", 5, 2, true);
        images[3] = readImage("entities/interactables/beds", "bed_single_occupied", 4, 1, true);
        images[4] = readImage("entities/interactables/beds", "bed_queen_occupied", 4, 2, true);
        images[5] = readImage("entities/interactables/beds", "bed_king_occupied", 5, 2, true);

        return images;
    }

    public static BufferedImage[] loadBedsIcons() {
        BufferedImage[] icons = new BufferedImage[3];

        icons[0] = readImage("item_icons/interactables", "bed_single", 1, 1, false);
        icons[1] = readImage("item_icons/interactables", "bed_queen", 1, 1, false);
        icons[2] = readImage("item_icons/interactables", "bed_king", 1, 1, false);

        return icons;
    }

    public static BufferedImage loadClock() {
        BufferedImage image = readImage("entities/interactables/clock", "clock", 1, 1, true);
        
        return image;
    }

    public static BufferedImage loadClockIcon() {
        BufferedImage icon = readImage("item_icons/interactables", "clock", 1, 1, false);

        return icon;
    }

    public static BufferedImage[] loadShower() {
        BufferedImage[] images = new BufferedImage[2];
        
        images[0] = readImage("entities/interactables/shower", "shower_idle", 1, 2, true);
        images[1] = readImage("entities/interactables/shower", "shower_occupied", 1, 2, true);
    
        return images;
    }

    public static BufferedImage loadShowerIcon() {
        BufferedImage icon = readImage("item_icons/interactables", "shower", 1, 1, false);
    
        return icon;
    }

    public static BufferedImage[] loadStoves() {
        BufferedImage[] images = new BufferedImage[4];
        
        images[0] = readImage("entities/interactables/stoves", "gas_stove_idle", 2, 1, true);
        images[1] = readImage("entities/interactables/stoves", "electric_stove_idle", 1, 1, true);
        images[2] = readImage("entities/interactables/stoves", "gas_stove_occupied", 2, 1, true);
        images[3] = readImage("entities/interactables/stoves", "electric_stove_occupied", 1, 1, true);
    
        return images;
    }

    public static BufferedImage[] loadStovesIcons() {
        BufferedImage[] images = new BufferedImage[2];
        
        images[0] = readImage("item_icons/interactables", "gas_stove", 1, 1, false);
        images[1] = readImage("item_icons/interactables", "electric_stove", 1, 1, false);
    
        return images;
    }

    public static BufferedImage[] loadTableAndChair() {
        BufferedImage[] images = new BufferedImage[4];
        
        images[0] = readImage("entities/interactables/table_and_chair", "table_and_chair_idle", 3, 3, true);
        images[1] = readImage("entities/interactables/table_and_chair", "table_and_chair_eat_1", 3, 3, true);
        images[2] = readImage("entities/interactables/table_and_chair", "table_and_chair_eat_2", 3, 3, true);
        images[3] = readImage("entities/interactables/table_and_chair", "table_and_chair_read", 3, 3, true);

        return images;
    }

    public static BufferedImage loadTableAndChairIcon() {
        BufferedImage icon = readImage("item_icons/interactables", "table_and_chair", 3, 3, false);
    
        return icon;
    }

    public static BufferedImage[] loadTelevision() {
        BufferedImage[] images = new BufferedImage[4];
        
        images[0] = readImage("entities/interactables/television", "television_idle", 2, 1, true);
        images[1] = readImage("entities/interactables/television", "television_watch", 2, 2, true);
        images[2] = readImage("entities/interactables/television", "television_karaoke_1", 2, 2, true);
        images[3] = readImage("entities/interactables/television", "television_karaoke_2", 2, 2, true);
    
        return images;
    }

    public static BufferedImage loadTelevisionIcon() {
        BufferedImage icon = readImage("item_icons/interactables", "television", 2, 1, false);
    
        return icon;
    }

    public static BufferedImage[] loadToilet() {
        BufferedImage[] images = new BufferedImage[3];
        
        images[0] = readImage("entities/interactables/toilet", "toilet_idle", 1, 1, true);
        images[1] = readImage("entities/interactables/toilet", "toilet_occupied", 1, 1, true);
        images[2] = readImage("tiles", "polished_quartz", 1, 1, true);

        return images;
    }

    public static BufferedImage loadToiletIcon() {
        BufferedImage icon = readImage("item_icons/interactables", "toilet", 1, 1, false);
    
        return icon;
    }

    public static BufferedImage[] loadTrashBin() {
        BufferedImage[] images = new BufferedImage[10];

        images[0] = readImage("entities/interactables/trash_bin", "trash_bin_empty", 1, 1, true);
        images[1] = readImage("entities/interactables/trash_bin", "trash_bin_filled", 1, 1, true);
        images[2] = readImage("entities/interactables/trash_bin", "trash_bin_empty_on_floor", 1, 1, true);
        images[3] = readImage("entities/interactables/trash_bin", "trash_bin_filled_on_floor", 1, 1, true);
        images[4] = readImage("entities/interactables/trash_bin", "trash_bin_empty_kick", 2, 1, true);
        images[5] = readImage("entities/interactables/trash_bin", "trash_bin_empty_clean", 2, 1, true);
        images[6] = readImage("entities/interactables/trash_bin", "trash_bin_filled_kick", 2, 1, true);
        images[7] = readImage("entities/interactables/trash_bin", "trash_bin_filled_clean_1", 2, 1, true);
        images[8] = readImage("entities/interactables/trash_bin", "trash_bin_filled_clean_2", 2, 1, true);
    
        return images;
    }

    public static BufferedImage loadTrashBinIcon() {
        BufferedImage icon = readImage("item_icons/interactables", "trash_bin", 1, 1, false);
    
        return icon;
    }

    public static BufferedImage[] loadRawFood() {
        BufferedImage[] images = new BufferedImage[8];
        
        images[0] = readImage("item_icons/raw_food", "carrot", 1, 1, false);
        images[1] = readImage("item_icons/raw_food", "chicken", 1, 1, false);
        images[2] = readImage("item_icons/raw_food", "milk", 1, 1, false);
        images[3] = readImage("item_icons/raw_food", "meat", 1, 1, false);
        images[4] = readImage("item_icons/raw_food", "peanut", 1, 1, false);
        images[5] = readImage("item_icons/raw_food", "potato", 1, 1, false);
        images[6] = readImage("item_icons/raw_food", "rice", 1, 1, false);
        images[7] = readImage("item_icons/raw_food", "spinach", 1, 1, false);
    
        return images;
    }

    public static BufferedImage[] loadBakedFood() {
        BufferedImage[] images = new BufferedImage[5];
        
        images[0] = readImage("item_icons/baked_food", "almond_milk", 1, 1, false);
        images[1] = readImage("item_icons/baked_food", "rice_chicken", 1, 1, false);
        images[2] = readImage("item_icons/baked_food", "rice_curry", 1, 1, false);
        images[3] = readImage("item_icons/baked_food", "cut_vegetables", 1, 1, false);
        images[4] = readImage("item_icons/baked_food", "steak", 1, 1, false);
    
        return images;
    }

    public static Color setColor(int selectedColor) {
        Color color = null;
        if (selectedColor == 0) color = Color.YELLOW;
        if (selectedColor == 1) color = Color.ORANGE;
        if (selectedColor == 2) color = new Color(215, 0, 20); // red color
        if (selectedColor == 3) color = Color.MAGENTA;
        if (selectedColor == 4) color = Color.PINK;
        if (selectedColor == 5) color = Color.BLUE;
        if (selectedColor == 6) color = Color.CYAN;
        if (selectedColor == 7) color = new Color(0, 254, 10); // green color

        return color;
    }

    public static BufferedImage simColorSelector(int selectedColor) {
        BufferedImage newImage = readImage("entities/sim", "sim_down", 1, 1, false);
        Color oldShirtColor = new Color(215, 0, 20); // red color
        Color newShirtColor = setColor(selectedColor);

        float[] oldShirtColorHsb = new float[3];
        float[] newShirtColorHsb = new float[3];
        float hueDiff;

        Color.RGBtoHSB(oldShirtColor.getRed(), oldShirtColor.getGreen(), oldShirtColor.getBlue(), oldShirtColorHsb);
        Color.RGBtoHSB(newShirtColor.getRed(), newShirtColor.getGreen(), newShirtColor.getBlue(), newShirtColorHsb);
    
        // to change the shirt color
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                int rgb = newImage.getRGB(x, y);
                if ((rgb >> 24) == 0x00) continue; // if pixel is transparent, skip color transformation

                Color pixelColor = new Color(rgb);
    
                // Check if the pixel color is within the range of hues
                float[] pixelHsb = new float[3];
                Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelHsb);
                hueDiff = Math.abs(pixelHsb[0] - oldShirtColorHsb[0]);

                if (hueDiff <= 0.1 || hueDiff >= 0.9) {
                    // Keep the saturation and brightness values of the pixel, but change its hue to the new hue
                    newShirtColorHsb[1] = pixelHsb[1]; // keep saturation value
                    newShirtColorHsb[2] = pixelHsb[2]; // keep brightness value
                    Color newPixelColor = new Color(Color.HSBtoRGB(newShirtColorHsb[0], newShirtColorHsb[1], newShirtColorHsb[2]));
    
                    newImage.setRGB(x, y, newPixelColor.getRGB());
                }
            }
        }
        return newImage;
    }

    public static BufferedImage changeSimColor(BufferedImage simImage, Sim sim) {
        Color oldShirtColor = new Color(215, 0, 20); // red color
        Color newShirtColor = sim.getShirtColor();

        float[] oldShirtColorHsb = new float[3];
        float[] newShirtColorHsb = new float[3];
        float hueDiff;

        Color.RGBtoHSB(oldShirtColor.getRed(), oldShirtColor.getGreen(), oldShirtColor.getBlue(), oldShirtColorHsb);
        Color.RGBtoHSB(newShirtColor.getRed(), newShirtColor.getGreen(), newShirtColor.getBlue(), newShirtColorHsb);
    
        // to change the shirt color
        for (int x = 0; x < simImage.getWidth(); x++) {
            for (int y = 0; y < simImage.getHeight(); y++) {
                int rgb = simImage.getRGB(x, y);
                if ((rgb >> 24) == 0x00) continue; // if pixel is transparent, skip color transformation

                Color pixelColor = new Color(rgb);
    
                // Check if the pixel color is within the range of hues
                float[] pixelHsb = new float[3];
                Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelHsb);
                hueDiff = Math.abs(pixelHsb[0] - oldShirtColorHsb[0]);

                if (hueDiff <= 0.05 || hueDiff >= 0.95) {
                    // Keep the saturation and brightness values of the pixel, but change its hue to the new hue
                    newShirtColorHsb[1] = pixelHsb[1]; // keep saturation value
                    newShirtColorHsb[2] = pixelHsb[2]; // keep brightness value
                    Color newPixelColor = new Color(Color.HSBtoRGB(newShirtColorHsb[0], newShirtColorHsb[1], newShirtColorHsb[2]));
    
                    simImage.setRGB(x, y, newPixelColor.getRGB());
                }
            }
        }
        return simImage;
    }

    public static BufferedImage showSimPreview(Sim sim) {
        BufferedImage newImage = readImage("entities/sim", "sim_down", 1, 1, false);

        newImage = changeSimColor(newImage, sim);

        return newImage;
    }
}