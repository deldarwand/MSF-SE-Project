package com.project.googlecardboard.gui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.Matrix;

import com.project.googlecardboard.graph.Graph;
import com.project.googlecardboard.graph.LineGraph;
import com.project.googlecardboard.matrix.TransformationMatrix;

/**
 * Created by Garrett on 27/10/2015.
 */
public class GUI {

    private float radius;
    private float pitch;
    private float yaw;


    private static final float PITCH_LIMIT = 0.15f, YAW_LIMIT = 0.15f;

    private TransformationMatrix matrix;
    private GUITexture texture;
    private LineGraph graph;

    public GUI(float radius, float pitch, float yaw, Context context){
        pitch *= 20;
        yaw *= 30;
        setRadius(radius);
        setPitch(pitch);
        setYaw(yaw);
        //double x = radius * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        //double y = radius * Math.sin(Math.toRadians(pitch));
        //double z = radius * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        this.texture = new GUITexture();
        this.graph = new LineGraph(30, context, this);
    }

    //public GUI(float radius, float pitch, float yaw){
     //   this(radius, pitch, yaw);
    //}

    //public float[] getModel(){
  //      return graph.getModel();
   // }

    public float[] getMatrix(){
        matrix = new TransformationMatrix(radius, 0.0f, pitch, 90 - yaw + 100);
        return matrix.getMatrix();
    }

    public void clean()
    {
        this.graph.clean();
    }

    public boolean isLookingAtMe(float[] headView)
    {
        float[] initVec = { 0, 0, 0, 1.0f };
        float[] objPositionVec = new float[4];
        float[] modelView = new float[16];
        // Convert object space to camera space. Use the headView from onNewFrame.
        Matrix.multiplyMM(modelView, 0, headView, 0, this.getMatrix(), 0);
        Matrix.multiplyMV(objPositionVec, 0, modelView, 0, initVec, 0);

        float pitch = (float) Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float) Math.atan2(objPositionVec[0], -objPositionVec[2]);

        return Math.abs(pitch) < PITCH_LIMIT && Math.abs(yaw) < YAW_LIMIT;
    }

    public LineGraph getGraph(){
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
