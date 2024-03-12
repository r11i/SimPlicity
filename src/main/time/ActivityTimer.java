package src.main.time;

import src.entities.sim.Sim;

public class ActivityTimer {
    private Sim sim;
    private String activity;
    private int timeRemaining;
    private int duration;

    public ActivityTimer(Sim sim, String activity, int timeRemaining, int duration) {
        this.sim = sim;
        this.activity = activity;
        this.timeRemaining = timeRemaining;
        this.duration = duration;
    }

    public Sim getSim() {
        return sim;
    }

    public String getActivity() {
        return activity;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public int getDuration() {
        return duration;
    }

    public void setSim(Sim sim) {
        this.sim = sim;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
