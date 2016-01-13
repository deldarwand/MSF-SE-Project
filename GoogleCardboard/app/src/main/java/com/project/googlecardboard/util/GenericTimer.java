package com.project.googlecardboard.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Garrett on 08/01/2016.
 */
public class GenericTimer {

    private Timer timer;
    protected TimerTask task;
    protected long delay;
    protected long period;
    private boolean isRunning;

    public GenericTimer(){
        this.timer = null;
        this.isRunning = false;
        this.delay = 0;
        this.period = 0;
    }

    public GenericTimer(TimerTask task, long delay, long period){
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
