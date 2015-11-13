package com.project.googlecardboard;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.project.googlecardboard.render.Camera;
import com.wulfstan.googlecardboard.R;
import com.project.googlecardboard.render.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Garrett on 22/10/2015.
 */
public class DroneActivity extends CardboardActivity implements CardboardView.StereoRenderer{

    private Vibrator vibrator;
    private Renderer renderer;

    // Matrices
    private Camera camera;

    private CardboardOverlayView view;

    // CARDBOARD ACTIVITY

    /**
     * Called when the activity is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        checkGLError("Init");
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        checkGLError("Vibrator");
        //Shader shader = new Shader(readRawTextFile(R.raw.vertex), readRawTextFile(R.raw.fragment));
        checkGLError("Shader");
        //this.renderer = new Renderer(shader);
        checkGLError("Renderer");

        this.camera = new Camera();


        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        view = (CardboardOverlayView) findViewById(R.id.overlay);
    }

    /**
     * Called when the cardboard's trigger is activated
     */
    @Override
    public void onCardboardTrigger(){
        view.show3DToast("Hello world");
    }

    // CARDBOARD VIEW . STEREO RENDERER

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
        checkGLError("onDrawEye");
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

    // MISCELLANEOUS

    /**
     * Vibrates the phone
     * @param time Time in milliseconds
     */
    public void vibrate(long time){
        vibrator.vibrate(time);
    }

    /**
     * Reads a raw file as a String
     * @param resourceID The resource's ID
     * @return The source code, represented as a String
     */
    private String readRawTextFile(int resourceID) {
        InputStream inputStream = getResources().openRawResource(resourceID);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("DroneActivity", label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }
}
