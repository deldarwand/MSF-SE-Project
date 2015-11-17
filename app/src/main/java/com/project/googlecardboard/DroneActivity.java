package com.project.googlecardboard;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.project.googlecardboard.render.Renderer;
import com.project.googlecardboard.render.Shader;
import com.project.googlecardboard.render.StaticShader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Garrett on 22/10/2015.
 */
public class DroneActivity extends CardboardActivity{

    private Vibrator vibrator;
    private Renderer renderer;

    private CardboardOverlayView view;

    // CARDBOARD ACTIVITY

    /**
     * Called when the activity is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        StaticShader shader = new StaticShader(readRawTextFile(R.raw.vertex), readRawTextFile(R.raw.fragment));
        this.renderer = new Renderer(shader);

        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(renderer);
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
