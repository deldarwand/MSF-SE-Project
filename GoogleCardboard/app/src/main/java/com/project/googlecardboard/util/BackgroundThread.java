package com.project.googlecardboard.util;

import android.bluetooth.BluetoothDevice;

import com.project.googlecardboard.bluetooth.BluetoothService;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUICollection;
import com.project.googlecardboard.gui.GraphGUI;
import com.project.googlecardboard.gui.TexturedGUI;
import com.project.googlecardboard.render.Renderer;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by Garrett on 08/01/2016.
 */
public class BackgroundThread extends Thread {

    public BackgroundThread(){
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        setDaemon(true);
    }

    public void run(){
        while(!Renderer.INSTANCE.hasShutdown()){
            for(GUI gui : Renderer.INSTANCE.getMenu()){
                gui.update();
            }
            /*for(BluetoothDevice device : BluetoothService.INSTANCE.getPairedDevices()){
                System.out.println("Device: " + device.getName() + " | Address: " + device.getAddress());
            }*/
            try{
                Thread.sleep(50);
            } catch(InterruptedException exception){
                exception.printStackTrace();
            }
        }
    }

}
