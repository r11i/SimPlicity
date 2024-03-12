package src.main;

public abstract class Consts {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int TILE_SIZE = 16;
    public static final int CENTER_X = (WIDTH - TILE_SIZE) / 2;
    public static final int CENTER_Y = (HEIGHT - TILE_SIZE) / 2;
    public static final int SCALE = 4;
    public static final int SCALED_TILE = TILE_SIZE * SCALE;
    public static final int ONE_MINUTE = 60;
    public static final int ONE_SECOND = 1;

    // FOR THREADS
    public static final int THREAD_ONE_SECOND = 1000;
    public static final int THREAD_ONE_MINUTE = 60 * THREAD_ONE_SECOND;
    
    // PLAY ARENA SIZE
    public static final int PLAY_ARENA_X_LEFT = 208;
    public static final int PLAY_ARENA_Y_UP = 51;
    public static final int PLAY_ARENA_X_RIGHT = PLAY_ARENA_X_LEFT + (SCALED_TILE * 5);
    public static final int PLAY_ARENA_Y_DOWN = PLAY_ARENA_Y_UP + (SCALED_TILE * 5);
    public static final int OFFSET_Y = 57;
}
