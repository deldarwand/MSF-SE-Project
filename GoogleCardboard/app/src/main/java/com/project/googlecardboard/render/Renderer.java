package com.project.googlecardboard.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.project.googlecardboard.R;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.graph.GraphTimer;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUIModel;
import com.project.googlecardboard.gui.TexturedGUI;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;
import com.project.googlecardboard.meshDrawing.Mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Garrett on 23/10/2015.
 */

public class Renderer implements CardboardView.StereoRenderer{

    private Context context;

    private static Bitmap b1 = null, b2 = null;

    private static final float YAW_LIMIT = 0.15f;
    private static final float PITCH_LIMIT = 0.15f;


    private final GUIModel model;
    private final List<GUI> guis;

    private float[] headView;
    private int index = 0;

    private static Random random = new Random();
    private GraphTimer timer;
    private GraphTimer videoTimer;

    public Renderer(Context context, StaticShader shader){
        this.context = context;
        //this.shader = shader;
        this.model = new GUIModel();
        this.guis = new ArrayList<GUI>();
        this.headView = new float[16];

        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inScaled = false;



        this.timer = new GraphTimer(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 50, 40);

        this.videoTimer = new GraphTimer(new TimerTask() {
            @Override
            public void run() {
                updateVideo();
            }
        }, 50, 40);


    }

    public void updateVideo() {
        for (GUI gui : guis) {
            Thread t;
            if (gui instanceof TexturedGUI)
            {
                t = new Thread((TexturedGUI)gui);

                t.start();
            }

        }
    }

    public void update() {
        for (GUI gui : guis) {
            Thread t;
            if (!(gui instanceof TexturedGUI))
            {
                t = new Thread(gui.getGraph());
                t.start();
            }
        }
    }

    /**
     * Called when a new frame is about to be drawn
     * @param headTransform The head transformation in the new frame
     */
    @Override
    public void onNewFrame(HeadTransform headTransform){
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glLineWidth(5.0f);
        headTransform.getHeadView(headView, 0);
    }

    /**
     * Called when a frame is drawn for an eye
     * @param eye The eye to render, including transformations applied
     */
    @Override
    public void onDrawEye(Eye eye){
        clearBackgroundTo(0.0f, 191.0f, 255.0f, 1.0f);
        ViewMatrix viewMatrix = new ViewMatrix(eye);
        ProjectionMatrix projectionMatrix = new ProjectionMatrix(eye);

        for(GUI gui : guis){
            if(!gui.isLookingAtMe(headView) && gui.getRadius() != 20.0f && !(gui instanceof TexturedGUI)){
                gui.setRadius(20.0f);
            }
            if(gui instanceof TexturedGUI)
            {
                ((TexturedGUI) gui).quad.draw(gui, gui.isLookingAtMe(headView), viewMatrix, projectionMatrix);
            }
            else {
                gui.getGraph().draw(headView, viewMatrix, projectionMatrix);
            }
        }
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
    public void onSurfaceCreated(EGLConfig config) {
        if (guis.size() == 0)
        {
            guis.add(new TexturedGUI(5,0f, 0.0f, context));

            guis.add(new GUI(20, 0.8f, 0.0f, context));
            guis.add(new GUI(20, 0.8f, -1.0f, context));
            guis.add(new GUI(20, 0.8f, 1.0f, context));

            guis.add(new GUI(20, -0.8f, -1.0f, context));
            guis.add(new GUI(20, -0.8f, 0.0f, context));
            guis.add(new GUI(20, -0.8f, 1.0f, context));


        }
/*
        for(GUI gui : guis){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.garrett);
            GUITexture texture = new GUITexture(bitmap);
            gui.setTexture(texture);
            bitmap.recycle();
        }*/
        videoTimer.start();
        timer.start();
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
        for (GUI gui : guis) {
            gui.clean();
        }
        timer.stop();
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
            if(gui.isLookingAtMe(headView) && gui.getRadius() != 10.0f && !(gui instanceof TexturedGUI)){
                gui.setRadius(10.0f);
            }
        }
    }
}