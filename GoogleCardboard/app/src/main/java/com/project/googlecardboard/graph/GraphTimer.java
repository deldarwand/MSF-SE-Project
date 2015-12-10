package com.project.googlecardboard.graph;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Garrett on 10/12/2015.
 */
public class GraphTimer {

    private Timer timer;
    private TimerTask task;
    private final long delay;
    private final long period;
    private boolean isRunning;

    public GraphTimer(TimerTask task, long delay, long period){
        this.task = task;
        this.isRunning = false;
        this.delay = delay;
        this.period = period;
    }

    public void start(){
        if(task != null){
            timer = new Timer();
            timer.schedule(task, delay, period);
            isRunning = true;
        }

    }

    public void stop(){
        if(timer != null){
            timer.cancel();
            timer = null;
            isRunning = false;
        }
    }

    public boolean isRunning(){
        return isRunning;
    }

}
