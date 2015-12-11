package com.project.googlecardboard.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.AsyncTask;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.project.googlecardboard.R;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.graph.BarGraph;
import com.project.googlecardboard.graph.GraphTimer;
import com.project.googlecardboard.graph.LineGraph;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUIModel;
import com.project.googlecardboard.gui.GUITexture;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;

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

    public Renderer(Context context, StaticShader shader){
        this.context = context;
        //this.shader = shader;
        this.model = new GUIModel();
        this.guis = new ArrayList<GUI>();
        this.headView = new float[16];

        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inScaled = false;

        //guis.add(new GUI(20, 0.8f, -1.0f, new LineGraph(20)));
        //guis.add(new GUI(20, 0.8f, 0.0f, context));
        //guis.add(new GUI(20, 0.8f, 1.0f, new LineGraph(20)));

        //guis.add(new GUI(20, -0.8f, -1.0f, new BarGraph(20)));
        //guis.add(new GUI(20, -0.8f, 0.0f, new LineGraph(20)));
        //guis.add(new GUI(20, -0.8f, 1.0f, new LineGraph(20)));

        this.timer = new GraphTimer(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 50, 50);

    }

    public void update(){
        GUI gui = guis.get(0);
            //float rand = random.nextFloat();
            //gui.getGraph().add(rand);
            //gui.getGraph().draw();
        Thread t = new Thread(gui.getGraph());
        t.start();
            //gui.getGraph().run();
    }

    /**
     * Called when a new frame is about to be drawn
     * @param headTransform The head transformation in the new frame
     */
    @Override
    public void onNewFrame(HeadTransform headTransform){
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glLineWidth(40.0f);
        headTransform.getHeadView(headView, 0);
        //shader.start();
        //update();

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
        //shader.loadViewMatrix(viewMatrix.getMatrix());
        //shader.loadProjectionMatrix(projectionMatrix.getMatrix());
        //shader.loadPosition(model.getModel());
        for(GUI gui : guis){
            if(!isLookingAt(gui) && gui.getRadius() != 20.0f){
                gui.setRadius(20.0f);
            }
            gui.getGraph().draw(headView, viewMatrix, projectionMatrix);
            /*shader.loadPosition(gui.getGraph().arrayOfPoints);
            shader.loadTransformationMatrix(gui.getMatrix());
            shader.loadTexture(gui.getTexture().getID());
            shader.loadColour((isLookingAt(gui)) ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
            shader.loadTextureCoordinates(gui.getTexture().textureCoordinates);
            shader.enableAttributes();*/
            //drawGUI(gui);
        }
    }

    /**
     * Called before a frame is finished being drawn
     * @param viewport The viewport of the full GL surface
     */
    @Override
    public void onFinishFrame(Viewport viewport){
//        int resourceID = (index % 2 == 0) ? R.drawable.garrett : R.drawable.googlecardboard;

  //      if (b1 == null)
    //    {
      //      b1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.garrett);
        //    b2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.googlecardboard);
        //}
       // shader.stop();
    }

    /**
     * Called when the surface is created or recreated
     * @param config The EGL configuration used when creating the surface
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        if (guis.size() == 0)
        {
            guis.add(new GUI(20, 0.8f, 0.0f, context));
        }
        /*shader.init();
        shader.loadUniformLocations();
        shader.loadAttributeLocations();*/
        for(GUI gui : guis){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.garrett);
            GUITexture texture = new GUITexture(bitmap);
            gui.setTexture(texture);
            bitmap.recycle();
        }
        //timer.start();
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
        //timer.stop();
    }

    /**
     * Draws the cube
     */
    public void drawGUI(GUI gui){
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, model.getModel().length / 3);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, 6);
        //GLES20.glDrawArrays(GLES20.GL_LINES, 0, model.getModel().length / 2);
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
            if(isLookingAt(gui) && gui.getRadius() != 10.0f){
                gui.setRadius(10.0f);
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