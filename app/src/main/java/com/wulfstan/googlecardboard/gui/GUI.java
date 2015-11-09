package com.wulfstan.googlecardboard.gui;

/**
 * Created by Garrett on 27/10/2015.
 */
public class GUI {

    private final float x;
    private final float y;
    private final float scale;

    public GUI(float x, float y, float scale){
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getScale(){
        return scale;
    }
}
