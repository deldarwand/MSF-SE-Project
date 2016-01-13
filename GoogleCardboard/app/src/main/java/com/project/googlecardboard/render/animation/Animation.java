package com.project.googlecardboard.render.animation;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Garrett on 10/01/2016.
 */
public class Animation {

    public static final Animation EMPTY = new Animation(new float[]{});

    protected Queue<Float> animation;

    public Animation(){
        this.animation = new LinkedList<>();
    }

    public Animation(float[] animation){
        this();
        for(float value : animation){
            this.animation.offer(value);
        }
    }

    public boolean hasNext(){
        return (length() != 0);
    }

    public float next(){
        return animation.poll();
    }

    public int length(){
        return animation.size();
    }
}
