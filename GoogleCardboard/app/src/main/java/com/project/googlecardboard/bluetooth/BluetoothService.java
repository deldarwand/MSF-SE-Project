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
        enableBroadcastReceiver();
        return !isEnabled();
    }

    /**
     * Start discovering devices
     */
    public void enableDiscovering(){
        this.isDiscovering = true;
        bluetoothAdaptor.startDiscovery();
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
        this.isDiscovering = false;
        bluetoothAdaptor.cancelDiscovery();
    }

    /**
     * Enable other devices to discover you
     * @param seconds
     */
    public void enableDiscoverability(int seconds){
        this.isDiscoverable = true;
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, seconds);
        activity.startActivity(discoverableIntent);
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
     * @param url MAC Address of device hosting server
     * @return BluetoothDevice hosting that address
     */
    public BluetoothDevice getRemoteDevice(String address){
        return bluetoothAdaptor.getRemoteDevice(address);
    }

    /**
     * Enable receiving discovered devices
     */
    public void enableBroadcastReceiver(){
        activity.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    /**
     * Disable receiving discovered devices
     */
    public void disableBroadcastReceiver(){
        activity.unregisterReceiver(receiver);
    }

    /**
     * Connect to a device with a universally unique identifier
     * @param device Device to connect
     * @param uuid Unique ID
     * @return Socket connected to
     * @throws IOException
     */
    public synchronized BluetoothSocket connect(BluetoothDevice device, UUID uuid) throws IOException{
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

    /**
     * Read from the BluetoothSocket
     * @param socket
     * @return Received byte array
     */
    public synchronized byte[] read(BluetoothSocket socket){
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
     * Write to the BluetoothSocket
     * @param socket
     * @param buffer Sending byte array
     */
    public synchronized void write(BluetoothSocket socket, byte[] buffer){
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
     * Close the BluetoothSocket
     * @param socket
     */
    public synchronized void close(BluetoothSocket socket){
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
