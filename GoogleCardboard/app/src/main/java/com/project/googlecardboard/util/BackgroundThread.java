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

import pl.googlecardboard.RotationPacket;
import pl.packet.Packet;
import pl.packet.UnknownPacket;

/**
 * Created by Garrett on 08/01/2016.
 */
public class BackgroundThread extends Thread {

    private BluetoothService bluetoothService = BluetoothService.INSTANCE;
    private BluetoothSocket socket;

    public BackgroundThread(){
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        setDaemon(true);
    }

    public void run(){
        try{
            System.out.println("G::Starting Bluetooth");
            this.socket = bluetoothService.connectTo(Constants.SERVER_NAME, Constants.SERVER_UUID);
            System.out.println("G::Bluetooth Started");
            while(!Renderer.INSTANCE.hasShutdown()){
                System.out.println("G::While loop");
                Packet packet = (bluetoothService.isPacketAvailable(socket)) ? bluetoothService.readPacket(socket) : new UnknownPacket();
                //System.out.println("G::Got packet: " + packet);
                for(GUI gui : Renderer.INSTANCE.getMenu()){
                    gui.update(packet);
                }
                packet = new RotationPacket();
                float[] angle = Renderer.INSTANCE.getEulerAngles();
                packet.write((int) (angle[0] * 180 / Math.PI) + "," +
                                (int) (angle[1] * 180 / Math.PI));
                bluetoothService.writePacket(socket, packet);
                Thread.sleep(50);
            }
            bluetoothService.close(socket);
            this.socket = null;
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

}
