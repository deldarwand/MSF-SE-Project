package com.project.googlecardboard.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.project.googlecardboard.bluetooth.BluetoothService;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUICollection;
import com.project.googlecardboard.gui.GraphGUI;
import com.project.googlecardboard.gui.TexturedGUI;
import com.project.googlecardboard.render.Renderer;

import java.io.IOException;
import java.util.Random;
import java.util.TimerTask;
import java.util.UUID;

import pl.packet.Packet;

/**
 * Created by Garrett on 08/01/2016.
 */
public class BackgroundThread extends Thread {

    private BluetoothSocket socket;

    public BackgroundThread(){
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        setDaemon(true);
    }

    public void run(){
        try{
            System.out.println("G::Starting Bluetooth");
            this.socket = BluetoothService.INSTANCE.connectTo("LENOVO-PC", UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb"));
            System.out.println("G::Bluetooth Started");
            while(!Renderer.INSTANCE.hasShutdown()){
                System.out.println("G::While loop");
                Packet packet = BluetoothService.INSTANCE.readPacket(socket);
                System.out.println("G::Got packet: " + packet);
                for(GUI gui : Renderer.INSTANCE.getMenu()){
                    gui.update(packet);
                }
                //Thread.sleep(50);
            }
            BluetoothService.INSTANCE.close(socket);
            this.socket = null;
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

}
