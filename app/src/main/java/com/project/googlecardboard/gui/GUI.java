package com.project.googlecardboard.gui;


import com.project.googlecardboard.matrix.TransformationMatrix;

/**
 * Created by Garrett on 27/10/2015.
 */
public class GUI {

    private TransformationMatrix matrix;

    public GUI(float radius, float pitch, float yaw){
        double x = radius * Math.sin(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        double y = radius * Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        double z = radius * Math.cos(Math.toRadians(pitch));
        this.matrix = new TransformationMatrix((float) x, (float) y, (float) z, 0.0f, pitch, yaw);
    }

    public float[] getMatrix(){
        return matrix.getMatrix();
    }

}
