package com.project.googlecardboard.gui;


import android.graphics.SurfaceTexture;

import com.project.googlecardboard.matrix.TransformationMatrix;

/**
 * Created by Garrett on 27/10/2015.
 */
public class GUI {

    private float radius;
    private float pitch;
    private float yaw;

    private TransformationMatrix matrix;
    private GUITexture texture;

    public GUI(float radius, float pitch, float yaw){
        setRadius(radius);
        setPitch(pitch);
        setYaw(yaw);
        //double x = radius * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        //double y = radius * Math.sin(Math.toRadians(pitch));
        //double z = radius * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        this.matrix = new TransformationMatrix(radius, 0.0f, pitch, 90 - yaw);
    }

    public float[] getMatrix(){
        matrix = new TransformationMatrix(radius, 0.0f, pitch, 90 - yaw);
        return matrix.getMatrix();
    }

    public GUITexture getTexture(){
        return texture;
    }

    public float getRadius(){
        return radius;
    }

    public float getPitch(){
        return pitch;
    }

    public float getYaw(){
        return yaw;
    }

    public void setRadius(float radius){
        this.radius = radius;
    }

    public void setPitch(float pitch){
        this.pitch = pitch;
    }

    public void setYaw(float yaw){
        this.yaw = yaw;
    }

}
