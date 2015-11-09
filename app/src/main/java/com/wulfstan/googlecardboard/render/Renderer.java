package com.wulfstan.googlecardboard.render;

import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.wulfstan.googlecardboard.DroneActivity;
import com.wulfstan.googlecardboard.gui.GUI;
import com.wulfstan.googlecardboard.gui.GUIModel;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Garrett on 23/10/2015.
 */
public class Renderer {

    private final Shader shader;
    private final GUIModel model;
    private final List<GUI> guis;

    public Renderer(Shader shader){
        this.shader = shader;
        this.model = new GUIModel();
        this.guis = new ArrayList<GUI>();
    }

    public void render(){
        DroneActivity.checkGLError("onRender");
        clearBackgroundColour(0.8f, 0.0f, 0.0f, 1.0f);
        //shader.start();
        //drawGUIs();
        //shader.stop();
    }

    private void drawGUIs(){
        blendAlpha();
        disableDepthTesting();
        bindModel();
        for(GUI gui : guis){
            drawGUI(gui);
        }
        unbindModel();
        unblendAlpha();
        enableDepthTesting();
    }

    private void drawGUI(GUI gui){

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, model.getVertexCount());
    }

    private void bindModel(){

    }

    private void unbindModel(){

    }

    /**
     * Clears the background to a specific colour
     * @param red Redness
     * @param green Greenness
     * @param blue Blueness
     * @param alpha Transparency
     */
    private void clearBackgroundColour(float red, float green, float blue, float alpha){
        GLES20.glClearColor(red, green, blue, alpha);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    private void blendAlpha(){
        GLES11.glEnable(GLES11.GL_BLEND);
        GLES11.glBlendFunc(GLES11.GL_SRC_ALPHA, GLES11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void unblendAlpha(){
        GLES11.glDisable(GLES11.GL_BLEND);
    }

    private void enableDepthTesting(){
        GLES11.glEnable(GLES11.GL_DEPTH_TEST);
    }

    private void disableDepthTesting(){
        GLES11.glDisable(GLES11.GL_DEPTH_TEST);
    }
}