package com.project.googlecardboard.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.project.googlecardboard.R;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUIModel;
import com.project.googlecardboard.gui.GUITexture;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Garrett on 23/10/2015.
 */
public class Renderer implements CardboardView.StereoRenderer{

    private Context context;

    private static final float YAW_LIMIT = 0.15f;
    private static final float PITCH_LIMIT = 0.15f;

    private final StaticShader shader;
    private final GUIModel model;
    private final List<GUI> guis;

    private float[] headView;
    private int index = 0;

    public Renderer(Context context, StaticShader shader){
        this.context = context;
        this.shader = shader;
        this.model = new GUIModel();
        this.guis = new ArrayList<GUI>();
        this.headView = new float[16];

        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inScaled = false;

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
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        headTransform.getHeadView(headView, 0);
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
            if(!isLookingAt(gui) && gui.getRadius() != 10.0f){
                gui.setRadius(10.0f);
            }
            shader.loadTransformationMatrix(gui.getMatrix());
            shader.loadTexture(gui.getTexture().getID());
            shader.loadColour((isLookingAt(gui)) ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
            shader.loadTextureCoordinates(gui.getTexture().textureCoordinates);
            shader.enableAttributes();
            drawGUI();
        }
    }

    /**
     * Called before a frame is finished being drawn
     * @param viewport The viewport of the full GL surface
     */
    @Override
    public void onFinishFrame(Viewport viewport){
        shader.stop();
        int resourceID = (index % 2 == 0) ? R.drawable.garrett : R.drawable.googlecardboard;
        for(GUI gui : guis){
            gui.updateTexture(BitmapFactory.decodeResource(context.getResources(), resourceID));
        }
        guis.get(0).getTexture().loadBitmap(BitmapFactory.decodeResource(context.getResources(), resourceID));
        index = (index + 1) % 2;
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
        for(GUI gui : guis){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.garrett);
            GUITexture texture = new GUITexture(bitmap);
            gui.setTexture(texture);
            bitmap.recycle();
        }
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

    /**
     * Draws the cube
     */
    public void drawGUI(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, model.getModel().length / 3);
    }

    /**
     * Clears the background to a specific colour
     * @param red Red component
     * @param green Green component
     * @param blue Blue component
     * @param alpha Transparency component
     */
    public void clearBackgroundTo(float red, float green, float blue, float alpha){
        GLES20.glClearColor(red / 255.0f, green / 255.0f, blue / 255.0f, alpha);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Called when the cardboard's trigger is activated
     * First called in DroneActivity, which calls this function
     */
    public void onCardboardTrigger(){
        for(GUI gui : guis){
            if(isLookingAt(gui) && gui.getRadius() != 5.0f){
                gui.setRadius(5.0f);
            }
        }
    }

    /**
     * Checks whether we are looking at a specific GUI
     * @param gui GUI to be considered
     * @return Whether we are looking at this GUI
     */
    public boolean isLookingAt(GUI gui){
        float[] initVec = { 0, 0, 0, 1.0f };
        float[] objPositionVec = new float[4];
        float[] modelView = new float[16];
        // Convert object space to camera space. Use the headView from onNewFrame.
        Matrix.multiplyMM(modelView, 0, headView, 0, gui.getMatrix(), 0);
        Matrix.multiplyMV(objPositionVec, 0, modelView, 0, initVec, 0);

        float pitch = (float) Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float) Math.atan2(objPositionVec[0], -objPositionVec[2]);

        return Math.abs(pitch) < PITCH_LIMIT && Math.abs(yaw) < YAW_LIMIT;
    }
}