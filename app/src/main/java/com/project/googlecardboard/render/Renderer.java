package com.project.googlecardboard.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUIModel;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Garrett on 23/10/2015.
 */
public class Renderer implements CardboardView.StereoRenderer{

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;

    private final StaticShader shader;
    private final GUIModel model;
    private final List<GUI> guis;

    // Matrices
    //private Camera camera;

    public Renderer(StaticShader shader){
        this.shader = shader;
        this.model = new GUIModel();
        this.guis = new ArrayList<GUI>();

        guis.add(new GUI(10, -20, -60));
        guis.add(new GUI(10, -20, -30));
        guis.add(new GUI(10, -20, 0));
        guis.add(new GUI(10, -20, 30));
        guis.add(new GUI(10, -20, 60));

        guis.add(new GUI(10, 0, -60));
        guis.add(new GUI(10, 0, -30));
        guis.add(new GUI(10, 0, 0));
        guis.add(new GUI(10, 0, 30));
        guis.add(new GUI(10, 0, 60));

        guis.add(new GUI(10, 20, -60));
        guis.add(new GUI(10, 20, -30));
        guis.add(new GUI(10, 20, 0));
        guis.add(new GUI(10, 20, 30));
        guis.add(new GUI(10, 20, 60));
    }

    /**
     * Called when a new frame is about to be drawn
     * @param headTransform The head transformation in the new frame
     */
    @Override
    public void onNewFrame(HeadTransform headTransform){
        //headTransform.getHeadView(camera.getHeadView(), 0);
        shader.start();
    }

    /**
     * Called when a frame is drawn for an eye
     * @param eye The eye to render, including transformations applied
     */
    @Override
    public void onDrawEye(Eye eye){
        clearBackgroundTo(124.0f, 252.0f, 0.0f, 1.0f);
        ViewMatrix viewMatrix = new ViewMatrix(eye);
        ProjectionMatrix projectionMatrix = new ProjectionMatrix(eye);
        shader.loadViewMatrix(viewMatrix.getMatrix());
        shader.loadProjectionMatrix(projectionMatrix.getMatrix());
        shader.loadPosition(model.getModel());
        for(GUI gui : guis){
            shader.loadTransformationMatrix(gui.getMatrix());
            drawCube();
        }
    }

    /**
     * Called before a frame is finished being drawn
     * @param viewport The viewport of the full GL surface
     */
    @Override
    public void onFinishFrame(Viewport viewport){
        shader.stop();
    }

    /**
     * Called when the surface is created or recreated
     * @param config The EGL configuration used when creating the surface
     */
    @Override
    public void onSurfaceCreated(EGLConfig config){
        shader.init();
        shader.loadUniformLocations();
        shader.loadAttributeLocations();
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
        shader.clean();
    }

    public void drawCube(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    public void clearBackgroundTo(float red, float green, float blue, float alpha){
        GLES20.glClearColor(red / 255.0f, green / 255.0f, blue / 255.0f, alpha);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }
}