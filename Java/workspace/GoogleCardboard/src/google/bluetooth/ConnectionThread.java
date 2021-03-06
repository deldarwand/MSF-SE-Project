package google.bluetooth;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.fazecast.jSerialComm.SerialPort;

public class ConnectionThread extends Thread{
	
	private final UUID uuid;
	private final String name;
	private final String url;
	private final DataTransmissionServer server;
	private final SerialPort port;
	
	public ConnectionThread(DataTransmissionServer server){
		this.uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
		this.name = "GCServer";
		this.url = "btspp://localhost:" + uuid + ";name=" + name + ";authenticate=false;encrypt=false";
		this.server = server;
		this.port = SerialPort.getCommPort("COM5");
		port.setBaudRate(57600);
		port.openPort();
	}
	
	@Override
	public void run(){
		try{
			establishConnection();
		} catch(BluetoothStateException exception){
			System.err.println("An exception occured in the bluetooth state");
			exception.printStackTrace();
		} catch(IOException exception){
			System.err.println("An IO exception occurred");
			exception.printStackTrace();
		}
	}
	
	private void establishConnection() throws BluetoothStateException, IOException{
		
		System.out.println("Setting device to be discoverable...");
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		System.out.println("Start advertising service...");
		StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector.open(url);
		
		while(true){
			System.out.println("Waiting for incoming connection...");
			StreamConnection connection = notifier.acceptAndOpen();
			System.out.println("Connection establised!");
			Session session = new Session(port, connection, server.getPacketManager());
			server.addSession(session);
			new Thread(session).start();
		}
	}
}
