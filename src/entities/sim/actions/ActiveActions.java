package src.entities.sim.actions;

import src.main.Consts;
import src.main.UserInterface;
import src.main.time.GameTime;
import src.world.Room;

import src.entities.interactables.Interactables;
import src.entities.sim.Sim;

public class ActiveActions {
    public static int workDuration = 120 * Consts.ONE_SECOND;
    public static int exerciseDuration = 20 * Consts.ONE_SECOND;

    public static void work (Sim sim, int duration){ // TO DO LIST: durasi validasi di dalam work atau diluar (main program)
        Thread working = new Thread() {
            @Override
            public void run() {
                int initialDurationWorked = sim.getDurationWorked();
                sim.setStatus("Working");

                GameTime.addActivityTimer(sim, "Working", duration, duration);
                
                while (GameTime.isAlive(sim, "Working")) {
                    try {
                        if (UserInterface.isPaused()) continue;
                        
                        int durationWorked = sim.getDurationWorked();
                        
                        Thread.sleep(Consts.THREAD_ONE_SECOND);
                        if (durationWorked <  initialDurationWorked + workDuration) {
                            sim.setDurationWorked(sim.getDurationWorked() + 1);
                        }

                        if (durationWorked == 0) continue;
                        
                        // decrease sim mood and hunger every 30 seconds
                        if (durationWorked % (Consts.ONE_SECOND * 30) == 0) {
                            int simHunger = sim.getHunger();
                            int simMood = sim.getMood();
                            sim.setHunger(simHunger - 10);
                            sim.setMood(simMood - 10);
                        }

                        // increase sim salary every 4 minutes of work done
                        if (durationWorked == (Consts.ONE_MINUTE * 4)) {
                            int simMoney = sim.getMoney();
                            int salary = sim.getProfession().getSalary();
                            sim.setMoney(simMoney + salary);
                            sim.setDurationWorked(0);
                        }
                    } catch (InterruptedException e) {}
                }
                sim.resetStatus();
            }
        };
        working.start();
    }

    public static void exercise (Sim sim, int duration){
        Thread exercising = new Thread() {
            @Override
            public void run() {
                int timeExercised = 0;
                sim.setStatus("Exercising");

                GameTime.addActivityTimer(sim, "Exercising", duration, duration);
                
                while (GameTime.isAlive(sim, "Exercising")) {
                    // skip the whole block if game is currently paused
                    if (UserInterface.isPaused()) continue;

                    try {
                        Thread.sleep(Consts.THREAD_ONE_SECOND);

                        timeExercised++;
                        if (timeExercised % 5 == 0) {
                            sim.setHealth(sim.getHealth() + 5); // increase sim's health by 5 every 20 seconds 
                            sim.setHunger(sim.getHunger() - 5); // decrease sim's hunger by 5 every 20 seconds
                            sim.setMood(sim.getMood() + 10); // increase sim's mood by 10 every 20 seconds
                        }
                    }
                    catch (InterruptedException e) {}
                }
                sim.resetStatus();
            }
        };
        exercising.start();
    }

    public static void interact() {
        Sim sim = UserInterface.getCurrentSim();
        Room currentRoom = sim.getCurrentRoom();
        Interactables object = sim.getInteractionHandler().getInteractableObject();
        
        if (object == null) {
            return;
        }
        
        if (object.isOccupied()) {
            return;
        }

        if (currentRoom.isEditingRoom()) {
            return;
        }

        object.interact(sim);
    }

    public static void visitAnotherSim() {
        UserInterface.viewWorld();
    }
}
