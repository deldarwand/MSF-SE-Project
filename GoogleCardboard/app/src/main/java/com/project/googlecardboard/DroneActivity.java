package com.project.googlecardboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.project.googlecardboard.gui.GUICollection;
import com.project.googlecardboard.render.Renderer;
import com.project.googlecardboard.util.BackgroundThread;
import com.project.googlecardboard.util.IO;

/**
 * Created by Garrett on 22/10/2015.
 */
public class DroneActivity extends CardboardActivity{

    private Vibrator vibrator;
    //private Renderer renderer;

    private BackgroundThread backgroundThread;

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
        IO.init(getBaseContext(), vibrator);
        GUICollection menu = new GUICollection();
        //this.renderer = new Renderer(menu);
        this.backgroundThread = new BackgroundThread();

        backgroundThread.start();

        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(Renderer.INSTANCE);
        setCardboardView(cardboardView);

        view = (CardboardOverlayView) findViewById(R.id.overlay);
    }

    /**
     * Called when the cardboard's trigger is activated
     */
    @Override
    public void onCardboardTrigger(){
        //view.show3DToast("Hello world");
        Renderer.INSTANCE.onCardboardTrigger();
        //vibrate(50);
    }

    // MISCELLANEOUS

    /**
     * Vibrates the phone
     * @param time Time in milliseconds
     */
    public void vibrate(long time){
        vibrator.vibrate(time);
    }
}
