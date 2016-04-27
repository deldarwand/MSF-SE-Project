package com.project.googlecardboard.gui;


import android.opengl.Matrix;

import com.project.googlecardboard.graph.Drawing;
import com.project.googlecardboard.matrix.TransformationMatrix;
import com.project.googlecardboard.render.animation.Animation;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.Constants;
import com.project.googlecardboard.util.IO;

import pl.packet.Packet;

/**
 * Created by Garrett on 27/10/2015.
 */
public abstract class GUI implements Drawing, Comparable<GUI>{

    private float radius;
    private float pitch;
    private float yaw;
    private boolean isBeingViewed;
    private TransformationMatrix matrix;

    private Animation animation;

    protected ShaderType shaderType;

    public GUI(float radius, float pitch, float yaw, ShaderType shaderType){
        //pitch *= 20;
        //yaw *= 30;
        setRadius(radius);
        setPitch(pitch);
        setYaw(yaw);
        this.isBeingViewed = false;

        this.animation = Animation.EMPTY;

        this.shaderType = shaderType;
    }

    /* GETTERS */

    public float getRadius(){

        return radius;
    }

    public float getPitch(){
        return pitch;
    }

    public float getYaw(){
        return yaw;
    }

    public boolean isBeingViewed() {
        return isBeingViewed;
    }

    public float[] getMatrix(){
        matrix = new TransformationMatrix(radius, 0.0f, pitch, 90 - yaw + 100);
        return matrix.getMatrix();
    }

    public ShaderType getShaderType(){
        return shaderType;
    }

    public Shader getShader(){
        return shaderType.getShader();
    }

    /* SETTERS */

    public void setRadius(float radius){
        this.radius = radius;
    }

    public void setPitch(float pitch){
        this.pitch = pitch;
    }

    public void setYaw(float yaw){
        this.yaw = yaw;
    }

    public void updateGUI(float[] headView){
        float[] initVec = { 0, 0, 0, 1.0f };
        float[] objPositionVec = new float[4];
        float[] modelView = new float[16];
        Matrix.multiplyMM(modelView, 0, headView, 0, this.getMatrix(), 0);
        Matrix.multiplyMV(objPositionVec, 0, modelView, 0, initVec, 0);
        float pitch = (float) Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float) Math.atan2(objPositionVec[0], -objPositionVec[2]);
        isBeingViewed = (Math.abs(pitch) < Constants.PITCH_LIMIT && Math.abs(yaw) < Constants.YAW_LIMIT);
    }

    public void setAnimation(Animation animation){
        this.animation = animation;
    }

    public void setAnimatedRadius(float radius, Animation animation){
        setRadius(radius);
        setAnimation(animation);
    }

    /* DRAWING */

    public abstract void draw();

    /* UPDATING */

    public abstract void update(Packet packet);

    public void playAnimation(){
        if(animation.hasNext()){
            if(animation.length() == 1){
                IO.vibrate(50);
            }
            setRadius(animation.next());
        } else{
            animation = Animation.EMPTY;
        }
    }

    /* COMPARABLE */

    public int compareTo(GUI gui){
        return (getRadius() > gui.getRadius()) ? -1 : (getRadius() < gui.getRadius()) ? 1 : 0;
    }

    /* TEARDOWN */

    public abstract void teardown();
}
