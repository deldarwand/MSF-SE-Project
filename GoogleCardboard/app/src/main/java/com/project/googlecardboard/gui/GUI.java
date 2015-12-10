package com.project.googlecardboard.gui;


import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;

import com.project.googlecardboard.graph.Graph;
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
    private Graph graph;

    public GUI(float radius, float pitch, float yaw, Graph graph){
        pitch *= 20;
        yaw *= 30;
        setRadius(radius);
        setPitch(pitch);
        setYaw(yaw);
        //double x = radius * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        //double y = radius * Math.sin(Math.toRadians(pitch));
        //double z = radius * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        this.texture = new GUITexture();
        this.graph = graph;
    }

    public GUI(float radius, float pitch, float yaw){
        this(radius, pitch, yaw, Graph.EMPTY);
    }

    public float[] getModel(){
        return graph.getModel();
    }

    public float[] getMatrix(){
        matrix = new TransformationMatrix(radius, 0.0f, pitch, 90 - yaw + 100);
        return matrix.getMatrix();
    }

    public Graph getGraph(){
        return graph;
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

    public void setTexture(GUITexture texture){
        this.texture = texture;
    }

    public void updateTexture(Bitmap bitmap){
        texture.loadBitmap(bitmap);
    }

}
