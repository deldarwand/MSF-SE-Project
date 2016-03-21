package com.project.googlecardboard.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pl.exception.PLReadException;
import pl.googlecardboard.AltitudePacket;
import pl.googlecardboard.BitmapPacket;
import pl.googlecardboard.GPSPacket;
import pl.googlecardboard.HumidityPacket;
import pl.googlecardboard.RotationPacket;
import pl.googlecardboard.TemperaturePacket;
import pl.packet.Packet;
import pl.packet.PacketManager;

/**
 * Created by Garrett on 23/01/2016.
 */
public enum BluetoothService {

    INSTANCE;

    private static final int DELIMETER = 124;
    private static final int REQUEST_ENABLE_BT = 0;
    private Activity activity;

    private BluetoothAdapter bluetoothAdaptor;
    private boolean isDiscovering;
    private boolean isDiscoverable;
    private boolean receiverRegistered;

    private final PacketManager packetManager;

    private final Set<BluetoothDevice> devices;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device);
            }
        }
    };



    private BluetoothService(){
        this.bluetoothAdaptor = BluetoothAdapter.getDefaultAdapter();
        this.devices = new HashSet<BluetoothDevice>();
        this.isDiscovering = false;
        this.isDiscoverable = false;
        this.receiverRegistered = false;
        this.packetManager = new PacketManager();
        packetManager.addPacketClass('H', HumidityPacket.class);
        packetManager.addPacketClass('T', TemperaturePacket.class);
        packetManager.addPacketClass('G', GPSPacket.class);
        packetManager.addPacketClass('A', AltitudePacket.class);
        packetManager.addPacketClass('B', BitmapPacket.class);
        packetManager.addPacketClass('R', RotationPacket.class);
    }

    /**
     * Initialize the BluetoothService
     * We require the activity for enabling and discovering
     * @param activity
     */
    public void init(Activity activity){
        this.activity = activity;
    }

    /**
     * Teardown the BluetoothService
     * Get rid of Activity reference
     */
    public void teardown(){
        disable();
        this.activity = null;
    }

    /**
     * Enable the BluetoothService
     * @return Success
     */
    public boolean enable(){
        if(!isEnabled()){
            activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
            //enableBroadcastReceiver();
        }
        return isEnabled();
    }

    /**
     * Check whether BluetoothService is enabled
     * @return Enabled
     */
    public boolean isEnabled(){
        return bluetoothAdaptor.isEnabled();
    }

    /**
     * Disable the BluetoothService
     * @return Success
     */
    public boolean disable(){
        disableBroadcastReceiver();
        if(isEnabled()){
            bluetoothAdaptor.disable();
        }
        return !isEnabled();
    }

    /**
     * Start discovering devices
     */
    public void enableDiscovering(){
        bluetoothAdaptor.startDiscovery();
        this.isDiscovering = true;
    }

    /**
     * Check whether is discovering other devices
     * @return
     */
    public boolean isDiscovering(){
        return isDiscovering;
    }

    /**
     * Stop discovering devices
     */
    public void disableDiscovering(){
        bluetoothAdaptor.cancelDiscovery();
        this.isDiscovering = false;
    }

    /**
     * Enable other devices to discover you
     * @param seconds
     */
    public void enableDiscoverability(int seconds){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, seconds);
        activity.startActivity(discoverableIntent);
        this.isDiscoverable = true;
    }

    /**
     * Get paired devices
     * @return Set of BluetoothDevices paired to this device
     */
    public Set<BluetoothDevice> getPairedDevices(){
        return bluetoothAdaptor.getBondedDevices();
    }

    /**
     * Get discovered devices
     * @return Set of BluetoothDevices discovered by this device
     */
    public Set<BluetoothDevice> getDiscoveredDevices(){
        return (isDiscovering()) ? devices : new HashSet<BluetoothDevice>();
    }

    /**
     * Get remote device
     * @param address MAC Address of device hosting server
     * @return BluetoothDevice hosting that address
     */
    public BluetoothDevice getRemoteDevice(String address){
        return bluetoothAdaptor.getRemoteDevice(address);
    }

    /**
     * Enable receiving discovered devices
     */
    public void enableBroadcastReceiver(){
        if(!receiverRegistered){
            activity.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            this.receiverRegistered = true;
        }
    }

    /**
     * Disable receiving discovered devices
     */
    public void disableBroadcastReceiver(){
        if(receiverRegistered){
            activity.unregisterReceiver(receiver);
            this.receiverRegistered = false;
        }
    }

    /**
     * Connect to a device with a universally unique identifier
     * @param device Device to connect
     * @param uuid Unique ID
     * @return Socket connected to
     * @throws IOException
     */
    public BluetoothSocket connect(BluetoothDevice device, UUID uuid) throws IOException{
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
        if(socket == null){
            // Socket was null, so there was an IO problem
            throw new IOException();
        }
        disableDiscovering();
        try{
            socket.connect();
        } catch(IOException connectException){
            System.err.println("Unable to connect to socket: " + socket);
            close(socket);
        }
        return socket;
    }

    public BluetoothSocket connectTo(String deviceName, UUID uuid) throws IOException{
        System.out.println("G::Attempting connection...");
        for(BluetoothDevice device : getPairedDevices()){
            System.out.println("G::Device: " + device.getName() + " | Address: " + device.getAddress());
            if(device.getName().equals(deviceName)){
                System.out.println("G::Connection");
                return connect(device, uuid);
            }
        }
        return null;
    }

    /**
     * Read from the BluetoothSocket
     * @param socket
     * @return Received byte array
     */
    public byte[] read(BluetoothSocket socket){
        byte[] buffer = new byte[1024];
        if(socket == null){
            return buffer;
        }
        try{
            socket.getInputStream().read(buffer);
        } catch(IOException exception){
            System.err.println("Unable to read from socket: " + socket);
            close(socket);
        } finally {
            return buffer;
        }
    }

    /**
     * Read from the BluetoothSocket
     * @param socket
     * @return
     * @throws Exception
     */
    public Packet readPacket(BluetoothSocket socket) throws Exception{
        String str = new String(read(socket)).trim();
        System.out.println("G::Read bytes: " + str);
        return packetManager.new_Packet(str);
    }

    /**
     * Write to the BluetoothSocket
     * @param socket
     * @param buffer Sending byte array
     */
    public void write(BluetoothSocket socket, byte[] buffer){
        if(socket == null){
            return;
        }
        try{
            socket.getOutputStream().write(buffer);
            socket.getOutputStream().write(DELIMETER);
        } catch(IOException exception){
            System.err.println("Unable to write to socket: " + socket + " | " + buffer);
            close(socket);
        }
    }

    /**
     * Write to the BluetoothSocket
     * @param socket
     * @param packet
     * @throws Exception
     */
    public void writePacket(BluetoothSocket socket, Packet packet) throws Exception{
        write(socket, packet.read());
    }

    /**
     * Close the BluetoothSocket
     * @param socket
     */
    public void close(BluetoothSocket socket){
        if(socket == null){
            return;
        }
        try{
            socket.close();
        } catch(IOException exception){
            System.err.println("Unable to close socket: " + socket);
        }
    }

}
