package com.project.googlecardboard.gui;


import com.project.googlecardboard.matrix.TransformationMatrix;

/**
 * Created by Garrett on 27/10/2015.
 */
public class GUI {

    private TransformationMatrix matrix;

    public GUI(float radius, float pitch, float yaw){
        //double x = radius * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        //double y = radius * Math.sin(Math.toRadians(pitch));
        //double z = radius * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        this.matrix = new TransformationMatrix(radius, 0.0f, pitch, 90 - yaw);
    }

    public float[] getMatrix(){
        return matrix.getMatrix();
    }

}
