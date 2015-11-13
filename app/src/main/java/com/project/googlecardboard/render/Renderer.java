package com.project.googlecardboard.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUIModel;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Garrett on 23/10/2015.
 */
public class Renderer implements CardboardView.StereoRenderer{

    private final Shader shader;
    private final GUIModel model;
    private final List<GUI> guis;

    // Matrices
    private Camera camera;

    public Renderer(Shader shader){
        this.shader = shader;
        this.model = new GUIModel();
        this.guis = new ArrayList<GUI>();

        this.camera = new Camera();
    }

    /**
     * Called when a new frame is about to be drawn
     * @param headTransform The head transformation in the new frame
     */
    @Override
    public void onNewFrame(HeadTransform headTransform){
        headTransform.getHeadView(camera.getHeadView(), 0);
    }

    /**
     * Called when a frame is drawn for an eye
     * @param eye The eye to render, including transformations applied
     */
    @Override
    public void onDrawEye(Eye eye){
        //renderer.render();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.multiplyMM(camera.getView(), 0, eye.getEyeView(), 0, camera.getPosition(), 0);

        float[] perspective = eye.getPerspective(Camera.Z_NEAR, Camera.Z_FAR);
        //Matrix.multiplyMM(modelView, 0, view, 0, modelCube, 0);
        //Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);
        //drawCube();
    }

    /**
     * Called before a frame is finished being drawn
     * @param viewport The viewport of the full GL surface
     */
    @Override
    public void onFinishFrame(Viewport viewport){

    }

    /**
     * Called when the surface is created or recreated
     * @param config The EGL configuration used when creating the surface
     */
    @Override
    public void onSurfaceCreated(EGLConfig config){
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f);
    }

    /**
     * Called when there is a change in surface dimensions
     * @param width New width of the surface for a single eye, in pixels
     * @param height New height of the surface for a single eye, in pixels
     */
    @Override
    public void onSurfaceChanged(int width, int height){

    }

    /**
     * Called when rendering is shutdown
     */
    @Override
    public void onRendererShutdown(){

    }
}