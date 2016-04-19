package com.project.googlecardboard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.project.googlecardboard.bluetooth.BluetoothService;
import com.project.googlecardboard.gui.GUICollection;
import com.project.googlecardboard.render.Renderer;
import com.project.googlecardboard.util.BackgroundThread;
import com.project.googlecardboard.util.IO;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Garrett on 22/10/2015.
 */
public class DroneActivity extends CardboardActivity{

    private Vibrator vibrator;
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
        this.backgroundThread = new BackgroundThread();
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        IO.init(getBaseContext(), vibrator);
        BluetoothService.INSTANCE.init(this);
        while(!BluetoothService.INSTANCE.isEnabled()
                && !BluetoothService.INSTANCE.isDiscovering()){
            enableBluetoothService(BluetoothService.INSTANCE);
        }
        enableBackgroundThread(backgroundThread);
        enableRenderer(Renderer.INSTANCE);
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

    private void enableBackgroundThread(BackgroundThread backgroundThread){
        backgroundThread.start();
    }

    private void enableRenderer(Renderer renderer){
        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(renderer);
        setCardboardView(cardboardView);


        view = (CardboardOverlayView) findViewById(R.id.overlay);
    }

    private void enableBluetoothService(BluetoothService bluetoothService){
        System.out.println("G::Turning bluetooth on.");
        bluetoothService.enable();
        bluetoothService.enableDiscovering();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        BluetoothService.INSTANCE.teardown();
        Renderer.INSTANCE.onRendererShutdown();
        finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
