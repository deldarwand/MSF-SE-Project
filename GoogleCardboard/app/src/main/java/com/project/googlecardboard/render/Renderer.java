package com.project.googlecardboard.render;

import android.opengl.GLES20;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.project.googlecardboard.graph.LineGraph;
import com.project.googlecardboard.graph.PieChart;
import com.project.googlecardboard.gui.ArrowGUI;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUICollection;
import com.project.googlecardboard.gui.GraphGUI;
import com.project.googlecardboard.gui.TexturedGUI;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;
import com.project.googlecardboard.projection.CameraUtil;
import com.project.googlecardboard.render.animation.Animation;
import com.project.googlecardboard.render.animation.PopAnimation;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.Constants;
import com.project.googlecardboard.util.IO;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Garrett on 23/10/2015.
 */

public enum Renderer implements CardboardView.StereoRenderer{

    INSTANCE;

    private GUICollection menu;
    private float[] headView;
    private boolean hasShutdown = false;
    private float[] eulerAngles = new float[3];
    private float GPSLatitude, GPSLongitude;

    private Renderer(){
        this.menu = new GUICollection();
        this.headView = new float[16];
        this.hasShutdown = false;
        GPSLatitude = GPSLongitude = 0.0f;
        GPSLatitude = 51.524173f;
        GPSLongitude = -0.131792f;
    }

    /* GETTERS */

    public GUICollection getMenu(){
        return menu;
    }

    public boolean hasShutdown(){
        return hasShutdown;
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
        for(GUI gui : menu) {
            if (gui.isBeingViewed()) {
                //gui.playAnimation();
            } else {
                // gui.setAnimatedRadius(10.0f, new PopAnimation(6.0f, 10.0f, 1.0f));
            }
            if (gui.getClass() == TexturedGUI.class) {
                ((TexturedGUI) gui).headTran = headTransform;
            }
            else if(gui.getClass() == GraphGUI.class) {
                ((GraphGUI) gui).headTran = headTransform;
            }
        }
        menu.sort();
        headTransform.getEulerAngles(eulerAngles, 0);
        if(hasRolled(headTransform)){
            onCardboardRoll();
        }
    }

    /**
     * Called when a frame is drawn for an eye
     * @param eye The eye to render, including transformations applied
     */
    @Override
    public void onDrawEye(Eye eye){
        clearBackgroundTo(0.0f, 191.0f, 255.0f, 1.0f);
        loadProjectionMatrixAndViewMatrix(new ProjectionMatrix(eye), new ViewMatrix(eye));
        for(GUI gui : menu){
            gui.getShaderType().start();
            gui.updateGUI(headView);
            if(gui instanceof TexturedGUI)
            {
                gui.update(null);
            }
            if(gui instanceof ArrowGUI)
            {
                //GPSLongitude += 0.0000000001;
                //GPSLatitude += 0.0000000001;
                ((ArrowGUI) gui).updateArrow(GPSLongitude, GPSLatitude);
            }
            gui.draw();
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
        this.menu = new GUICollection();
        this.headView = new float[16];
        this.hasShutdown = false;
        if(menu.size() == 0){
            menu.add(new TexturedGUI(11.0f, 0.0f, 0.0f));

            menu.add(new GraphGUI(10, 14.0f, -20.0f, new LineGraph(30)));
            menu.add(new GraphGUI(10, 14.0f, 20.0f, new LineGraph(30)));
            menu.add(new GraphGUI(10, -14.0f, -20.0f, new LineGraph(30)));
            menu.add(new GraphGUI(10, -14.0f, 20.0f, new LineGraph(30)));

            menu.add(new ArrowGUI(1, 0.0f, 0.0f, -0.1390035f, 51.5252657f, 0.0f, 0.0f));

           // menu.add(new ArrowGUI(1, 0.0f, 0.0f, 0.00000000f, -0.000000125f, 0.0f, 0.0f));
            //menu.add(new GraphGUI(10, -24.0f, 30.0f, new PieChart()));
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
        hasShutdown = true;
        for(GUI gui : menu){
            gui.teardown();
        }
        clean();
    }

    /* MISCELLANEOUS */

    private void loadProjectionMatrixAndViewMatrix(ProjectionMatrix pMatrix, ViewMatrix vMatrix){
        for(ShaderType shaderType : ShaderType.values()){
            shaderType.start();
            Shader shader = shaderType.getShader();
            shader.loadProjectionMatrix(pMatrix.getMatrix());
            shader.loadViewMatrix(vMatrix.getMatrix());
        }
    }

    private void clean(){
        for(ShaderType shaderType : ShaderType.values()){
            Shader shader = shaderType.getShader();
            shader.clean();
        }
    }

    /**
     * Clears the background to a specific colour
     * @param red Red component
     * @param green Green component
     * @param blue Blue component
     * @param alpha Transparency component
     */
    public void clearBackgroundTo(float red, float green, float blue, float alpha){
        GLES20.glClearColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Called when the cardboard's trigger is activated
     * First called in DroneActivity, which calls this function
     */
    public void onCardboardTrigger(){
        for(GUI gui : menu){
            if(gui.isBeingViewed()){
                gui.setAnimatedRadius(5.0f, Animation.EMPTY);
            }
        }
    }

    /**
     * Called when the cardboard rolls to the left or the right
     */
    public void onCardboardRoll(){
        //IO.vibrate(50);
    }

    /**
     * Checks whether the user has rolled the phone
     * @param headTransform The head transformation of the phone
     * @return Whether it has rolled
     */
    private boolean hasRolled(HeadTransform headTransform){
        float[] angle = new float[3];
        headTransform.getEulerAngles(angle, 0);
        float roll = angle[2];
        return (Math.abs(roll) > Math.PI/2 - Constants.ROLL_LIMIT) && (Math.abs(roll) < Math.PI/2 + Constants.ROLL_LIMIT);
    }

    public float[] getEulerAngles(){
        return eulerAngles;
    }
}