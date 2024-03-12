package src.main.time;

import java.util.ConcurrentModificationException;
import java.util.ArrayList;

import src.main.Consts;
import src.entities.sim.Sim;
import src.main.UserInterface;
import src.world.World;

public class GameTime implements Runnable {
    private static GameTime gt = new GameTime();
    private static Thread gtThread = new Thread(gt);

    public static int initialTimeRemaining = Consts.ONE_MINUTE * 12;
    public static int timeRemaining;
    public static int day;
    private static ArrayList<ActivityTimer> listOfActiveActivities = new ArrayList<>();

    private GameTime() {}

    public static void init(int day, int timeRemaining) {
        GameTime.day = day;
        GameTime.timeRemaining = timeRemaining;
        
        if (!gtThread.isAlive()) {
            gtThread.start();
        }
    }

    // GETTERS
    public static GameTime getInstance() {
        return gt;
    }

    public static Thread getThreadInstance() {
        return gtThread;
    }

    public static ArrayList<ActivityTimer> getListOfActiveActivities() {
        return listOfActiveActivities;
    }

    public static ActivityTimer getActivityTimer(Sim sim, String activity) {
        try {
            for (ActivityTimer activityTimer : listOfActiveActivities) {
                Sim activeSim = activityTimer.getSim();
        
                boolean simIsActive = sim.getName().equals(activeSim.getName());
                boolean activityIsActive = activity.equals(activityTimer.getActivity());
    
                if (simIsActive && activityIsActive) {
                    return activityTimer;
                }
            }
        }
        catch (ConcurrentModificationException cme) {}

        return null;
    }

    // IMPLEMENTATION OF INTERFACE
    @Override
    public void run() {
        while (gtThread.isAlive()) {
            if (UserInterface.isPaused()) continue;
            if (listOfActiveActivities.isEmpty()) continue;

            try {
                Thread.sleep(Consts.THREAD_ONE_SECOND);
                
                for (ActivityTimer activityTimer : listOfActiveActivities) {
                    int timeRemaining = activityTimer.getTimeRemaining();

                    if (timeRemaining <= 0) listOfActiveActivities.remove(activityTimer);
                    
                    activityTimer.setTimeRemaining(timeRemaining - 1);
                }
                decrementTimeRemaining();

                World world = UserInterface.getWorld();
                ArrayList<Sim> listOfSim = world.getListOfSim();

                for (Sim sim : listOfSim) {
                    int timeNotSlept = sim.getTimeNotSlept();
                    int timeNotTakenLeak = sim.getTimeNotTakenLeak();

                    sim.setTimeNotSlept(timeNotSlept + 1);
                    sim.setTimeNotTakenLeak(timeNotTakenLeak + 1);
                }
            }
            catch (InterruptedException ie) {}
            catch (ConcurrentModificationException cme) {}
        }
    }

    public static boolean isAlive(Sim sim, String activity) {
        if (listOfActiveActivities.isEmpty()) return false;
        
        boolean isAlive = false;
        try {
            for (ActivityTimer activityTimer : listOfActiveActivities) {
                Sim activeSim = activityTimer.getSim();
    
                boolean simIsActive = sim.getName().equals(activeSim.getName());
                boolean activityIsActive = activity.equals(activityTimer.getActivity());
    
                if (simIsActive && activityIsActive) {
                    isAlive = true;
                }
            }
        }
        catch (ConcurrentModificationException cme) {}
        catch (NullPointerException npe) {}
        
        return isAlive;
    }

    // SETTERS
    public static void addActivityTimer(Sim sim, String activity, int timeRemaining, int duration) {
        ActivityTimer activityTimer = new ActivityTimer(sim, activity, timeRemaining, duration);
        listOfActiveActivities.add(activityTimer);
    }

    public static void decrementTimeRemaining() {
        timeRemaining--;
        if (timeRemaining == 0)
        {
            timeRemaining = initialTimeRemaining;
            incrementDay();
        }
    }

    public static void incrementDay() {
        day++;
    }

    public static void decreaseTimeRemaining(int time) {
        timeRemaining -= time;

        for (ActivityTimer activityTimer : listOfActiveActivities) {
            int timeRemaining = activityTimer.getTimeRemaining();
            
            activityTimer.setTimeRemaining(timeRemaining - time);
        }

        World world = UserInterface.getWorld();
        ArrayList<Sim> listOfSim = world.getListOfSim();

        for (Sim sim : listOfSim) {
            int timeNotSlept = sim.getTimeNotSlept();
            int timeNotTakenLeak = sim.getTimeNotTakenLeak();

            sim.setTimeNotSlept(timeNotSlept + time);
            sim.setTimeNotTakenLeak(timeNotTakenLeak + time);
        }

        if (timeRemaining <= 0){
            int timeLeft = 0 - timeRemaining;
            timeRemaining = initialTimeRemaining - timeLeft;
            incrementDay();
        }
    }
}
